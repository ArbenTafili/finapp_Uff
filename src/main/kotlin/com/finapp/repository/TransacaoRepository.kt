package com.finapp.repository

import com.finapp.model.Transacao
import org.springframework.data.jpa.repository.JpaRepository

interface TransacaoRepository : JpaRepository<Transacao, Long> {
    fun existsByCategoriaId(categoriaId: Long): Boolean
}
