package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.repository.CategoriaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataSeederTest {

    private val repository = mockk<CategoriaRepository>()

    @Test
    fun `cria todas as categorias padrao quando o banco esta vazio`() {
        val salvas = mutableListOf<Categoria>()
        every { repository.existsByNomeIgnoreCaseAndTipo(any(), any()) } returns false
        every { repository.save(capture(salvas)) } answers { firstArg() }

        DataSeeder(repository).run()

        assertEquals(DataSeeder.CATEGORIAS_PADRAO.size, salvas.size)
        assertTrue(salvas.all { it.ehPadrao })
        assertTrue(salvas.any { it.tipo == TipoTransacao.RECEITA })
        assertTrue(salvas.any { it.tipo == TipoTransacao.DESPESA })
    }

    @Test
    fun `nao duplica categorias ja existentes`() {
        every { repository.existsByNomeIgnoreCaseAndTipo(any(), any()) } returns true

        DataSeeder(repository).run()

        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `cria apenas a categoria padrao que falta`() {
        val salva = slot<Categoria>()
        every { repository.existsByNomeIgnoreCaseAndTipo(any(), any()) } returns true
        every { repository.existsByNomeIgnoreCaseAndTipo("Lazer", TipoTransacao.DESPESA) } returns false
        every { repository.save(capture(salva)) } answers { firstArg() }

        DataSeeder(repository).run()

        verify(exactly = 1) { repository.save(any()) }
        assertEquals("Lazer", salva.captured.nome)
    }
}
