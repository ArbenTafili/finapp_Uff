package com.finapp.config

import com.finapp.model.Categoria
import com.finapp.model.Meta
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.MetaRepository
import com.finapp.repository.TransacaoRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate

/**
 * Carrega dados de exemplo para a conta de teste da versão publicada: 3 meses fechados + mês corrente
 * (datas relativas a hoje, para o relatório e as metas nunca parecerem vazios) e três metas (uma já concluída, para mostrar os estados).
 * Ativado por FINAPP_SEED_DEMO=true; idempotente (só insere se não houver transações nem metas).
 */
@Component
@ConditionalOnProperty(prefix = "finapp.seed", name = ["demo"], havingValue = "true")
class ContaTesteSeeder(
    private val categoriaRepository: CategoriaRepository,
    private val transacaoRepository: TransacaoRepository,
    private val metaRepository: MetaRepository,
    private val clock: Clock
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private data class Modelo(val dia: Int, val categoria: String, val valor: String, val descricao: String)

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun popular() {
        if (transacaoRepository.count() > 0 || metaRepository.count() > 0) {
            log.info("Seed da conta de teste: já há dados, nada foi inserido")
            return
        }
        val categorias = categoriaRepository.findAll().associateBy { it.nome to it.tipo }.toMutableMap()
        categorias.getOrPut("Educação" to TipoTransacao.DESPESA) {
            categoriaRepository.save(Categoria("Educação", TipoTransacao.DESPESA, false))
        }
        val hoje = LocalDate.now(clock)

        val transacoes = mutableListOf<Transacao>()
        for (atras in 3 downTo 0) {
            val mes = hoje.withDayOfMonth(1).minusMonths(atras.toLong())
            val ultimoDia = if (atras == 0) hoje.dayOfMonth else mes.lengthOfMonth()
            adicionar(transacoes, categorias, mes, ultimoDia, TipoTransacao.RECEITA, RECEITAS)
            adicionar(transacoes, categorias, mes, ultimoDia, TipoTransacao.DESPESA, DESPESAS)
        }
        transacaoRepository.saveAll(transacoes)

        val inicio = hoje.withDayOfMonth(1).minusMonths(3)
        metaRepository.saveAll(
            listOf(
                Meta("Reserva de emergência", BigDecimal("25000.00"), inicio, hoje.plusMonths(12)),
                Meta("Viagem de fim de ano", BigDecimal("12000.00"), inicio, hoje.plusMonths(2)),
                Meta("Fone de ouvido novo", BigDecimal("1500.00"), inicio, hoje.plusMonths(1))
            )
        )
        log.info("Seed da conta de teste: {} transações e 3 metas inseridas", transacoes.size)
    }

    private fun adicionar(
        destino: MutableList<Transacao>,
        categorias: Map<Pair<String, TipoTransacao>, Categoria>,
        mes: LocalDate,
        ultimoDia: Int,
        tipo: TipoTransacao,
        modelos: List<Modelo>
    ) {
        modelos.filter { it.dia <= ultimoDia }.forEach {
            destino += Transacao(
                valor = BigDecimal(it.valor),
                tipo = tipo,
                data = mes.withDayOfMonth(it.dia),
                descricao = it.descricao,
                categoria = categorias.getValue(it.categoria to tipo)
            )
        }
    }

    private companion object {
        val RECEITAS = listOf(
            Modelo(5, "Salário", "4500.00", "Salário"),
            Modelo(14, "Outras Receitas", "350.00", "Freelance")
        )
        val DESPESAS = listOf(
            Modelo(2, "Moradia", "1350.00", "Aluguel"),
            Modelo(3, "Moradia", "182.40", "Conta de luz"),
            Modelo(6, "Alimentação", "312.75", "Mercado"),
            Modelo(13, "Alimentação", "87.30", "Feira e padaria"),
            Modelo(9, "Transporte", "150.00", "Passagens do mês"),
            Modelo(11, "Lazer", "139.00", "Cinema e jantar"),
            Modelo(15, "Saúde", "78.35", "Farmácia"),
            Modelo(16, "Educação", "199.90", "Curso online")
        )
    }
}
