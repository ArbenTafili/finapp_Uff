package com.finapp.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
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

    /** JSON malformado, enum fora do domínio (tipo) ou data em formato inválido. */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleCorpoIlegivel(ex: HttpMessageNotReadableException): ResponseEntity<ErroResposta> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErroResposta(
                status = HttpStatus.BAD_REQUEST.value(),
                erro = "Dados inválidos",
                mensagem = "Corpo da requisição inválido. Verifique o JSON, o tipo (RECEITA ou DESPESA) e a data (AAAA-MM-DD)"
            )
        )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTipoParametro(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErroResposta> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErroResposta(
                status = HttpStatus.BAD_REQUEST.value(),
                erro = "Dados inválidos",
                mensagem = "O parâmetro '${ex.name}' possui um valor inválido: ${ex.value}"
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
