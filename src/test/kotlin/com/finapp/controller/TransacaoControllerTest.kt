package com.finapp.controller

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature
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
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        .configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, false)
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
    fun `deve rejeitar valor com mais de duas casas decimais em vez de arredondar`() {
        postar(json("10.999", "DESPESA", LocalDate.now(), despesaId))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.detalhes.valor").value("O valor deve ter no máximo 2 casas decimais"))
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

    @Test
    fun `deve retornar 400 para JSON malformado ou tipo invalido`() {
        postar("{ nao e json").andExpect(status().isBadRequest)
        postar(json("50", "TRANSFERENCIA", LocalDate.now(), despesaId))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.erro").value("Dados inválidos"))
    }

    @Test
    fun `deve retornar 400 para id nao numerico e 404 para id inexistente`() {
        mockMvc.perform(get("/api/transacoes/abc")).andExpect(status().isBadRequest)
        mockMvc.perform(get("/api/transacoes/99999")).andExpect(status().isNotFound)
        mockMvc.perform(delete("/api/transacoes/99999")).andExpect(status().isNotFound)
    }

    @Test
    fun `saldo deve ser receitas menos despesas com precisao exata`() {
        val antes = objectMapper.readTree(
            mockMvc.perform(get("/api/transacoes/saldo")).andReturn().response.contentAsString
        )
        postar(json("0.10", "RECEITA", LocalDate.now(), receitaId)).andExpect(status().isCreated)
        postar(json("0.20", "RECEITA", LocalDate.now(), receitaId)).andExpect(status().isCreated)
        postar(json("0.05", "DESPESA", LocalDate.now(), despesaId)).andExpect(status().isCreated)

        val depois = objectMapper.readTree(
            mockMvc.perform(get("/api/transacoes/saldo")).andExpect(status().isOk).andReturn().response.contentAsString
        )

        fun dif(campo: String) = depois.get(campo).decimalValue().subtract(antes.get(campo).decimalValue())
        kotlin.test.assertEquals("0.30", dif("totalReceitas").toPlainString())
        kotlin.test.assertEquals("0.05", dif("totalDespesas").toPlainString())
        kotlin.test.assertEquals("0.25", dif("saldo").toPlainString())
    }
}
