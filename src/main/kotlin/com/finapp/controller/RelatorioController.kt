package com.finapp.controller

import com.finapp.dto.RelatorioMensalResponse
import com.finapp.exception.ParametroInvalidoException
import com.finapp.service.RelatorioService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

@RestController
@RequestMapping("/api/relatorios")
class RelatorioController(private val relatorioService: RelatorioService) {

    /** RF03: relatório de um mês; a navegação usa os campos mesAnterior e proximoMes da resposta. */
    @GetMapping
    fun relatorioMensal(@RequestParam(required = false) mes: String?): RelatorioMensalResponse =
        relatorioService.gerarRelatorioMensal(paraMes(mes))

    private fun paraMes(mes: String?): YearMonth {
        if (mes.isNullOrBlank()) {
            throw ParametroInvalidoException("mes", "O parâmetro 'mes' é obrigatório, no formato AAAA-MM (ex.: 2026-10)")
        }
        if (!FORMATO_MES.matches(mes)) {
            throw ParametroInvalidoException("mes", "O parâmetro 'mes' deve estar no formato AAAA-MM (ex.: 2026-10)")
        }
        return YearMonth.parse(mes)
    }

    private companion object {
        val FORMATO_MES = Regex("""\d{4}-(0[1-9]|1[0-2])""")
    }
}
