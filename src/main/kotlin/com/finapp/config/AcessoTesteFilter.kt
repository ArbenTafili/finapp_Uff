package com.finapp.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

@ConfigurationProperties(prefix = "finapp.acesso-teste")
data class AcessoTesteProperties(
    val habilitado: Boolean = false,
    val usuario: String = "",
    val senha: String = ""
)

@Configuration
@EnableConfigurationProperties(AcessoTesteProperties::class)
class AcessoTesteConfig

/**
 * Conta de teste para a versão publicada: protege a aplicação inteira com HTTP Basic e um único par
 * usuário/senha vindo do ambiente (FINAPP_ACESSO_TESTE_USUARIO / FINAPP_ACESSO_TESTE_SENHA).
 *
 * Não é um sistema de usuários: o FinApp continua sem entidade Usuario (fora do escopo, ver ADR 001),
 * e os dados são compartilhados por quem tiver a credencial. Desligado por padrão (dev, testes e Docker local).
 */
@Component
@ConditionalOnProperty(prefix = "finapp.acesso-teste", name = ["habilitado"], havingValue = "true")
class AcessoTesteFilter(private val props: AcessoTesteProperties) : OncePerRequestFilter() {

    init {
        require(props.usuario.isNotBlank() && props.senha.isNotBlank()) {
            "finapp.acesso-teste.habilitado=true exige FINAPP_ACESSO_TESTE_USUARIO e FINAPP_ACESSO_TESTE_SENHA"
        }
    }

    // Verificação de saúde da plataforma de hospedagem não leva credenciais
    override fun shouldNotFilter(request: HttpServletRequest): Boolean = request.requestURI == "/api/saude"

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (credenciaisValidas(request.getHeader("Authorization"))) {
            chain.doFilter(request, response)
            return
        }
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.setHeader("WWW-Authenticate", "Basic realm=\"FinApp (conta de teste)\", charset=\"UTF-8\"")
        response.contentType = "text/plain;charset=UTF-8"
        response.writer.write("Autenticação necessária: use a conta de teste fornecida pela equipe.")
    }

    private fun credenciaisValidas(cabecalho: String?): Boolean {
        if (cabecalho == null || !cabecalho.startsWith("Basic ", ignoreCase = true)) return false
        val decodificado = try {
            String(Base64.getDecoder().decode(cabecalho.substring(6).trim()), StandardCharsets.UTF_8)
        } catch (e: IllegalArgumentException) {
            return false
        }
        val separador = decodificado.indexOf(':')
        if (separador < 0) return false
        val usuarioOk = iguais(decodificado.substring(0, separador), props.usuario)
        val senhaOk = iguais(decodificado.substring(separador + 1), props.senha)
        return usuarioOk && senhaOk
    }

    /** Comparação em tempo constante, para não vazar o tamanho do prefixo correto. */
    private fun iguais(a: String, b: String): Boolean =
        MessageDigest.isEqual(a.toByteArray(StandardCharsets.UTF_8), b.toByteArray(StandardCharsets.UTF_8))
}
