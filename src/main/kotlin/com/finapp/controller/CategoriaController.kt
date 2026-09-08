package com.finapp.controller

import com.finapp.dto.CategoriaRequest
import com.finapp.dto.CategoriaResponse
import com.finapp.dto.toResponse
import com.finapp.service.CategoriaService
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
@RequestMapping("/api/categorias")
class CategoriaController(private val categoriaService: CategoriaService) {

    @GetMapping
    fun listar(): List<CategoriaResponse> =
        categoriaService.listar().map { it.toResponse() }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): CategoriaResponse =
        categoriaService.buscarPorId(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody request: CategoriaRequest): CategoriaResponse =
        categoriaService.criar(request).toResponse()

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: CategoriaRequest): CategoriaResponse =
        categoriaService.atualizar(id, request).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun excluir(@PathVariable id: Long) {
        categoriaService.excluir(id)
    }
}
