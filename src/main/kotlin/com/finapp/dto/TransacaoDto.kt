package com.finapp.dto

import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class TransacaoRequest(
    @field:NotNull(message = "O valor é obrigatório")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero")
    val valor: BigDecimal?,

    @field:NotNull(message = "O tipo é obrigatório (RECEITA ou DESPESA)")
    val tipo: TipoTransacao?,

    @field:NotNull(message = "A data é obrigatória")
    val data: LocalDate?,

    @field:Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    val descricao: String? = null,

    @field:NotNull(message = "A categoria é obrigatória")
    val categoriaId: Long?
)

data class TransacaoResponse(
    val id: Long,
    val valor: BigDecimal,
    val tipo: TipoTransacao,
    val data: LocalDate,
    val descricao: String?,
    val categoria: CategoriaResponse
)

fun Transacao.toResponse() = TransacaoResponse(
    id = requireNotNull(id) { "Transação sem id não pode ser convertida em resposta" },
    valor = valor,
    tipo = tipo,
    data = data,
    descricao = descricao,
    categoria = categoria.toResponse()
)
