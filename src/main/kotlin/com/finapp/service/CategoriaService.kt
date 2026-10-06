package com.finapp.service

import com.finapp.dto.CategoriaRequest
import com.finapp.exception.ConflitoException
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoriaService(
    private val categoriaRepository: CategoriaRepository,
    private val transacaoRepository: TransacaoRepository
) {

    @Transactional(readOnly = true)
    fun listar(): List<Categoria> = categoriaRepository.findAll()

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): Categoria =
        categoriaRepository.findById(id).orElseThrow {
            RecursoNaoEncontradoException("Categoria com id $id não encontrada")
        }

    fun criar(request: CategoriaRequest): Categoria {
        val nome = request.nome!!.trim()
        val tipo = request.tipo!!
        validarNomeUnico(nome, tipo, idIgnorado = null)
        return categoriaRepository.save(Categoria(nome = nome, tipo = tipo, ehPadrao = false))
    }

    /** Categorias padrão são imutáveis; o tipo de uma categoria com transações não pode mudar (consistência RF01 × RF02). */
    fun atualizar(id: Long, request: CategoriaRequest): Categoria {
        val categoria = buscarPorId(id)
        if (categoria.ehPadrao) {
            throw RegraDeNegocioException("Categorias padrão não podem ser editadas")
        }
        val nome = request.nome!!.trim()
        val tipo = request.tipo!!
        if (tipo != categoria.tipo && transacaoRepository.existsByCategoriaId(id)) {
            throw RegraDeNegocioException(
                "O tipo da categoria não pode ser alterado porque ela possui transações associadas"
            )
        }
        validarNomeUnico(nome, tipo, idIgnorado = id)
        categoria.nome = nome
        categoria.tipo = tipo
        return categoriaRepository.save(categoria)
    }

    /** Regra de exclusão (ADR 002): bloqueia categoria padrão e categoria com transações vinculadas. */
    fun excluir(id: Long) {
        val categoria = buscarPorId(id)
        if (categoria.ehPadrao) {
            throw RegraDeNegocioException("Categorias padrão não podem ser excluídas")
        }
        val vinculadas = transacaoRepository.countByCategoriaId(id)
        if (vinculadas > 0) {
            throw RegraDeNegocioException(
                "A categoria possui $vinculadas transação(ões) associada(s) e não pode ser excluída. " +
                    "Exclua ou mude a categoria dessas transações primeiro"
            )
        }
        categoriaRepository.delete(categoria)
    }

    private fun validarNomeUnico(nome: String, tipo: TipoTransacao, idIgnorado: Long?) {
        val duplicada = if (idIgnorado == null) {
            categoriaRepository.existsByNomeIgnoreCaseAndTipo(nome, tipo)
        } else {
            categoriaRepository.existsByNomeIgnoreCaseAndTipoAndIdNot(nome, tipo, idIgnorado)
        }
        if (duplicada) {
            throw ConflitoException("Já existe uma categoria de $tipo chamada '$nome'")
        }
    }
}
