package com.finapp.controller

import com.finapp.service.ExportacaoService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets
import java.time.LocalDate

@RestController
@RequestMapping("/api/transacoes/exportar")
class ExportacaoController(private val exportacaoService: ExportacaoService) {

    /** RF05: baixa as transações do período [de, ate] (AAAA-MM-DD, ambos opcionais) em CSV. */
    @GetMapping
    fun exportar(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) de: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ate: LocalDate?
    ): ResponseEntity<ByteArray> {
        val csv = exportacaoService.exportarCsv(de, ate)
        val nome = "finapp-transacoes-${de ?: "inicio"}_${ate ?: "hoje"}.csv"
        return ResponseEntity.ok()
            .contentType(MediaType("text", "csv", StandardCharsets.UTF_8))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment().filename(nome).build().toString()
            )
            .body(csv.toByteArray(StandardCharsets.UTF_8))
    }
}
