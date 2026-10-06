package com.finapp.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

data class ErroResposta(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val erro: String,
    val mensagem: String,
    val detalhes: Map<String, String>? = null
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException::class)
    fun handleNaoEncontrado(ex: RecursoNaoEncontradoException): ResponseEntity<ErroResposta> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErroResposta(
                status = HttpStatus.NOT_FOUND.value(),
                erro = "Recurso não encontrado",
                mensagem = ex.message ?: ""
            )
        )

    @ExceptionHandler(RegraDeNegocioException::class)
    fun handleRegraNegocio(ex: RegraDeNegocioException): ResponseEntity<ErroResposta> =
        ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
            ErroResposta(
                status = HttpStatus.UNPROCESSABLE_ENTITY.value(),
                erro = "Regra de negócio violada",
                mensagem = ex.message ?: ""
            )
        )

    @ExceptionHandler(ParametroInvalidoException::class)
    fun handleParametroInvalido(ex: ParametroInvalidoException): ResponseEntity<ErroResposta> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErroResposta(
                status = HttpStatus.BAD_REQUEST.value(),
                erro = "Parâmetro inválido",
                mensagem = ex.message ?: "",
                detalhes = mapOf(ex.parametro to (ex.message ?: "inválido"))
            )
        )

    @ExceptionHandler(ConflitoException::class)
    fun handleConflito(ex: ConflitoException): ResponseEntity<ErroResposta> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErroResposta(
                status = HttpStatus.CONFLICT.value(),
                erro = "Conflito",
                mensagem = ex.message ?: ""
            )
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidacao(ex: MethodArgumentNotValidException): ResponseEntity<ErroResposta> {
        val detalhes = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "inválido") }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErroResposta(
                status = HttpStatus.BAD_REQUEST.value(),
                erro = "Dados inválidos",
                mensagem = "Um ou mais campos são inválidos",
                detalhes = detalhes
            )
        )
    }
}
