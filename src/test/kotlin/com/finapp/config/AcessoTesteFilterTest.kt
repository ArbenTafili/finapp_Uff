package com.finapp.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Base64

/** Conta de teste da versão publicada: HTTP Basic liga só com finapp.acesso-teste.habilitado=true. */
@SpringBootTest(
    properties = [
        "finapp.acesso-teste.habilitado=true",
        "finapp.acesso-teste.usuario=teste",
        "finapp.acesso-teste.senha=s3nh@-de-teste"
    ]
)
@AutoConfigureMockMvc
class AcessoTesteFilterTest(@Autowired private val mockMvc: MockMvc) {

    private fun basic(usuario: String, senha: String) =
        "Basic " + Base64.getEncoder().encodeToString("$usuario:$senha".toByteArray())

    @Test
    fun `deve exigir credenciais e informar o desafio Basic`() {
        mockMvc.perform(get("/api/categorias"))
            .andExpect(status().isUnauthorized)
            .andExpect(header().exists("WWW-Authenticate"))
        mockMvc.perform(get("/")).andExpect(status().isUnauthorized)
    }

    @Test
    fun `deve recusar usuario ou senha incorretos e cabecalho malformado`() {
        mockMvc.perform(get("/api/categorias").header("Authorization", basic("teste", "errada")))
            .andExpect(status().isUnauthorized)
        mockMvc.perform(get("/api/categorias").header("Authorization", basic("outro", "s3nh@-de-teste")))
            .andExpect(status().isUnauthorized)
        mockMvc.perform(get("/api/categorias").header("Authorization", "Basic %%%nao-base64"))
            .andExpect(status().isUnauthorized)
        mockMvc.perform(get("/api/categorias").header("Authorization", "Bearer abc"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `deve liberar a conta de teste`() {
        mockMvc.perform(get("/api/categorias").header("Authorization", basic("teste", "s3nh@-de-teste")))
            .andExpect(status().isOk)
    }

    @Test
    fun `verificacao de saude nao exige credenciais`() {
        mockMvc.perform(get("/api/saude")).andExpect(status().isOk)
    }
}
