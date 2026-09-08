package com.finapp.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "categorias")
class Categoria(
    @Column(nullable = false)
    var nome: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoTransacao,

    @Column(name = "eh_padrao", nullable = false)
    var ehPadrao: Boolean = false
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
