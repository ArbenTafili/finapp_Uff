package com.finapp.model

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

class TransacaoTest {

    private fun transacao(valor: String, tipo: TipoTransacao) = Transacao(
        valor = BigDecimal(valor),
        tipo = tipo,
        data = LocalDate.of(2026, 9, 1),
        categoria = Categoria(nome = "Teste", tipo = tipo)
    )

    @Test
    fun `receita impacta o saldo com sinal positivo`() {
        assertEquals(BigDecimal("150.50"), transacao("150.50", TipoTransacao.RECEITA).impactoNoSaldo())
    }

    @Test
    fun `despesa impacta o saldo com sinal negativo`() {
        assertEquals(BigDecimal("-150.50"), transacao("150.50", TipoTransacao.DESPESA).impactoNoSaldo())
    }
}
