package com.finapp.service

import com.finapp.dto.CategoriaRequest
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Categoria
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
        val categoria = Categoria(
            nome = request.nome!!.trim(),
            tipo = request.tipo!!,
            ehPadrao = false
        )
        return categoriaRepository.save(categoria)
    }

    fun atualizar(id: Long, request: CategoriaRequest): Categoria {
        val categoria = buscarPorId(id)
        categoria.nome = request.nome!!.trim()
        categoria.tipo = request.tipo!!
        return categoriaRepository.save(categoria)
    }

    fun excluir(id: Long) {
        val categoria = buscarPorId(id)
        if (categoria.ehPadrao) {
            throw RegraDeNegocioException("Categorias padrão não podem ser excluídas")
        }
        if (transacaoRepository.existsByCategoriaId(id)) {
            throw RegraDeNegocioException("A categoria possui transações associadas e não pode ser excluída")
        }
        categoriaRepository.delete(categoria)
    }
}
