package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.repository.CategoriaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/** Popula categorias padrão (RF02) na primeira inicialização, para a API ficar utilizável de imediato. */
@Component
class DataSeeder(private val categoriaRepository: CategoriaRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (categoriaRepository.count() > 0) return

        listOf(
            Categoria(nome = "Salário", tipo = TipoTransacao.RECEITA, ehPadrao = true),
            Categoria(nome = "Outras Receitas", tipo = TipoTransacao.RECEITA, ehPadrao = true),
            Categoria(nome = "Alimentação", tipo = TipoTransacao.DESPESA, ehPadrao = true),
            Categoria(nome = "Moradia", tipo = TipoTransacao.DESPESA, ehPadrao = true),
            Categoria(nome = "Transporte", tipo = TipoTransacao.DESPESA, ehPadrao = true),
            Categoria(nome = "Lazer", tipo = TipoTransacao.DESPESA, ehPadrao = true),
            Categoria(nome = "Saúde", tipo = TipoTransacao.DESPESA, ehPadrao = true),
            Categoria(nome = "Outras Despesas", tipo = TipoTransacao.DESPESA, ehPadrao = true)
        ).forEach(categoriaRepository::save)
    }
}
