package com.finapp.service

import com.finapp.dto.DespesaPorCategoriaResponse
import com.finapp.dto.RelatorioMensalResponse
import com.finapp.model.TipoTransacao
import com.finapp.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth

@Service
@Transactional(readOnly = true)
class RelatorioService(private val transacaoRepository: TransacaoRepository) {

    /** RF03: totais, saldo e distribuição das despesas por categoria de um mês, a partir de agregações feitas no banco. */
    fun gerarRelatorioMensal(mes: YearMonth): RelatorioMensalResponse {
        val inicio = mes.atDay(1)
        val fim = mes.plusMonths(1).atDay(1)

        val totais = transacaoRepository.somarPorTipo(inicio, fim).associate { it.tipo to it.total }
        val totalReceitas = emReais(totais[TipoTransacao.RECEITA] ?: BigDecimal.ZERO)
        val totalDespesas = emReais(totais[TipoTransacao.DESPESA] ?: BigDecimal.ZERO)

        val despesasPorCategoria = transacaoRepository.somarPorCategoria(TipoTransacao.DESPESA, inicio, fim).map {
            DespesaPorCategoriaResponse(
                categoriaId = it.categoriaId,
                categoria = it.categoria,
                valor = emReais(it.total),
                percentual = percentual(it.total, totalDespesas)
            )
        }

        return RelatorioMensalResponse(
            mes = mes.toString(),
            mesAnterior = mes.minusMonths(1).toString(),
            proximoMes = mes.plusMonths(1).toString(),
            totalReceitas = totalReceitas,
            totalDespesas = totalDespesas,
            saldo = totalReceitas.subtract(totalDespesas),
            despesasPorCategoria = despesasPorCategoria
        )
    }

    private fun emReais(valor: BigDecimal): BigDecimal = valor.setScale(2, RoundingMode.HALF_UP)

    /** Arredondado a 2 casas; por isso a soma dos percentuais pode diferir de 100 em centésimos. */
    private fun percentual(parte: BigDecimal, total: BigDecimal): BigDecimal =
        if (total.signum() == 0) BigDecimal.ZERO.setScale(2)
        else parte.multiply(BigDecimal(100)).divide(total, 2, RoundingMode.HALF_UP)
}
