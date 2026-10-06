package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.repository.CategoriaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Popula as categorias padrão (RF02) na inicialização.
 * Idempotente: cada categoria é criada só se ainda não existir (nome + tipo), então reiniciar a API
 * não duplica dados e categorias padrão novas entram em bancos já populados.
 */
@Component
class DataSeeder(private val categoriaRepository: CategoriaRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {
        CATEGORIAS_PADRAO.forEach { (nome, tipo) ->
            if (!categoriaRepository.existsByNomeIgnoreCaseAndTipo(nome, tipo)) {
                categoriaRepository.save(Categoria(nome = nome, tipo = tipo, ehPadrao = true))
            }
        }
    }

    companion object {
        val CATEGORIAS_PADRAO: List<Pair<String, TipoTransacao>> = listOf(
            "Salário" to TipoTransacao.RECEITA,
            "Outras Receitas" to TipoTransacao.RECEITA,
            "Alimentação" to TipoTransacao.DESPESA,
            "Moradia" to TipoTransacao.DESPESA,
            "Transporte" to TipoTransacao.DESPESA,
            "Lazer" to TipoTransacao.DESPESA,
            "Saúde" to TipoTransacao.DESPESA,
            "Outras Despesas" to TipoTransacao.DESPESA
        )
    }
}
