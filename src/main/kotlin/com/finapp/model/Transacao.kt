package com.finapp.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "transacoes")
class Transacao(
    @Column(nullable = false, precision = 19, scale = 2)
    var valor: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoTransacao,

    @Column(nullable = false)
    var data: LocalDate,

    @Column(length = 255)
    var descricao: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    var categoria: Categoria
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /** Valor com sinal aplicado (positivo para receita, negativo para despesa). */
    fun impactoNoSaldo(): BigDecimal =
        if (tipo == TipoTransacao.RECEITA) valor else valor.negate()
}
