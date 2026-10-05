package com.finapp.service

import com.finapp.dto.TransacaoRequest
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.TransacaoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TransacaoServiceTest {

    private val hoje = LocalDate.now()

    private val transacaoRepository = mockk<TransacaoRepository>()
    private val categoriaService = mockk<CategoriaService>()
    private val service = TransacaoService(transacaoRepository, categoriaService)

    private val despesaCat = Categoria("Alimentação", TipoTransacao.DESPESA, true).also { it.id = 1L }
    private val receitaCat = Categoria("Salário", TipoTransacao.RECEITA, true).also { it.id = 2L }

    private fun request(
        valor: String = "50.00",
        tipo: TipoTransacao = TipoTransacao.DESPESA,
        data: LocalDate = hoje,
        descricao: String? = null,
        categoriaId: Long = 1L
    ) = TransacaoRequest(BigDecimal(valor), tipo, data, descricao, categoriaId)

    private fun preparar() {
        every { categoriaService.buscarPorId(1L) } returns despesaCat
        every { categoriaService.buscarPorId(2L) } returns receitaCat
        every { transacaoRepository.save(any()) } answers { firstArg() }
    }

    @Test
    fun `criar aceita data de hoje`() {
        preparar()

        val t = service.criar(request(data = hoje))

        assertEquals(hoje, t.data)
    }

    @Test
    fun `criar aceita data passada (ADR 001 permite gastos retroativos)`() {
        preparar()

        val t = service.criar(request(data = hoje.minusYears(1)))

        assertEquals(hoje.minusYears(1), t.data)
    }

    @Test
    fun `criar rejeita data futura`() {
        preparar()

        assertFailsWith<RegraDeNegocioException> { service.criar(request(data = hoje.plusDays(1))) }
        verify(exactly = 0) { transacaoRepository.save(any()) }
    }

    @Test
    fun `criar rejeita tipo diferente do tipo da categoria`() {
        preparar()

        val erro = assertFailsWith<RegraDeNegocioException> {
            service.criar(request(tipo = TipoTransacao.RECEITA, categoriaId = 1L))
        }

        assertEquals(true, erro.message!!.contains("Alimentação"))
        verify(exactly = 0) { transacaoRepository.save(any()) }
    }

    @Test
    fun `criar aceita receita em categoria de receita`() {
        preparar()

        val t = service.criar(request(tipo = TipoTransacao.RECEITA, categoriaId = 2L))

        assertEquals(receitaCat, t.categoria)
    }

    @Test
    fun `atualizar altera os campos da transacao existente`() {
        preparar()
        val existente = Transacao(BigDecimal("10.00"), TipoTransacao.DESPESA, hoje, "antiga", despesaCat)
        every { transacaoRepository.findById(5L) } returns Optional.of(existente)

        val t = service.atualizar(5L, request(valor = "99.90", descricao = "nova"))

        assertEquals(BigDecimal("99.90"), t.valor)
        assertEquals("nova", t.descricao)
    }

    @Test
    fun `atualizar rejeita tipo incompativel com a categoria`() {
        preparar()
        val existente = Transacao(BigDecimal("10.00"), TipoTransacao.DESPESA, hoje, null, despesaCat)
        every { transacaoRepository.findById(5L) } returns Optional.of(existente)

        assertFailsWith<RegraDeNegocioException> {
            service.atualizar(5L, request(tipo = TipoTransacao.RECEITA, categoriaId = 1L))
        }
        assertEquals(TipoTransacao.DESPESA, existente.tipo)
    }

    @Test
    fun `excluir transacao inexistente lanca nao encontrado`() {
        every { transacaoRepository.findById(7L) } returns Optional.empty()

        assertFailsWith<RecursoNaoEncontradoException> { service.excluir(7L) }
    }

}
