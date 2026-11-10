package com.finapp.controller

import com.finapp.dto.MetaRequest
import com.finapp.dto.MetaResponse
import com.finapp.service.MetaService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/metas")
class MetaController(private val metaService: MetaService) {

    @GetMapping
    fun listar(): List<MetaResponse> = metaService.listar()

    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Long): MetaResponse = metaService.buscar(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody request: MetaRequest): MetaResponse = metaService.criar(request)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: MetaRequest): MetaResponse =
        metaService.atualizar(id, request)

    @PatchMapping("/{id}/cancelar")
    fun cancelar(@PathVariable id: Long): MetaResponse = metaService.cancelar(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun excluir(@PathVariable id: Long) {
        metaService.excluir(id)
    }
}
