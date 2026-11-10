package com.finapp.service

import com.finapp.model.StatusMeta
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

/**
 * Regras de progresso e status de metas (RNF05): somente BigDecimal, escala fixa de 2 casas.
 *
 * - economizado = receitas − despesas das transações entre o início e o prazo da meta, nunca negativo;
 * - percentual = economizado / alvo, truncado a 2 casas e limitado a 100 (nunca mostra 100% antes de atingir);
 * - status: CANCELADA > CONCLUIDA (economizado ≥ alvo) > VENCIDA (hoje > prazo) > EM_ANDAMENTO.
 */
object CalculoMeta {

    private val CEM = BigDecimal(100)

    fun economizado(saldoDoPeriodo: BigDecimal): BigDecimal =
        saldoDoPeriodo.max(BigDecimal.ZERO).setScale(2, RoundingMode.UNNECESSARY)

    fun percentual(economizado: BigDecimal, alvo: BigDecimal): BigDecimal =
        economizado.multiply(CEM).divide(alvo, 2, RoundingMode.DOWN).min(CEM).setScale(2)

    fun restante(economizado: BigDecimal, alvo: BigDecimal): BigDecimal =
        alvo.subtract(economizado).max(BigDecimal.ZERO).setScale(2, RoundingMode.UNNECESSARY)

    fun status(cancelada: Boolean, economizado: BigDecimal, alvo: BigDecimal, prazo: LocalDate, hoje: LocalDate) = when {
        cancelada -> StatusMeta.CANCELADA
        economizado >= alvo -> StatusMeta.CONCLUIDA
        hoje.isAfter(prazo) -> StatusMeta.VENCIDA
        else -> StatusMeta.EM_ANDAMENTO
    }
}
