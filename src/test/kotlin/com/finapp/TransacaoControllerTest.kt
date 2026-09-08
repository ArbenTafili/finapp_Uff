package com.finapp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.finapp.repository.CategoriaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class TransacaoControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val categoriaRepository: CategoriaRepository
) {

    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
    private var categoriaId: Long = 0

    @BeforeEach
    fun setUp() {
        categoriaId = requireNotNull(categoriaRepository.findAll().first().id)
    }

    @Test
    fun `deve criar, listar, atualizar e excluir uma transacao`() {
        val criarBody = """
            {"valor": 150.50, "tipo": "DESPESA", "data": "${LocalDate.now()}", "descricao": "Mercado", "categoriaId": $categoriaId}
        """.trimIndent()

        val criarResponse = mockMvc.perform(
            post("/api/transacoes").contentType(MediaType.APPLICATION_JSON).content(criarBody)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.valor").value(150.50))
            .andExpect(jsonPath("$.tipo").value("DESPESA"))
            .andReturn().response.contentAsString

        val id = objectMapper.readTree(criarResponse).get("id").asLong()

        mockMvc.perform(get("/api/transacoes/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.descricao").value("Mercado"))

        val atualizarBody = """
            {"valor": 200.00, "tipo": "DESPESA", "data": "${LocalDate.now()}", "descricao": "Mercado - ajuste", "categoriaId": $categoriaId}
        """.trimIndent()

        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/transacoes/$id")
                .contentType(MediaType.APPLICATION_JSON).content(atualizarBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.valor").value(200.00))

        mockMvc.perform(delete("/api/transacoes/$id"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/transacoes/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve rejeitar valor menor ou igual a zero`() {
        val body = """
            {"valor": 0, "tipo": "DESPESA", "data": "${LocalDate.now()}", "categoriaId": $categoriaId}
        """.trimIndent()

        mockMvc.perform(post("/api/transacoes").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.detalhes.valor").exists())
    }

    @Test
    fun `deve rejeitar data futura`() {
        val dataFutura = LocalDate.now().plusDays(1)
        val body = """
            {"valor": 50, "tipo": "RECEITA", "data": "$dataFutura", "categoriaId": $categoriaId}
        """.trimIndent()

        mockMvc.perform(post("/api/transacoes").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `deve retornar 404 para categoria inexistente`() {
        val body = """
            {"valor": 50, "tipo": "RECEITA", "data": "${LocalDate.now()}", "categoriaId": 99999}
        """.trimIndent()

        mockMvc.perform(post("/api/transacoes").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isNotFound)
    }
}
