package com.finapp.service

import com.finapp.dto.CategoriaRequest
import com.finapp.exception.ConflitoException
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class CategoriaServiceTest {

    private val categoriaRepository = mockk<CategoriaRepository>()
    private val transacaoRepository = mockk<TransacaoRepository>()
    private val service = CategoriaService(categoriaRepository, transacaoRepository)

    private fun categoria(ehPadrao: Boolean, tipo: TipoTransacao = TipoTransacao.DESPESA) =
        Categoria(nome = "Pets", tipo = tipo, ehPadrao = ehPadrao).also { it.id = 1L }

    private fun comCategoria(c: Categoria) {
        every { categoriaRepository.findById(1L) } returns Optional.of(c)
    }

    @Test
    fun `criar gera categoria personalizada com nome sem espacos nas pontas`() {
        every { categoriaRepository.existsByNomeIgnoreCaseAndTipo("Pets", TipoTransacao.DESPESA) } returns false
        every { categoriaRepository.save(any()) } answers { firstArg() }

        val criada = service.criar(CategoriaRequest("  Pets  ", TipoTransacao.DESPESA))

        assertEquals("Pets", criada.nome)
        assertFalse(criada.ehPadrao)
    }

    @Test
    fun `criar rejeita nome duplicado do mesmo tipo`() {
        every { categoriaRepository.existsByNomeIgnoreCaseAndTipo("pets", TipoTransacao.DESPESA) } returns true

        assertFailsWith<ConflitoException> { service.criar(CategoriaRequest("pets", TipoTransacao.DESPESA)) }
        verify(exactly = 0) { categoriaRepository.save(any()) }
    }

    @Test
    fun `buscar categoria inexistente lanca nao encontrado`() {
        every { categoriaRepository.findById(9L) } returns Optional.empty()

        assertFailsWith<RecursoNaoEncontradoException> { service.buscarPorId(9L) }
    }

    @Test
    fun `atualizar rejeita categoria padrao`() {
        comCategoria(categoria(ehPadrao = true))

        assertFailsWith<RegraDeNegocioException> {
            service.atualizar(1L, CategoriaRequest("Outro", TipoTransacao.DESPESA))
        }
    }

    @Test
    fun `atualizar rejeita mudanca de tipo quando ha transacoes`() {
        comCategoria(categoria(ehPadrao = false, tipo = TipoTransacao.DESPESA))
        every { transacaoRepository.existsByCategoriaId(1L) } returns true

        assertFailsWith<RegraDeNegocioException> {
            service.atualizar(1L, CategoriaRequest("Pets", TipoTransacao.RECEITA))
        }
    }

    @Test
    fun `atualizar permite renomear categoria com transacoes sem mudar o tipo`() {
        comCategoria(categoria(ehPadrao = false))
        every { categoriaRepository.existsByNomeIgnoreCaseAndTipoAndIdNot("Animais", TipoTransacao.DESPESA, 1L) } returns false
        every { categoriaRepository.save(any()) } answers { firstArg() }

        val atualizada = service.atualizar(1L, CategoriaRequest("Animais", TipoTransacao.DESPESA))

        assertEquals("Animais", atualizada.nome)
    }

    @Test
    fun `atualizar rejeita nome que ja existe em outra categoria`() {
        comCategoria(categoria(ehPadrao = false))
        every { categoriaRepository.existsByNomeIgnoreCaseAndTipoAndIdNot("Lazer", TipoTransacao.DESPESA, 1L) } returns true

        assertFailsWith<ConflitoException> { service.atualizar(1L, CategoriaRequest("Lazer", TipoTransacao.DESPESA)) }
    }

    @Test
    fun `excluir rejeita categoria padrao`() {
        comCategoria(categoria(ehPadrao = true))

        assertFailsWith<RegraDeNegocioException> { service.excluir(1L) }
        verify(exactly = 0) { categoriaRepository.delete(any()) }
    }

    @Test
    fun `excluir rejeita categoria com transacoes e informa a quantidade`() {
        comCategoria(categoria(ehPadrao = false))
        every { transacaoRepository.countByCategoriaId(1L) } returns 3

        val erro = assertFailsWith<RegraDeNegocioException> { service.excluir(1L) }

        assertEquals(true, erro.message!!.contains("3 transação(ões)"))
        verify(exactly = 0) { categoriaRepository.delete(any()) }
    }

    @Test
    fun `excluir remove categoria personalizada sem transacoes`() {
        val c = categoria(ehPadrao = false)
        comCategoria(c)
        every { transacaoRepository.countByCategoriaId(1L) } returns 0
        every { categoriaRepository.delete(c) } returns Unit

        service.excluir(1L)

        verify(exactly = 1) { categoriaRepository.delete(c) }
    }
}
