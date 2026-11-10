package com.finapp.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

/** Meta de economia (RF04). O progresso é calculado a partir das transações entre [dataInicio] e [prazo]. */
@Entity
@Table(name = "metas")
class Meta(
    @Column(nullable = false, length = 100)
    var nome: String,

    @Column(name = "valor_alvo", nullable = false, precision = 19, scale = 2)
    var valorAlvo: BigDecimal,

    @Column(name = "data_inicio", nullable = false)
    var dataInicio: LocalDate,

    @Column(nullable = false)
    var prazo: LocalDate,

    @Column(nullable = false)
    var cancelada: Boolean = false
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
