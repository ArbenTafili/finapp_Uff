package com.finapp.service

import com.finapp.exception.ParametroInvalidoException
import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.TransacaoRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate

/** RF05 / 4.4: conteúdo e formato do CSV (colunas, vírgula decimal, escape e proteção contra fórmulas). */
class ExportacaoServiceTest {

    private val repository = mockk<TransacaoRepository>()
    private val service = ExportacaoService(repository)

    private fun transacao(valor: String, tipo: TipoTransacao, data: String, categoria: String, descricao: String?) =
        Transacao(BigDecimal(valor), tipo, LocalDate.parse(data), descricao, Categoria(categoria, tipo))

    @Test
    fun `deve gerar cabecalho, BOM e linhas no padrao pt-BR`() {
        val csv = service.gerarCsv(
            listOf(
                transacao("4500.00", TipoTransacao.RECEITA, "2026-09-05", "Salário", "Salário de setembro"),
                transacao("1234.5", TipoTransacao.DESPESA, "2026-09-06", "Moradia", null)
            )
        )

        assertTrue(csv.startsWith("﻿"))
        val linhas = csv.removePrefix("﻿").split("\r\n").filter { it.isNotEmpty() }
        assertEquals("Data;Tipo;Categoria;Descrição;Valor", linhas[0])
        assertEquals("05/09/2026;Receita;Salário;Salário de setembro;4500,00", linhas[1])
        assertEquals("06/09/2026;Despesa;Moradia;;1234,50", linhas[2])
    }

    @Test
    fun `deve gerar apenas o cabecalho quando nao ha transacoes`() {
        assertEquals("﻿Data;Tipo;Categoria;Descrição;Valor\r\n", service.gerarCsv(emptyList()))
    }

    @Test
    fun `deve proteger campos com ponto e virgula, aspas e quebra de linha`() {
        assertEquals("\"a;b\"", service.campo("a;b"))
        assertEquals("\"diz \"\"oi\"\"\"", service.campo("diz \"oi\""))
        assertEquals("linha1 linha2", service.campo("linha1\nlinha2"))
    }

    @Test
    fun `deve neutralizar texto que a planilha interpretaria como formula`() {
        assertEquals("'=SOMA(A1)", service.campo("=SOMA(A1)"))
        assertEquals("'+55", service.campo("+55"))
        assertEquals("'-1", service.campo("-1"))
        assertEquals("'@cmd", service.campo("@cmd"))
        assertEquals("normal", service.campo("normal"))
    }

    @Test
    fun `deve rejeitar periodo com data inicial posterior a final`() {
        assertThrows<ParametroInvalidoException> {
            service.exportarCsv(LocalDate.parse("2026-10-01"), LocalDate.parse("2026-09-01"))
        }
    }

    @Test
    fun `deve consultar o repositorio com o periodo informado`() {
        val de = LocalDate.parse("2026-09-01")
        val ate = LocalDate.parse("2026-09-30")
        every { repository.buscarParaExportacao(de, ate) } returns emptyList()

        service.exportarCsv(de, ate)

        io.mockk.verify(exactly = 1) { repository.buscarParaExportacao(de, ate) }
    }
}
