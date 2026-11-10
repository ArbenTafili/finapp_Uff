package com.finapp.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** Verificação de saúde para a hospedagem (liberada de autenticação). */
@RestController
@RequestMapping("/api/saude")
class SaudeController {
    @GetMapping
    fun saude(): Map<String, String> = mapOf("status" to "ok")
}
