package com.finapp.repository

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import org.springframework.data.jpa.repository.JpaRepository

interface CategoriaRepository : JpaRepository<Categoria, Long> {
    fun existsByNomeIgnoreCaseAndTipo(nome: String, tipo: TipoTransacao): Boolean

    fun existsByNomeIgnoreCaseAndTipoAndIdNot(nome: String, tipo: TipoTransacao, id: Long): Boolean
}
