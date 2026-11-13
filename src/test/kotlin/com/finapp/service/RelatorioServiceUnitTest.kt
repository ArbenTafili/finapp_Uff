package com.finapp.service

import com.finapp.dto.TotalPorCategoria
import com.finapp.dto.TotalPorTipo
import com.finapp.model.TipoTransacao
import com.finapp.repository.TransacaoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** RF03 / RNF05: regras de cálculo do relatório mensal, sem banco (as agregações vêm do repositório). */
class RelatorioServiceUnitTest {

    private val transacaoRepository = mockk<TransacaoRepository>()
    private val service = RelatorioService(transacaoRepository)

    private fun simular(
        receitas: String? = null,
        despesas: String? = null,
        porCategoria: List<TotalPorCategoria> = emptyList()
    ) {
        val totais = buildList {
            receitas?.let { add(TotalPorTipo(TipoTransacao.RECEITA, BigDecimal(it))) }
            despesas?.let { add(TotalPorTipo(TipoTransacao.DESPESA, BigDecimal(it))) }
        }
        every { transacaoRepository.somarPorTipo(any(), any()) } returns totais
        every { transacaoRepository.somarPorCategoria(TipoTransacao.DESPESA, any(), any()) } returns porCategoria
    }

    @Test
    fun `consulta o intervalo do primeiro dia do mes ao primeiro dia do mes seguinte`() {
        simular()

        service.gerarRelatorioMensal(YearMonth.of(2028, 2))

        val inicio = LocalDate.of(2028, 2, 1)
        val fim = LocalDate.of(2028, 3, 1)
        verify { transacaoRepository.somarPorTipo(inicio, fim) }
        verify { transacaoRepository.somarPorCategoria(TipoTransacao.DESPESA, inicio, fim) }
    }

    @Test
    fun `navegacao atravessa a virada do ano`() {
        simular()

        val janeiro = service.gerarRelatorioMensal(YearMonth.of(2027, 1))
        val dezembro = service.gerarRelatorioMensal(YearMonth.of(2026, 12))

        assertEquals("2026-12", janeiro.mesAnterior)
        assertEquals("2027-02", janeiro.proximoMes)
        assertEquals("2026-11", dezembro.mesAnterior)
        assertEquals("2027-01", dezembro.proximoMes)
    }

    @Test
    fun `totais sempre saem com duas casas decimais`() {
        simular(receitas = "1500", despesas = "250.5")

        val relatorio = service.gerarRelatorioMensal(YearMonth.of(2026, 9))

        assertEquals(BigDecimal("1500.00"), relatorio.totalReceitas)
        assertEquals(BigDecimal("250.50"), relatorio.totalDespesas)
        assertEquals(BigDecimal("1249.50"), relatorio.saldo)
    }

    @Test
    fun `saldo fica negativo quando as despesas superam as receitas`() {
        simular(receitas = "100.00", despesas = "350.75")

        assertEquals(BigDecimal("-250.75"), service.gerarRelatorioMensal(YearMonth.of(2026, 9)).saldo)
    }

    @Test
    fun `mes so com receitas nao tem distribuicao de despesas`() {
        simular(receitas = "3000.00")

        val relatorio = service.gerarRelatorioMensal(YearMonth.of(2026, 9))

        assertEquals(BigDecimal("0.00"), relatorio.totalDespesas)
        assertTrue(relatorio.despesasPorCategoria.isEmpty())
    }

    @Test
    fun `percentuais em dizima sao arredondados a duas casas`() {
        simular(
            despesas = "300.00",
            porCategoria = listOf(
                TotalPorCategoria(1, "Alimentação", BigDecimal("100.00")),
                TotalPorCategoria(2, "Transporte", BigDecimal("100.00")),
                TotalPorCategoria(3, "Lazer", BigDecimal("100.00"))
            )
        )

        val percentuais = service.gerarRelatorioMensal(YearMonth.of(2026, 9)).despesasPorCategoria.map { it.percentual }

        assertEquals(List(3) { BigDecimal("33.33") }, percentuais)
    }

    @Test
    fun `percentual usa arredondamento meio para cima`() {
        // 2/3 = 66,666...% -> 66,67%; 1/3 = 33,333...% -> 33,33%
        simular(
            despesas = "3.00",
            porCategoria = listOf(
                TotalPorCategoria(1, "Moradia", BigDecimal("2.00")),
                TotalPorCategoria(2, "Saúde", BigDecimal("1.00"))
            )
        )

        val despesas = service.gerarRelatorioMensal(YearMonth.of(2026, 9)).despesasPorCategoria

        assertEquals(listOf(BigDecimal("66.67"), BigDecimal("33.33")), despesas.map { it.percentual })
        assertEquals(listOf(BigDecimal("2.00"), BigDecimal("1.00")), despesas.map { it.valor })
    }

    @Test
    fun `mantem a ordem e os dados de categoria vindos da consulta`() {
        simular(
            despesas = "600.00",
            porCategoria = listOf(
                TotalPorCategoria(9, "Educação Demo", BigDecimal("300.00")),
                TotalPorCategoria(3, "Alimentação", BigDecimal("200.00")),
                TotalPorCategoria(5, "Transporte", BigDecimal("100.00"))
            )
        )

        val despesas = service.gerarRelatorioMensal(YearMonth.of(2026, 9)).despesasPorCategoria

        assertEquals(listOf(9L, 3L, 5L), despesas.map { it.categoriaId })
        assertEquals(listOf("Educação Demo", "Alimentação", "Transporte"), despesas.map { it.categoria })
        assertEquals(listOf(BigDecimal("50.00"), BigDecimal("33.33"), BigDecimal("16.67")), despesas.map { it.percentual })
    }
}
