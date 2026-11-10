package com.finapp.dto

import com.finapp.model.StatusMeta
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class MetaRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    val nome: String?,

    @field:NotNull(message = "O valor alvo é obrigatório")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "O valor alvo deve ser maior que zero")
    @field:Digits(integer = 17, fraction = 2, message = "O valor alvo deve ter no máximo 2 casas decimais")
    val valorAlvo: BigDecimal?,

    @field:NotNull(message = "O prazo é obrigatório")
    val prazo: LocalDate?
)

data class MetaResponse(
    val id: Long,
    val nome: String,
    val valorAlvo: BigDecimal,
    val dataInicio: LocalDate,
    val prazo: LocalDate,
    /** Saldo (receitas − despesas) acumulado entre dataInicio e o prazo, nunca negativo. */
    val valorEconomizado: BigDecimal,
    val valorRestante: BigDecimal,
    /** Progresso de 0 a 100, truncado a 2 casas. */
    val percentual: BigDecimal,
    val status: StatusMeta,
    /** Dias até o prazo; 0 quando o prazo já passou. */
    val diasRestantes: Long
)
