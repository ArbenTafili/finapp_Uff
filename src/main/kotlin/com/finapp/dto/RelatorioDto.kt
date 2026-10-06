package com.finapp.dto

import com.finapp.model.TipoTransacao
import java.math.BigDecimal

/** Linha da consulta agregada por tipo (RF03): soma de um mês para RECEITA ou DESPESA. */
data class TotalPorTipo(
    val tipo: TipoTransacao,
    val total: BigDecimal
)

/** Linha da consulta agregada por categoria (RF03). */
data class TotalPorCategoria(
    val categoriaId: Long,
    val categoria: String,
    val total: BigDecimal
)

data class DespesaPorCategoriaResponse(
    val categoriaId: Long,
    val categoria: String,
    val valor: BigDecimal,
    /** Participação no total de despesas do mês, de 0 a 100. */
    val percentual: BigDecimal
)

data class RelatorioMensalResponse(
    /** Mês do relatório no formato AAAA-MM. */
    val mes: String,
    val mesAnterior: String,
    val proximoMes: String,
    val totalReceitas: BigDecimal,
    val totalDespesas: BigDecimal,
    val saldo: BigDecimal,
    /** Distribuição das despesas do mês, da maior para a menor. Receitas não entram. */
    val despesasPorCategoria: List<DespesaPorCategoriaResponse>
)
