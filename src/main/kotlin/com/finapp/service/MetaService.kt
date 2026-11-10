package com.finapp.service

import com.finapp.dto.MetaRequest
import com.finapp.dto.MetaResponse
import com.finapp.exception.RecursoNaoEncontradoException
import com.finapp.exception.RegraDeNegocioException
import com.finapp.model.Meta
import com.finapp.model.StatusMeta
import com.finapp.repository.MetaRepository
import com.finapp.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/** RF04: metas de economia com progresso calculado a partir das transações (3.4.1 a 3.4.9). */
@Service
@Transactional
class MetaService(
    private val metaRepository: MetaRepository,
    private val transacaoRepository: TransacaoRepository,
    private val clock: Clock
) {

    @Transactional(readOnly = true)
    fun listar(): List<MetaResponse> = metaRepository.findAllByOrderByPrazoAscIdAsc().map { paraResposta(it) }

    @Transactional(readOnly = true)
    fun buscar(id: Long): MetaResponse = paraResposta(buscarEntidade(id))

    fun criar(request: MetaRequest): MetaResponse {
        val hoje = LocalDate.now(clock)
        val prazo = request.prazo!!
        validarPrazo(prazo, hoje)
        val meta = Meta(
            nome = request.nome!!.trim(),
            valorAlvo = request.valorAlvo!!.setScale(2, RoundingMode.UNNECESSARY),
            dataInicio = hoje,
            prazo = prazo
        )
        return paraResposta(metaRepository.save(meta))
    }

    /** Edita nome, valor alvo e prazo. Metas canceladas ou concluídas não podem mais ser alteradas. */
    fun atualizar(id: Long, request: MetaRequest): MetaResponse {
        val meta = buscarEntidade(id)
        val hoje = LocalDate.now(clock)
        val statusAtual = paraResposta(meta).status
        if (statusAtual == StatusMeta.CANCELADA) throw RegraDeNegocioException("Metas canceladas não podem ser editadas")
        if (statusAtual == StatusMeta.CONCLUIDA) throw RegraDeNegocioException("Metas concluídas não podem ser editadas")
        validarPrazo(request.prazo!!, hoje)
        meta.nome = request.nome!!.trim()
        meta.valorAlvo = request.valorAlvo!!.setScale(2, RoundingMode.UNNECESSARY)
        meta.prazo = request.prazo
        return paraResposta(metaRepository.save(meta))
    }

    /** Cancela a meta, preservando o registro. Só metas em andamento ou vencidas podem ser canceladas. */
    fun cancelar(id: Long): MetaResponse {
        val meta = buscarEntidade(id)
        when (paraResposta(meta).status) {
            StatusMeta.CANCELADA -> throw RegraDeNegocioException("A meta já está cancelada")
            StatusMeta.CONCLUIDA -> throw RegraDeNegocioException("Metas concluídas não podem ser canceladas")
            else -> Unit
        }
        meta.cancelada = true
        return paraResposta(metaRepository.save(meta))
    }

    fun excluir(id: Long) {
        metaRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long): Meta =
        metaRepository.findById(id).orElseThrow { RecursoNaoEncontradoException("Meta com id $id não encontrada") }

    private fun validarPrazo(prazo: LocalDate, hoje: LocalDate) {
        if (prazo.isBefore(hoje)) {
            throw RegraDeNegocioException("O prazo da meta não pode ser anterior à data atual")
        }
    }

    private fun paraResposta(meta: Meta): MetaResponse {
        val hoje = LocalDate.now(clock)
        // Integração Transações x Metas: saldo acumulado entre o início e o prazo (limitado a hoje, pois não há datas futuras)
        val fim = minOf(meta.prazo, hoje)
        val saldo: BigDecimal =
            if (fim.isBefore(meta.dataInicio)) BigDecimal.ZERO else transacaoRepository.saldoNoPeriodo(meta.dataInicio, fim)
        val economizado = CalculoMeta.economizado(saldo)
        return MetaResponse(
            id = requireNotNull(meta.id) { "Meta sem id não pode ser convertida em resposta" },
            nome = meta.nome,
            valorAlvo = meta.valorAlvo,
            dataInicio = meta.dataInicio,
            prazo = meta.prazo,
            valorEconomizado = economizado,
            valorRestante = CalculoMeta.restante(economizado, meta.valorAlvo),
            percentual = CalculoMeta.percentual(economizado, meta.valorAlvo),
            status = CalculoMeta.status(meta.cancelada, economizado, meta.valorAlvo, meta.prazo, hoje),
            diasRestantes = maxOf(0L, ChronoUnit.DAYS.between(hoje, meta.prazo))
        )
    }
}
