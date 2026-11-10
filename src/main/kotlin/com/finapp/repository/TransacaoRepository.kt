package com.finapp.repository

import com.finapp.dto.TotalPorCategoria
import com.finapp.dto.TotalPorTipo
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface TransacaoRepository : JpaRepository<Transacao, Long> {
    fun existsByCategoriaId(categoriaId: Long): Boolean

    fun countByCategoriaId(categoriaId: Long): Long

    /** RF03: total por tipo no intervalo [inicio, fim), somado no banco. Tipos sem transação não aparecem. */
    @Query(
        """
        select new com.finapp.dto.TotalPorTipo(t.tipo, sum(t.valor))
        from Transacao t
        where t.data >= :inicio and t.data < :fim
        group by t.tipo
        """
    )
    fun somarPorTipo(@Param("inicio") inicio: LocalDate, @Param("fim") fim: LocalDate): List<TotalPorTipo>

    /** RF03: total por categoria das transações de um tipo no intervalo [inicio, fim), do maior para o menor. */
    @Query(
        """
        select new com.finapp.dto.TotalPorCategoria(c.id, c.nome, sum(t.valor))
        from Transacao t join t.categoria c
        where t.tipo = :tipo and t.data >= :inicio and t.data < :fim
        group by c.id, c.nome
        order by sum(t.valor) desc, c.nome asc
        """
    )
    fun somarPorCategoria(
        @Param("tipo") tipo: TipoTransacao,
        @Param("inicio") inicio: LocalDate,
        @Param("fim") fim: LocalDate
    ): List<TotalPorCategoria>

    /** RF05: transações no intervalo fechado [de, ate], em ordem cronológica. Limites nulos não restringem. */
    @Query(
        """
        select t from Transacao t join fetch t.categoria
        where (cast(:de as date) is null or t.data >= :de) and (cast(:ate as date) is null or t.data <= :ate)
        order by t.data asc, t.id asc
        """
    )
    fun buscarParaExportacao(@Param("de") de: LocalDate?, @Param("ate") ate: LocalDate?): List<Transacao>

    /** RF04: saldo (receitas − despesas) no intervalo fechado [inicio, fim], somado no banco. Sem transações, devolve 0. */
    @Query(
        """
        select coalesce(sum(case when t.tipo = com.finapp.model.TipoTransacao.RECEITA then t.valor else -t.valor end), 0)
        from Transacao t
        where t.data >= :inicio and t.data <= :fim
        """
    )
    fun saldoNoPeriodo(@Param("inicio") inicio: LocalDate, @Param("fim") fim: LocalDate): java.math.BigDecimal
}
