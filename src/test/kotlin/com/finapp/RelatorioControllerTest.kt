package com.finapp

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.system.measureTimeMillis

/**
 * RF03 / RNF02: GET /api/relatorios?mes=AAAA-MM em PostgreSQL real (Testcontainers).
 * Usa categorias e meses próprios (ano 2019) e remove o que criou, para não depender do seed
 * nem interferir em outros testes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RelatorioControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcTemplate
) {

    private var salario = 0L
    private var alimentacao = 0L
    private var transporte = 0L
    private var educacao = 0L

    @BeforeEach
    fun setUp() {
        salario = criarCategoria("Salário (teste)", "RECEITA")
        alimentacao = criarCategoria("Alimentação (teste)", "DESPESA")
        transporte = criarCategoria("Transporte (teste)", "DESPESA")
        educacao = criarCategoria("Educação (teste)", "DESPESA")
    }

    @AfterEach
    fun tearDown() {
        val ids = arrayOf(salario, alimentacao, transporte, educacao)
        jdbc.update("delete from transacoes where categoria_id in (?, ?, ?, ?)", *ids)
        jdbc.update("delete from categorias where id in (?, ?, ?, ?)", *ids)
    }

    @Test
    fun `deve devolver totais, saldo, despesas por categoria e meses vizinhos`() {
        inserir("3000.00", "RECEITA", "2019-09-01", salario)
        inserir("200.00", "DESPESA", "2019-09-02", alimentacao)
        inserir("100.00", "DESPESA", "2019-09-03", transporte)
        inserir("300.00", "DESPESA", "2019-09-04", educacao)

        mockMvc.perform(get("/api/relatorios").param("mes", "2019-09"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.mes").value("2019-09"))
            .andExpect(jsonPath("$.mesAnterior").value("2019-08"))
            .andExpect(jsonPath("$.proximoMes").value("2019-10"))
            .andExpect(jsonPath("$.totalReceitas").value(3000.00))
            .andExpect(jsonPath("$.totalDespesas").value(600.00))
            .andExpect(jsonPath("$.saldo").value(2400.00))
            .andExpect(jsonPath("$.despesasPorCategoria.length()").value(3))
            .andExpect(jsonPath("$.despesasPorCategoria[0].categoria").value("Educação (teste)"))
            .andExpect(jsonPath("$.despesasPorCategoria[0].categoriaId").value(educacao.toInt()))
            .andExpect(jsonPath("$.despesasPorCategoria[0].valor").value(300.00))
            .andExpect(jsonPath("$.despesasPorCategoria[0].percentual").value(50.00))
            .andExpect(jsonPath("$.despesasPorCategoria[1].percentual").value(33.33))
            .andExpect(jsonPath("$.despesasPorCategoria[2].percentual").value(16.67))
    }

    @Test
    fun `mes sem transacoes deve devolver 200 com totais zerados`() {
        mockMvc.perform(get("/api/relatorios").param("mes", "2019-07"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalReceitas").value(0.00))
            .andExpect(jsonPath("$.totalDespesas").value(0.00))
            .andExpect(jsonPath("$.saldo").value(0.00))
            .andExpect(jsonPath("$.despesasPorCategoria").isEmpty)
    }

    @ParameterizedTest
    @ValueSource(strings = ["2019-13", "2019-00", "2019-2", "02-2019", "2019/02", "2019-02-01", "abc", " "])
    fun `deve rejeitar mes em formato invalido`(mes: String) {
        mockMvc.perform(get("/api/relatorios").param("mes", mes))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.erro").value("Parâmetro inválido"))
            .andExpect(jsonPath("$.detalhes.mes").exists())
    }

    @Test
    fun `deve rejeitar requisicao sem o parametro mes`() {
        mockMvc.perform(get("/api/relatorios"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.detalhes.mes").exists())
    }

    @Test
    fun `relatorio de um mes com 20 mil transacoes deve responder em menos de 3 segundos`() {
        // 20 mil despesas espalhadas por 90 dias (jan a mar de 2019), em três categorias
        jdbc.update(
            """
            insert into transacoes (valor, tipo, data, categoria_id)
            select 10.50, 'DESPESA', date '2019-01-01' + (n % 90),
                   case n % 3 when 0 then ? when 1 then ? else ? end
            from generate_series(1, 20000) as n
            """.trimIndent(),
            alimentacao, transporte, educacao
        )

        val duracaoMs = measureTimeMillis {
            mockMvc.perform(get("/api/relatorios").param("mes", "2019-02"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.despesasPorCategoria.length()").value(3))
        }

        assertTrue(duracaoMs < 3000) { "RNF02: o relatório levou $duracaoMs ms" }
    }

    private fun criarCategoria(nome: String, tipo: String): Long =
        jdbc.queryForObject(
            "insert into categorias (nome, tipo, eh_padrao) values (?, ?, false) returning id",
            Long::class.java,
            nome, tipo
        )!!

    private fun inserir(valor: String, tipo: String, data: String, categoriaId: Long) {
        jdbc.update(
            "insert into transacoes (valor, tipo, data, categoria_id) values (cast(? as numeric), ?, cast(? as date), ?)",
            valor, tipo, data, categoriaId
        )
    }
}
