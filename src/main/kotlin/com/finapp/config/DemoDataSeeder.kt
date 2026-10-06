package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Pré-carrega a massa base da Demo v2 (D1 a D4 de docs/rodada2/04-aceite-demo-v2.md), para o
 * ensaio ou para quando não houver tempo de cadastrar ao vivo. Só roda com o perfil "demo" e
 * apenas se ainda não houver nenhuma transação.
 */
@Component
@Profile("demo")
class DemoDataSeeder(
    private val categoriaRepository: CategoriaRepository,
    private val transacaoRepository: TransacaoRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private data class Exemplo(
        val data: String,
        val tipo: TipoTransacao,
        val categoria: String,
        val valor: String,
        val descricao: String
    )

    // Executa depois do DataSeeder, que cria as categorias padrão
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun popular() {
        if (transacaoRepository.count() > 0) {
            log.info("Perfil demo: já existem transações, a massa base não foi inserida")
            return
        }

        val categorias = categoriaRepository.findAll().associateBy { it.nome }.toMutableMap()
        val faltando = MASSA_BASE.map { it.categoria }.filter { it != CATEGORIA_PERSONALIZADA && it !in categorias }
        if (faltando.isNotEmpty()) {
            log.warn("Perfil demo: categorias padrão não encontradas ({}), a massa base não foi inserida", faltando)
            return
        }
        // No roteiro, esta categoria é criada como "Curso Demo" e renomeada para "Educação Demo"
        categorias.getOrPut(CATEGORIA_PERSONALIZADA) {
            categoriaRepository.save(Categoria(nome = CATEGORIA_PERSONALIZADA, tipo = TipoTransacao.DESPESA))
        }

        val transacoes = MASSA_BASE.map {
            Transacao(
                valor = BigDecimal(it.valor),
                tipo = it.tipo,
                data = LocalDate.parse(it.data),
                descricao = it.descricao,
                categoria = categorias.getValue(it.categoria)
            )
        }
        transacaoRepository.saveAll(transacoes)
        log.info("Perfil demo: {} transações da massa base inseridas em setembro de 2026", transacoes.size)
    }

    private companion object {
        const val CATEGORIA_PERSONALIZADA = "Educação Demo"

        val MASSA_BASE = listOf(
            Exemplo("2026-09-01", TipoTransacao.RECEITA, "Salário", "3000.00", "Salário Demo v2"),
            Exemplo("2026-09-02", TipoTransacao.DESPESA, "Alimentação", "200.00", "Mercado Demo v2"),
            Exemplo("2026-09-03", TipoTransacao.DESPESA, "Transporte", "100.00", "Transporte Demo v2"),
            Exemplo("2026-09-04", TipoTransacao.DESPESA, CATEGORIA_PERSONALIZADA, "300.00", "Curso Demo v2")
        )
    }
}
