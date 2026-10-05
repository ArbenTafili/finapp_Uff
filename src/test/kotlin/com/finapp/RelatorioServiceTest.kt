package com.finapp

import com.finapp.model.Categoria
import com.finapp.model.TipoTransacao
import com.finapp.model.Transacao
import com.finapp.repository.CategoriaRepository
import com.finapp.repository.TransacaoRepository
import com.finapp.service.RelatorioService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

/**
 * RF03: consultas agregadas do relatório mensal, executadas em PostgreSQL real (Testcontainers).
 * Usa categorias e meses próprios (ano 2019) e remove o que criou, para não depender do seed
 * nem interferir em outros testes.
 */
@SpringBootTest
@ActiveProfiles("test")
class RelatorioServiceTest(
    @Autowired private val relatorioService: RelatorioService,
    @Autowired private val transacaoRepository: TransacaoRepository,
    @Autowired private val categoriaRepository: CategoriaRepository
) {

    private val setembro = YearMonth.of(2019, 9)
    private val transacoesCriadas = mutableListOf<Transacao>()
    private lateinit var categorias: Map<String, Categoria>

    @BeforeEach
    fun setUp() {
        categorias = listOf(
            Categoria(nome = "Salário (teste)", tipo = TipoTransacao.RECEITA),
            Categoria(nome = "Outras Receitas (teste)", tipo = TipoTransacao.RECEITA),
            Categoria(nome = "Alimentação (teste)", tipo = TipoTransacao.DESPESA),
            Categoria(nome = "Transporte (teste)", tipo = TipoTransacao.DESPESA),
            Categoria(nome = "Educação (teste)", tipo = TipoTransacao.DESPESA)
        ).map(categoriaRepository::save).associateBy { it.nome.removeSuffix(" (teste)") }
    }

    @AfterEach
    fun tearDown() {
        transacaoRepository.deleteAll(transacoesCriadas)
        categoriaRepository.deleteAll(categorias.values)
    }

    @Test
    fun `mes vazio deve devolver totais zerados e nenhuma despesa por categoria`() {
        val relatorio = relatorioService.gerarRelatorioMensal(YearMonth.of(2019, 7))

        assertEquals(BigDecimal("0.00"), relatorio.totalReceitas)
        assertEquals(BigDecimal("0.00"), relatorio.totalDespesas)
        assertEquals(BigDecimal("0.00"), relatorio.saldo)
        assertTrue(relatorio.despesasPorCategoria.isEmpty())
    }

    @Test
    fun `mes com receitas e despesas deve somar totais, saldo e distribuicao das despesas`() {
        massaBase()

        val relatorio = relatorioService.gerarRelatorioMensal(setembro)

        assertEquals("2019-09", relatorio.mes)
        assertEquals("2019-08", relatorio.mesAnterior)
        assertEquals("2019-10", relatorio.proximoMes)
        assertEquals(BigDecimal("3000.00"), relatorio.totalReceitas)
        assertEquals(BigDecimal("600.00"), relatorio.totalDespesas)
        assertEquals(BigDecimal("2400.00"), relatorio.saldo)

        val despesas = relatorio.despesasPorCategoria
        assertEquals(
            listOf("Educação (teste)", "Alimentação (teste)", "Transporte (teste)"),
            despesas.map { it.categoria }
        )
        assertEquals(listOf(BigDecimal("300.00"), BigDecimal("200.00"), BigDecimal("100.00")), despesas.map { it.valor })
        assertEquals(listOf(BigDecimal("50.00"), BigDecimal("33.33"), BigDecimal("16.67")), despesas.map { it.percentual })
    }

    @Test
    fun `receitas nao entram na distribuicao por categoria`() {
        receita("3000.00", "2019-09-01", "Salário")

        val relatorio = relatorioService.gerarRelatorioMensal(setembro)

        assertEquals(BigDecimal("3000.00"), relatorio.totalReceitas)
        assertTrue(relatorio.despesasPorCategoria.isEmpty())
    }

    @Test
    fun `transacao de outro mes nao altera os totais do mes consultado`() {
        massaBase()
        receita("100.00", "2019-08-15", "Outras Receitas")

        assertEquals(BigDecimal("3000.00"), relatorioService.gerarRelatorioMensal(setembro).totalReceitas)
        assertEquals(BigDecimal("100.00"), relatorioService.gerarRelatorioMensal(YearMonth.of(2019, 8)).totalReceitas)
    }

    @Test
    fun `deve incluir o primeiro e o ultimo dia do mes e excluir os dias vizinhos`() {
        despesa("10.00", "2019-08-31", "Transporte")
        despesa("20.00", "2019-09-01", "Transporte")
        despesa("30.00", "2019-09-30", "Transporte")
        despesa("40.00", "2019-10-01", "Transporte")

        val relatorio = relatorioService.gerarRelatorioMensal(setembro)

        assertEquals(BigDecimal("50.00"), relatorio.totalDespesas)
        assertEquals(BigDecimal("-50.00"), relatorio.saldo)
        assertEquals(BigDecimal("100.00"), relatorio.despesasPorCategoria.single().percentual)
    }

    @Test
    fun `saldo com centavos deve ser exato`() {
        receita("10.10", "2019-06-01", "Outras Receitas")
        receita("0.20", "2019-06-02", "Outras Receitas")
        despesa("0.10", "2019-06-03", "Alimentação")

        val relatorio = relatorioService.gerarRelatorioMensal(YearMonth.of(2019, 6))

        assertEquals(BigDecimal("10.30"), relatorio.totalReceitas)
        assertEquals(BigDecimal("0.10"), relatorio.totalDespesas)
        assertEquals(BigDecimal("10.20"), relatorio.saldo)
    }

    /** Mesma massa dos critérios de aceite da Demo v2 (D1 a D4), em outro ano. */
    private fun massaBase() {
        receita("3000.00", "2019-09-01", "Salário")
        despesa("200.00", "2019-09-02", "Alimentação")
        despesa("100.00", "2019-09-03", "Transporte")
        despesa("300.00", "2019-09-04", "Educação")
    }

    private fun receita(valor: String, data: String, categoria: String) =
        salvar(TipoTransacao.RECEITA, valor, data, categoria)

    private fun despesa(valor: String, data: String, categoria: String) =
        salvar(TipoTransacao.DESPESA, valor, data, categoria)

    private fun salvar(tipo: TipoTransacao, valor: String, data: String, categoria: String) {
        transacoesCriadas += transacaoRepository.save(
            Transacao(
                valor = BigDecimal(valor),
                tipo = tipo,
                data = LocalDate.parse(data),
                categoria = categorias.getValue(categoria)
            )
        )
    }
}
