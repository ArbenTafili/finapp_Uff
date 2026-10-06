package com.finapp.service

import com.finapp.dto.SaldoResponse
import com.finapp.dto.TransacaoRequest
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Clock
import java.time.LocalDate

@Service
@Transactional
class TransacaoService(
    private val transacaoRepository: TransacaoRepository,
    private val categoriaService: CategoriaService,
    private val clock: Clock
) {

    @Transactional(readOnly = true)
    fun listar(): List<Transacao> = transacaoRepository.findAll()

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): Transacao =
        transacaoRepository.findById(id).orElseThrow {
            RecursoNaoEncontradoException("Transação com id $id não encontrada")
        }

    @Transactional(readOnly = true)
    fun calcularSaldo(): SaldoResponse = CalculoFinanceiro.resumo(transacaoRepository.findAll())

    fun criar(request: TransacaoRequest): Transacao {
        val data = request.data!!
        validarData(data)
        val tipo = request.tipo!!
        val categoria = categoriaService.buscarPorId(request.categoriaId!!)
        validarCategoria(categoria, tipo)
        val transacao = Transacao(
            valor = normalizarValor(request.valor!!),
            tipo = tipo,
            data = data,
            descricao = normalizarDescricao(request.descricao),
            categoria = categoria
        )
        return transacaoRepository.save(transacao)
    }

    fun atualizar(id: Long, request: TransacaoRequest): Transacao {
        val data = request.data!!
        validarData(data)
        val transacao = buscarPorId(id)
        val tipo = request.tipo!!
        val categoria = categoriaService.buscarPorId(request.categoriaId!!)
        validarCategoria(categoria, tipo)
        transacao.valor = normalizarValor(request.valor!!)
        transacao.tipo = tipo
        transacao.data = data
        transacao.descricao = normalizarDescricao(request.descricao)
        transacao.categoria = categoria
        return transacaoRepository.save(transacao)
    }

    fun excluir(id: Long) {
        val transacao = buscarPorId(id)
        transacaoRepository.delete(transacao)
    }

    /** RF01 / UC01: impede valores negativos (via @DecimalMin no DTO) e datas futuras (ADR 001). */
    private fun validarData(data: LocalDate) {
        if (data.isAfter(LocalDate.now(clock))) {
            throw RegraDeNegocioException("A data da transação não pode ser posterior à data atual")
        }
    }

    /** PT12: o tipo da transação deve ser o mesmo da categoria escolhida (ADR 002). */
    private fun validarCategoria(categoria: Categoria, tipo: TipoTransacao) {
        if (categoria.tipo != tipo) {
            throw RegraDeNegocioException(
                "A categoria '${categoria.nome}' é de ${categoria.tipo} e não pode ser usada em uma transação de $tipo"
            )
        }
    }

    /** O DTO já garante no máximo 2 casas; aqui só padronizamos a escala (10.5 -> 10.50) sem arredondar. */
    private fun normalizarValor(valor: BigDecimal): BigDecimal = valor.setScale(2, RoundingMode.UNNECESSARY)

    private fun normalizarDescricao(descricao: String?): String? = descricao?.trim()?.ifBlank { null }
}
