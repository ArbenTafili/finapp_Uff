package com.finapp.controller

import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

/** RF05: GET /api/transacoes/exportar com filtro de período, em PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ExportacaoControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val categoriaRepository: CategoriaRepository,
    @Autowired private val transacaoRepository: TransacaoRepository
) {

    @BeforeEach
    fun setUp() {
        transacaoRepository.deleteAll()
        val receita = categoriaRepository.findAll().first { it.tipo == TipoTransacao.RECEITA }
        val despesa = categoriaRepository.findAll().first { it.tipo == TipoTransacao.DESPESA }
        transacaoRepository.saveAll(
            listOf(
                Transacao(BigDecimal("3000.00"), TipoTransacao.RECEITA, LocalDate.of(2025, 8, 31), "Agosto", receita),
                Transacao(BigDecimal("200.50"), TipoTransacao.DESPESA, LocalDate.of(2025, 9, 1), "Primeiro dia", despesa),
                Transacao(BigDecimal("99.90"), TipoTransacao.DESPESA, LocalDate.of(2025, 9, 30), "Ultimo dia", despesa),
                Transacao(BigDecimal("10.00"), TipoTransacao.DESPESA, LocalDate.of(2025, 10, 1), "Outubro", despesa)
            )
        )
    }

    private fun baixar(vararg params: Pair<String, String>): String {
        val req = get("/api/transacoes/exportar")
        params.forEach { req.param(it.first, it.second) }
        return mockMvc.perform(req)
            .andExpect(status().isOk)
            .andExpect(header().string("Content-Type", "text/csv;charset=UTF-8"))
            .andReturn().response.getContentAsString(Charsets.UTF_8)
    }

    @Test
    fun `deve exportar apenas o periodo informado, com limites inclusivos`() {
        val csv = baixar("de" to "2025-09-01", "ate" to "2025-09-30")
        val linhas = csv.removePrefix("﻿").split("\r\n").filter { it.isNotEmpty() }

        assertEquals(3, linhas.size)
        assertEquals("01/09/2025;Despesa;${linhas[1].split(";")[2]};Primeiro dia;200,50", linhas[1])
        assertTrue(linhas[2].endsWith(";Ultimo dia;99,90"))
        assertFalse(csv.contains("Agosto"))
        assertFalse(csv.contains("Outubro"))
    }

    @Test
    fun `deve exportar tudo em ordem cronologica sem filtro`() {
        val linhas = baixar().removePrefix("﻿").split("\r\n").filter { it.isNotEmpty() }
        assertEquals(5, linhas.size)
        assertTrue(linhas[1].startsWith("31/08/2025"))
        assertTrue(linhas[4].startsWith("01/10/2025"))
    }

    @Test
    fun `deve sugerir o nome do arquivo com o periodo`() {
        mockMvc.perform(get("/api/transacoes/exportar").param("de", "2025-09-01").param("ate", "2025-09-30"))
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"finapp-transacoes-2025-09-01_2025-09-30.csv\""))
    }

    @Test
    fun `deve responder 400 para periodo invertido ou data invalida`() {
        mockMvc.perform(get("/api/transacoes/exportar").param("de", "2025-10-01").param("ate", "2025-09-01"))
            .andExpect(status().isBadRequest)
        mockMvc.perform(get("/api/transacoes/exportar").param("de", "01/09/2025"))
            .andExpect(status().isBadRequest)
    }
}
