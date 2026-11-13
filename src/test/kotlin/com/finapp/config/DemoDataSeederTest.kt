package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

/** Perfil demo (Issue #29): massa base D1–D4 do roteiro de aceite da Demo v2. */
class DemoDataSeederTest {

    private val categoriaRepository = mockk<CategoriaRepository>()
    private val transacaoRepository = mockk<TransacaoRepository>()
    private val seeder = DemoDataSeeder(categoriaRepository, transacaoRepository)

    private val padrao = listOf(
        Categoria(nome = "Salário", tipo = TipoTransacao.RECEITA, ehPadrao = true).apply { id = 1L },
        Categoria(nome = "Alimentação", tipo = TipoTransacao.DESPESA, ehPadrao = true).apply { id = 3L },
        Categoria(nome = "Transporte", tipo = TipoTransacao.DESPESA, ehPadrao = true).apply { id = 5L }
    )

    private fun capturarTransacoes(): io.mockk.CapturingSlot<List<Transacao>> {
        val salvas = slot<List<Transacao>>()
        every { transacaoRepository.saveAll(capture(salvas)) } answers { salvas.captured }
        return salvas
    }

    @Test
    fun `nao insere nada quando ja existem transacoes`() {
        every { transacaoRepository.count() } returns 3

        seeder.popular()

        verify(exactly = 0) { categoriaRepository.findAll() }
        verify(exactly = 0) { transacaoRepository.saveAll(any<List<Transacao>>()) }
    }

    @Test
    fun `insere D1 a D4 e cria a categoria Educacao Demo quando o banco esta vazio`() {
        every { transacaoRepository.count() } returns 0
        every { categoriaRepository.findAll() } returns padrao
        val criada = slot<Categoria>()
        every { categoriaRepository.save(capture(criada)) } answers { criada.captured.apply { id = 9L } }
        val salvas = capturarTransacoes()

        seeder.popular()

        assertEquals("Educação Demo", criada.captured.nome)
        assertEquals(TipoTransacao.DESPESA, criada.captured.tipo)
        assertEquals(false, criada.captured.ehPadrao)

        val transacoes = salvas.captured
        assertEquals(4, transacoes.size)
        assertEquals(
            listOf("Salário Demo v2", "Mercado Demo v2", "Transporte Demo v2", "Curso Demo v2"),
            transacoes.map { it.descricao }
        )
        assertEquals(listOf("Salário", "Alimentação", "Transporte", "Educação Demo"), transacoes.map { it.categoria.nome })
        assertEquals((1..4).map { LocalDate.of(2026, 9, it) }, transacoes.map { it.data })
    }

    @Test
    fun `massa base produz os totais esperados pelo roteiro`() {
        every { transacaoRepository.count() } returns 0
        every { categoriaRepository.findAll() } returns padrao
        every { categoriaRepository.save(any()) } answers { firstArg<Categoria>().apply { id = 9L } }
        val salvas = capturarTransacoes()

        seeder.popular()

        val receitas = salvas.captured.filter { it.tipo == TipoTransacao.RECEITA }.sumOf { it.valor }
        val despesas = salvas.captured.filter { it.tipo == TipoTransacao.DESPESA }.sumOf { it.valor }
        assertEquals(BigDecimal("3000.00"), receitas)
        assertEquals(BigDecimal("600.00"), despesas)
        assertEquals(BigDecimal("2400.00"), receitas - despesas)
    }

    @Test
    fun `reaproveita a categoria Educacao Demo se ela ja existir`() {
        val existente = Categoria(nome = "Educação Demo", tipo = TipoTransacao.DESPESA).apply { id = 9L }
        every { transacaoRepository.count() } returns 0
        every { categoriaRepository.findAll() } returns padrao + existente
        val salvas = capturarTransacoes()

        seeder.popular()

        verify(exactly = 0) { categoriaRepository.save(any()) }
        assertEquals(existente, salvas.captured.last().categoria)
    }

    @Test
    fun `nao insere nada se faltar alguma categoria padrao`() {
        every { transacaoRepository.count() } returns 0
        every { categoriaRepository.findAll() } returns padrao.filter { it.nome != "Transporte" }

        seeder.popular()

        verify(exactly = 0) { categoriaRepository.save(any()) }
        verify(exactly = 0) { transacaoRepository.saveAll(any<List<Transacao>>()) }
    }
}
