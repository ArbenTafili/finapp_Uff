package com.finapp.repository

import com.finapp.model.Meta
import org.springframework.data.jpa.repository.JpaRepository

interface MetaRepository : JpaRepository<Meta, Long> {
    fun findAllByOrderByPrazoAscIdAsc(): List<Meta>
}
