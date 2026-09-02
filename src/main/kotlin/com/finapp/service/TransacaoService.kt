package com.finapp.service

import com.finapp.dto.TransacaoRequest
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Transacao
import com.finapp.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class TransacaoService(
    private val transacaoRepository: TransacaoRepository,
    private val categoriaService: CategoriaService
) {

    @Transactional(readOnly = true)
    fun listar(): List<Transacao> = transacaoRepository.findAll()

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): Transacao =
        transacaoRepository.findById(id).orElseThrow {
            RecursoNaoEncontradoException("Transação com id $id não encontrada")
        }

    fun criar(request: TransacaoRequest): Transacao {
        val data = request.data!!
        validarData(data)
        val categoria = categoriaService.buscarPorId(request.categoriaId!!)
        val transacao = Transacao(
            valor = request.valor!!,
            tipo = request.tipo!!,
            data = data,
            descricao = request.descricao?.trim(),
            categoria = categoria
        )
        return transacaoRepository.save(transacao)
    }

    fun atualizar(id: Long, request: TransacaoRequest): Transacao {
        val data = request.data!!
        validarData(data)
        val transacao = buscarPorId(id)
        val categoria = categoriaService.buscarPorId(request.categoriaId!!)
        transacao.valor = request.valor!!
        transacao.tipo = request.tipo!!
        transacao.data = data
        transacao.descricao = request.descricao?.trim()
        transacao.categoria = categoria
        return transacaoRepository.save(transacao)
    }

    fun excluir(id: Long) {
        val transacao = buscarPorId(id)
        transacaoRepository.delete(transacao)
    }

    /** RF01 / UC01: impede valores negativos (via @DecimalMin no DTO) e datas futuras. */
    private fun validarData(data: LocalDate) {
        if (data.isAfter(LocalDate.now())) {
            throw RegraDeNegocioException("A data da transação não pode ser posterior à data atual")
        }
    }
}
