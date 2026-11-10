package com.finapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

/** RF04: CRUD, cancelamento, progresso e integração com transações, em PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MetaControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val categoriaRepository: CategoriaRepository,
    @Autowired private val transacaoRepository: TransacaoRepository
) {

    private val mapper = ObjectMapper()
    private val hoje = LocalDate.now()

    @BeforeEach
    fun limpar() {
        transacaoRepository.deleteAll()
    }

    private fun json(nome: String? = "Viagem", alvo: String? = "1000.00", prazo: LocalDate? = hoje.plusMonths(3)): String {
        val campos = listOfNotNull(
            nome?.let { "\"nome\": \"$it\"" },
            alvo?.let { "\"valorAlvo\": $it" },
            prazo?.let { "\"prazo\": \"$it\"" }
        )
        return "{${campos.joinToString(",")}}"
    }

    private fun criar(body: String = json()): Long {
        val resposta = mockMvc.perform(post("/api/metas").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString
        return mapper.readTree(resposta).get("id").asLong()
    }

    private fun transacionar(tipo: TipoTransacao, valor: String, data: LocalDate = hoje) {
        val categoria = categoriaRepository.findAll().first { it.tipo == tipo }
        transacaoRepository.save(Transacao(BigDecimal(valor), tipo, data, null, categoria))
    }

    @Test
    fun `deve criar meta em andamento com progresso zero`() {
        mockMvc.perform(post("/api/metas").contentType(MediaType.APPLICATION_JSON).content(json()))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.nome").value("Viagem"))
            .andExpect(jsonPath("$.valorAlvo").value(1000.00))
            .andExpect(jsonPath("$.dataInicio").value(hoje.toString()))
            .andExpect(jsonPath("$.valorEconomizado").value(0.0))
            .andExpect(jsonPath("$.percentual").value(0.0))
            .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"))
    }

    @Test
    fun `progresso acompanha o saldo das transacoes desde o inicio da meta`() {
        val id = criar()
        transacionar(TipoTransacao.RECEITA, "800.00")
        transacionar(TipoTransacao.DESPESA, "300.00")

        mockMvc.perform(get("/api/metas/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.valorEconomizado").value(500.00))
            .andExpect(jsonPath("$.valorRestante").value(500.00))
            .andExpect(jsonPath("$.percentual").value(50.00))
            .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"))
    }

    @Test
    fun `transacoes anteriores ao inicio da meta nao contam`() {
        transacionar(TipoTransacao.RECEITA, "5000.00", hoje.minusDays(1))
        val id = criar()

        mockMvc.perform(get("/api/metas/$id"))
            .andExpect(jsonPath("$.valorEconomizado").value(0.0))
    }

    @Test
    fun `deve concluir a meta ao atingir o valor alvo`() {
        val id = criar()
        transacionar(TipoTransacao.RECEITA, "1000.00")

        mockMvc.perform(get("/api/metas/$id"))
            .andExpect(jsonPath("$.status").value("CONCLUIDA"))
            .andExpect(jsonPath("$.percentual").value(100.00))
    }

    @Test
    fun `saldo negativo nao gera progresso negativo`() {
        val id = criar()
        transacionar(TipoTransacao.DESPESA, "200.00")

        mockMvc.perform(get("/api/metas/$id"))
            .andExpect(jsonPath("$.valorEconomizado").value(0.0))
    }

    @Test
    fun `deve validar campos obrigatorios, valor alvo e prazo`() {
        val tipo = MediaType.APPLICATION_JSON
        mockMvc.perform(post("/api/metas").contentType(tipo).content(json(nome = null)))
            .andExpect(status().isBadRequest)
        mockMvc.perform(post("/api/metas").contentType(tipo).content(json(alvo = "0")))
            .andExpect(status().isBadRequest)
        mockMvc.perform(post("/api/metas").contentType(tipo).content(json(alvo = "10.999")))
            .andExpect(status().isBadRequest)
        mockMvc.perform(post("/api/metas").contentType(tipo).content(json(prazo = null)))
            .andExpect(status().isBadRequest)
        mockMvc.perform(post("/api/metas").contentType(tipo).content(json(prazo = hoje.minusDays(1))))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `deve editar nome, valor alvo e prazo`() {
        val id = criar()

        mockMvc.perform(
            put("/api/metas/$id").contentType(MediaType.APPLICATION_JSON)
                .content(json(nome = "Reserva", alvo = "2500.50", prazo = hoje.plusMonths(6)))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.nome").value("Reserva"))
            .andExpect(jsonPath("$.valorAlvo").value(2500.50))
    }

    @Test
    fun `deve cancelar meta e impedir edicao e novo cancelamento`() {
        val id = criar()

        mockMvc.perform(patch("/api/metas/$id/cancelar"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("CANCELADA"))
        mockMvc.perform(patch("/api/metas/$id/cancelar")).andExpect(status().isUnprocessableEntity)
        mockMvc.perform(put("/api/metas/$id").contentType(MediaType.APPLICATION_JSON).content(json()))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `meta concluida nao pode ser cancelada nem editada`() {
        val id = criar()
        transacionar(TipoTransacao.RECEITA, "1000.00")

        mockMvc.perform(patch("/api/metas/$id/cancelar")).andExpect(status().isUnprocessableEntity)
        mockMvc.perform(put("/api/metas/$id").contentType(MediaType.APPLICATION_JSON).content(json()))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `deve listar por prazo e excluir`() {
        val longe = criar(json(nome = "Longe", prazo = hoje.plusMonths(6)))
        criar(json(nome = "Perto", prazo = hoje.plusDays(10)))

        mockMvc.perform(get("/api/metas"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].nome").value("Perto"))
            .andExpect(jsonPath("$[1].nome").value("Longe"))

        mockMvc.perform(delete("/api/metas/$longe")).andExpect(status().isNoContent)
        mockMvc.perform(get("/api/metas/$longe")).andExpect(status().isNotFound)
    }
}
