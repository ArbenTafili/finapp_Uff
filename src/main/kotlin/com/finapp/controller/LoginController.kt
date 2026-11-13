package com.finapp.controller

import com.finapp.config.AcessoTesteFilter
import com.finapp.config.AcessoTesteProperties
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(val usuario: String? = null, val senha: String? = null)

/**
 * Login da conta de teste da versão publicada. Existe só quando finapp.acesso-teste.habilitado=true.
 * Não há cadastro nem entidade Usuario (fora do escopo): a credencial única vem do ambiente.
 */
@RestController
@RequestMapping("/api")
@ConditionalOnProperty(prefix = "finapp.acesso-teste", name = ["habilitado"], havingValue = "true")
class LoginController(private val props: AcessoTesteProperties) {

    @PostMapping("/login")
    fun login(@RequestBody corpo: LoginRequest, request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val ok = AcessoTesteFilter.iguais(corpo.usuario.orEmpty(), props.usuario) &&
            AcessoTesteFilter.iguais(corpo.senha.orEmpty(), props.senha)
        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("mensagem" to "Usuário ou senha incorretos."))
        }
        // Nova sessão a cada login evita fixação de sessão
        request.getSession(false)?.invalidate()
        request.getSession(true).setAttribute(AcessoTesteFilter.ATRIBUTO_SESSAO, true)
        return ResponseEntity.ok(mapOf("usuario" to props.usuario))
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Void> {
        request.getSession(false)?.invalidate()
        return ResponseEntity.noContent().build()
    }

    /** Protegido pelo filtro: 200 se há sessão válida, 401 caso contrário. */
    @GetMapping("/sessao")
    fun sessao(): Map<String, String> = mapOf("usuario" to props.usuario)
}
