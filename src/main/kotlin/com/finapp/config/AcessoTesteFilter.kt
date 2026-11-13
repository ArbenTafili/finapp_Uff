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
 * Conta de teste para a versão publicada: protege a aplicação inteira com uma tela de login (sessão por cookie,
 * ver [LoginController]) e um único par usuário/senha vindo do ambiente (FINAPP_ACESSO_TESTE_USUARIO /
 * FINAPP_ACESSO_TESTE_SENHA). Clientes de API ainda podem usar HTTP Basic.
 *
 * Não é um sistema de usuários: o FinApp continua sem entidade Usuario (fora do escopo, ver ADR 001),
 * e os dados são compartilhados por quem tiver a credencial. Desligado por padrão (dev, testes e Docker local).
 */
@Component
@ConditionalOnProperty(prefix = "finapp.acesso-teste", name = ["habilitado"], havingValue = "true")
class AcessoTesteFilter(private val props: AcessoTesteProperties) : OncePerRequestFilter() {

    companion object {
        /** Atributo de sessão que marca quem passou pela tela de login. */
        const val ATRIBUTO_SESSAO = "finapp.acesso-teste.autenticado"

        // Rotas acessíveis sem autenticação: página de login, envio do login e verificação de saúde
        private val ROTAS_PUBLICAS = setOf("/login.html", "/api/login", "/api/saude", "/favicon.ico")

        /** Comparação em tempo constante, para não vazar o tamanho do prefixo correto. */
        fun iguais(a: String, b: String): Boolean =
            MessageDigest.isEqual(a.toByteArray(StandardCharsets.UTF_8), b.toByteArray(StandardCharsets.UTF_8))
    }

    init {
        require(props.usuario.isNotBlank() && props.senha.isNotBlank()) {
            "finapp.acesso-teste.habilitado=true exige FINAPP_ACESSO_TESTE_USUARIO e FINAPP_ACESSO_TESTE_SENHA"
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean = request.requestURI in ROTAS_PUBLICAS

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (sessaoAutenticada(request) || credenciaisBasicValidas(request.getHeader("Authorization"))) {
            chain.doFilter(request, response)
            return
        }
        if (request.requestURI.startsWith("/api/")) {
            // Sem WWW-Authenticate: o navegador não deve abrir o pop-up do Basic, a interface redireciona ao login
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "text/plain;charset=UTF-8"
            response.writer.write("Autenticação necessária: faça login com a conta de teste.")
        } else {
            response.sendRedirect("/login.html")
        }
    }

    private fun sessaoAutenticada(request: HttpServletRequest): Boolean =
        request.getSession(false)?.getAttribute(ATRIBUTO_SESSAO) == true

    // Mantido para clientes de API (curl, scripts); a interface usa a sessão
    private fun credenciaisBasicValidas(cabecalho: String?): Boolean {
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
}
