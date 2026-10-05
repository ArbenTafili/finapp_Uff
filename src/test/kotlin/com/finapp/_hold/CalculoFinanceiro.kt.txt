package com.finapp.service

import com.finapp.dto.SaldoResponse
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import java.math.BigDecimal
import java.math.RoundingMode

/** Regras de cálculo financeiro (RNF05): somente BigDecimal, sem arredondamento implícito, escala fixa de 2 casas. */
object CalculoFinanceiro {

    private const val ESCALA = 2

    fun total(transacoes: List<Transacao>, tipo: TipoTransacao): BigDecimal =
        transacoes.filter { it.tipo == tipo }
            .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.valor) }
            .setScale(ESCALA, RoundingMode.UNNECESSARY)

    fun saldo(transacoes: List<Transacao>): BigDecimal =
        transacoes.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.impactoNoSaldo()) }
            .setScale(ESCALA, RoundingMode.UNNECESSARY)

    fun resumo(transacoes: List<Transacao>) = SaldoResponse(
        totalReceitas = total(transacoes, TipoTransacao.RECEITA),
        totalDespesas = total(transacoes, TipoTransacao.DESPESA),
        saldo = saldo(transacoes)
    )
}
