package com.finapp.service

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

/** PT30: regras de cálculo (RNF05, precisão absoluta com BigDecimal). */
class CalculoFinanceiroTest {

    private fun receita(valor: String) = transacao(valor, TipoTransacao.RECEITA)
    private fun despesa(valor: String) = transacao(valor, TipoTransacao.DESPESA)

    private fun transacao(valor: String, tipo: TipoTransacao) = Transacao(
        valor = BigDecimal(valor),
        tipo = tipo,
        data = LocalDate.of(2026, 9, 1),
        categoria = Categoria(nome = "Teste", tipo = tipo)
    )

    @Test
    fun `lista vazia resulta em saldo zero com duas casas`() {
        val resumo = CalculoFinanceiro.resumo(emptyList())

        assertEquals("0.00", resumo.saldo.toPlainString())
        assertEquals("0.00", resumo.totalReceitas.toPlainString())
        assertEquals("0.00", resumo.totalDespesas.toPlainString())
    }

    @Test
    fun `saldo e receitas menos despesas`() {
        val saldo = CalculoFinanceiro.saldo(listOf(receita("1000.00"), despesa("250.75"), despesa("49.25")))

        assertEquals(BigDecimal("700.00"), saldo)
    }

    @Test
    fun `soma de centavos nao sofre erro de ponto flutuante`() {
        // Com Double, 0.1 + 0.2 = 0.30000000000000004
        val saldo = CalculoFinanceiro.saldo(listOf(receita("0.10"), receita("0.20")))

        assertEquals("0.30", saldo.toPlainString())
    }

    @Test
    fun `muitas parcelas de centavos somam exatamente`() {
        val transacoes = List(1000) { receita("0.01") }

        assertEquals("10.00", CalculoFinanceiro.saldo(transacoes).toPlainString())
    }

    @Test
    fun `saldo pode ficar negativo`() {
        val saldo = CalculoFinanceiro.saldo(listOf(receita("100.00"), despesa("100.01")))

        assertEquals("-0.01", saldo.toPlainString())
    }

    @Test
    fun `totais por tipo ignoram o outro tipo`() {
        val transacoes = listOf(receita("10.00"), receita("5.50"), despesa("3.25"))

        assertEquals(BigDecimal("15.50"), CalculoFinanceiro.total(transacoes, TipoTransacao.RECEITA))
        assertEquals(BigDecimal("3.25"), CalculoFinanceiro.total(transacoes, TipoTransacao.DESPESA))
    }

    @Test
    fun `resumo e consistente com a formula saldo igual receitas menos despesas`() {
        val resumo = CalculoFinanceiro.resumo(listOf(receita("2500.00"), despesa("1234.56"), despesa("0.44")))

        assertEquals(resumo.totalReceitas.subtract(resumo.totalDespesas), resumo.saldo)
        assertEquals("1265.00", resumo.saldo.toPlainString())
    }

    @Test
    fun `valores grandes mantem precisao`() {
        val saldo = CalculoFinanceiro.saldo(listOf(receita("99999999999999.99"), despesa("0.01")))

        assertEquals("99999999999999.98", saldo.toPlainString())
    }
}
