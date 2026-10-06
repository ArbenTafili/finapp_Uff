package com.finapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

/** Relógio injetável: permite testar a regra "data ≤ hoje" (ADR 001) sem depender do dia em que o teste roda. */
@Configuration
class ClockConfig {
    @Bean
    fun clock(): Clock = Clock.systemDefaultZone()
}
