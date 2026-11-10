package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.Meta
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.MetaRepository
import com.finapp.repository.TransacaoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ContaTesteSeederTest {

    private val clock = Clock.fixed(Instant.parse("2026-11-09T12:00:00Z"), ZoneOffset.UTC)
    private val categorias = mockk<CategoriaRepository>()
    private val transacoes = mockk<TransacaoRepository>()
    private val metas = mockk<MetaRepository>()
    private val seeder = ContaTesteSeeder(categorias, transacoes, metas, clock)

    @Test
    fun `nao insere nada quando ja ha dados`() {
        every { transacoes.count() } returns 1
        every { metas.count() } returns 0

        seeder.popular()

        verify(exactly = 0) { transacoes.saveAll(any<List<Transacao>>()) }
        verify(exactly = 0) { metas.saveAll(any<List<Meta>>()) }
    }

    @Test
    fun `popula tres meses fechados e o mes corrente sem datas futuras`() {
        every { transacoes.count() } returns 0
        every { metas.count() } returns 0
        every { categorias.findAll() } returns DataSeeder.CATEGORIAS_PADRAO.map { (n, t) -> Categoria(n, t, true) }
        every { categorias.save(any()) } answers { firstArg() }
        val salvas = slot<List<Transacao>>()
        every { transacoes.saveAll(capture(salvas)) } answers { firstArg() }
        every { metas.saveAll(any<List<Meta>>()) } answers { firstArg() }

        seeder.popular()

        val hoje = LocalDate.of(2026, 11, 9)
        assertTrue(salvas.captured.all { !it.data.isAfter(hoje) }, "não pode haver data futura (ADR 001)")
        assertEquals(setOf("2026-08", "2026-09", "2026-10", "2026-11"), salvas.captured.map { it.data.toString().take(7) }.toSet())
        assertTrue(salvas.captured.any { it.tipo == TipoTransacao.RECEITA })
        verify(exactly = 1) { metas.saveAll(match<List<Meta>> { it.size == 3 }) }
    }
}
