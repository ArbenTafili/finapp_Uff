package com.finapp.service

import com.finapp.model.StatusMeta
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

/** 4.1: progresso e status das metas (RF04 / RNF05). */
class CalculoMetaTest {

    private val hoje = LocalDate.of(2026, 9, 15)
    private val alvo = BigDecimal("1000.00")

    @Test
    fun `economizado nunca fica negativo`() {
        assertEquals(BigDecimal("0.00"), CalculoMeta.economizado(BigDecimal("-250.00")))
        assertEquals(BigDecimal("300.00"), CalculoMeta.economizado(BigDecimal("300.00")))
    }

    @Test
    fun `percentual e truncado a duas casas e limitado a 100`() {
        assertEquals(BigDecimal("33.33"), CalculoMeta.percentual(BigDecimal("333.33"), alvo))
        assertEquals(BigDecimal("0.00"), CalculoMeta.percentual(BigDecimal("0.00"), alvo))
        assertEquals(BigDecimal("100.00"), CalculoMeta.percentual(BigDecimal("1500.00"), alvo))
    }

    @Test
    fun `percentual nao chega a 100 antes de atingir o alvo`() {
        assertEquals(BigDecimal("99.99"), CalculoMeta.percentual(BigDecimal("999.99"), alvo))
    }

    @Test
    fun `restante e o que falta para o alvo, sem ser negativo`() {
        assertEquals(BigDecimal("700.00"), CalculoMeta.restante(BigDecimal("300.00"), alvo))
        assertEquals(BigDecimal("0.00"), CalculoMeta.restante(BigDecimal("1200.00"), alvo))
    }

    @Test
    fun `status em andamento dentro do prazo`() {
        assertEquals(StatusMeta.EM_ANDAMENTO, CalculoMeta.status(false, BigDecimal("10.00"), alvo, hoje.plusDays(5), hoje))
        assertEquals(StatusMeta.EM_ANDAMENTO, CalculoMeta.status(false, BigDecimal("10.00"), alvo, hoje, hoje))
    }

    @Test
    fun `status concluida ao atingir o alvo, mesmo apos o prazo`() {
        assertEquals(StatusMeta.CONCLUIDA, CalculoMeta.status(false, alvo, alvo, hoje.plusDays(5), hoje))
        assertEquals(StatusMeta.CONCLUIDA, CalculoMeta.status(false, BigDecimal("1001.00"), alvo, hoje.minusDays(1), hoje))
    }

    @Test
    fun `status vencida quando o prazo passou sem atingir o alvo`() {
        assertEquals(StatusMeta.VENCIDA, CalculoMeta.status(false, BigDecimal("999.99"), alvo, hoje.minusDays(1), hoje))
    }

    @Test
    fun `status cancelada prevalece sobre os demais`() {
        assertEquals(StatusMeta.CANCELADA, CalculoMeta.status(true, alvo, alvo, hoje.minusDays(1), hoje))
    }
}
