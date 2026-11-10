package com.finapp.service

import com.finapp.exception.ParametroInvalidoException
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * RF05: exportação das transações em CSV.
 * Formato pensado para abrir direto no Excel/LibreOffice em pt-BR: separador ";", datas dd/MM/yyyy,
 * valores com vírgula decimal e sem separador de milhar, UTF-8 com BOM, fim de linha CRLF.
 */
@Service
@Transactional(readOnly = true)
class ExportacaoService(private val transacaoRepository: TransacaoRepository) {

    fun exportarCsv(de: LocalDate?, ate: LocalDate?): String {
        if (de != null && ate != null && de.isAfter(ate)) {
            throw ParametroInvalidoException("de", "A data inicial não pode ser posterior à data final")
        }
        val transacoes = transacaoRepository.buscarParaExportacao(de, ate)
        return gerarCsv(transacoes)
    }

    fun gerarCsv(transacoes: List<Transacao>): String {
        val sb = StringBuilder(BOM)
        sb.append(CABECALHO.joinToString(SEPARADOR)).append(FIM_DE_LINHA)
        transacoes.forEach { t ->
            sb.append(
                listOf(
                    t.data.format(FORMATO_DATA),
                    if (t.tipo == TipoTransacao.RECEITA) "Receita" else "Despesa",
                    campo(t.categoria.nome),
                    campo(t.descricao.orEmpty()),
                    t.valor.setScale(2).toPlainString().replace('.', ',')
                ).joinToString(SEPARADOR)
            ).append(FIM_DE_LINHA)
        }
        return sb.toString()
    }

    /** Escapa conforme RFC 4180 e neutraliza fórmulas (=, +, -, @) que planilhas executariam ao abrir o arquivo. */
    internal fun campo(texto: String): String {
        var valor = texto.replace("\r\n", " ").replace('\n', ' ').replace('\r', ' ')
        if (valor.isNotEmpty() && valor[0] in GATILHOS_DE_FORMULA) valor = "'$valor"
        val precisaAspas = valor.any { it == ';' || it == '"' }
        return if (precisaAspas) "\"" + valor.replace("\"", "\"\"") + "\"" else valor
    }

    companion object {
        const val BOM = "﻿"
        private const val SEPARADOR = ";"
        private const val FIM_DE_LINHA = "\r\n"
        private val CABECALHO = listOf("Data", "Tipo", "Categoria", "Descrição", "Valor")
        private val FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private val GATILHOS_DE_FORMULA = setOf('=', '+', '-', '@', '\t')
    }
}
