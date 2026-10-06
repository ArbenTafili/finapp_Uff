package com.finapp.dto

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CategoriaRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    val nome: String?,

    @field:NotNull(message = "O tipo é obrigatório (RECEITA ou DESPESA)")
    val tipo: TipoTransacao?
)

data class CategoriaResponse(
    val id: Long,
    val nome: String,
    val tipo: TipoTransacao,
    val ehPadrao: Boolean
)

fun Categoria.toResponse() = CategoriaResponse(
    id = requireNotNull(id) { "Categoria sem id não pode ser convertida em resposta" },
    nome = nome,
    tipo = tipo,
    ehPadrao = ehPadrao
)
