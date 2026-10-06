package com.finapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.finapp.model.TipoTransacao
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/** Integração dos endpoints de transação (RF01, PT12). Cada teste é revertido ao final (@Transactional). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransacaoControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val categoriaRepository: CategoriaRepository
) {

    private val objectMapper = ObjectMapper()
    private var despesaId: Long = 0
    private var receitaId: Long = 0

    @BeforeEach
    fun setUp() {
        despesaId = requireNotNull(categoriaRepository.findAll().first { it.tipo == TipoTransacao.DESPESA }.id)
        receitaId = requireNotNull(categoriaRepository.findAll().first { it.tipo == TipoTransacao.RECEITA }.id)
    }

    private fun json(valor: String, tipo: String, data: LocalDate, categoriaId: Long, descricao: String? = null): String {
        val desc = descricao?.let { ""","descricao": "$it"""" } ?: ""
        return """{"valor": $valor, "tipo": "$tipo", "data": "$data", "categoriaId": $categoriaId$desc}"""
    }

    private fun postar(body: String) =
        mockMvc.perform(post("/api/transacoes").contentType(MediaType.APPLICATION_JSON).content(body))

    @Test
    fun `deve criar, listar, atualizar e excluir uma transacao`() {
        val criarResponse = postar(json("150.50", "DESPESA", LocalDate.now(), despesaId, "Mercado"))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.valor").value(150.50))
            .andExpect(jsonPath("$.tipo").value("DESPESA"))
            .andReturn().response.contentAsString
        val id = objectMapper.readTree(criarResponse).get("id").asLong()

        mockMvc.perform(get("/api/transacoes/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.descricao").value("Mercado"))

        mockMvc.perform(get("/api/transacoes"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[?(@.id == $id)]").exists())

        mockMvc.perform(
            put("/api/transacoes/$id").contentType(MediaType.APPLICATION_JSON)
                .content(json("200.00", "DESPESA", LocalDate.now(), despesaId, "Mercado - ajuste"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.valor").value(200.00))
            .andExpect(jsonPath("$.descricao").value("Mercado - ajuste"))

        mockMvc.perform(delete("/api/transacoes/$id")).andExpect(status().isNoContent)
        mockMvc.perform(get("/api/transacoes/$id")).andExpect(status().isNotFound)
    }

    @Test
    fun `deve aceitar data passada (gasto retroativo, ADR 001)`() {
        postar(json("10.00", "DESPESA", LocalDate.now().minusMonths(3), despesaId))
            .andExpect(status().isCreated)
    }

    @Test
    fun `deve rejeitar valor menor ou igual a zero`() {
        postar(json("0", "DESPESA", LocalDate.now(), despesaId))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.detalhes.valor").exists())
        postar(json("-5.00", "DESPESA", LocalDate.now(), despesaId))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `deve preservar centavos exatamente na resposta`() {
        postar(json("0.10", "RECEITA", LocalDate.now(), receitaId))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.valor").value(0.10))
    }

    @Test
    fun `deve rejeitar data futura com 422 e mensagem clara`() {
        postar(json("50", "DESPESA", LocalDate.now().plusDays(1), despesaId))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.mensagem").value("A data da transação não pode ser posterior à data atual"))
    }

    @Test
    fun `deve exigir categoria, tipo e data`() {
        postar("""{"valor": 10}""")
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.detalhes.categoriaId").exists())
            .andExpect(jsonPath("$.detalhes.tipo").exists())
            .andExpect(jsonPath("$.detalhes.data").exists())
    }

    @Test
    fun `deve retornar 404 para categoria inexistente`() {
        postar(json("50", "RECEITA", LocalDate.now(), 99999))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve rejeitar tipo diferente do tipo da categoria`() {
        postar(json("50", "RECEITA", LocalDate.now(), despesaId))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.erro").value("Regra de negócio violada"))
    }

    @Test
    fun `deve rejeitar atualizacao que deixa tipo incompativel com a categoria`() {
        val id = objectMapper.readTree(
            postar(json("50", "DESPESA", LocalDate.now(), despesaId)).andReturn().response.contentAsString
        ).get("id").asLong()

        mockMvc.perform(
            put("/api/transacoes/$id").contentType(MediaType.APPLICATION_JSON)
                .content(json("50", "RECEITA", LocalDate.now(), despesaId))
        ).andExpect(status().isUnprocessableEntity)
    }

}
