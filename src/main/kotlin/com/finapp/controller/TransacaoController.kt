package com.finapp.controller

import com.finapp.dto.TransacaoRequest
import com.finapp.dto.TransacaoResponse
import com.finapp.dto.toResponse
import com.finapp.service.TransacaoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/transacoes")
class TransacaoController(private val transacaoService: TransacaoService) {

    @GetMapping
    fun listar(): List<TransacaoResponse> =
        transacaoService.listar().map { it.toResponse() }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): TransacaoResponse =
        transacaoService.buscarPorId(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody request: TransacaoRequest): TransacaoResponse =
        transacaoService.criar(request).toResponse()

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: TransacaoRequest): TransacaoResponse =
        transacaoService.atualizar(id, request).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun excluir(@PathVariable id: Long) {
        transacaoService.excluir(id)
    }
}
