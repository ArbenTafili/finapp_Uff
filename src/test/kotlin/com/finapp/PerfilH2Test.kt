package com.finapp

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals

/**
 * Plano B da demo (Issue #29): o perfil h2 sobe sem Docker, aplica todas as migrações do Flyway
 * no H2 e serve o relatório mensal. Protege contra migrações que só funcionem no PostgreSQL.
 */
@SpringBootTest(properties = ["spring.datasource.url=jdbc:h2:mem:finapp-perfil-h2;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1"])
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class PerfilH2Test(
    @Autowired private val jdbc: JdbcTemplate,
    @Autowired private val mockMvc: MockMvc
) {

    @Test
    fun `usa o banco H2`() {
        val produto = jdbc.dataSource!!.connection.use { it.metaData.databaseProductName }

        assertEquals("H2", produto)
    }

    @Test
    fun `aplica todas as migracoes do Flyway sem falhas`() {
        val pendentesOuFalhas = jdbc.queryForObject(
            "select count(*) from \"flyway_schema_history\" where \"success\" = false",
            Int::class.java
        )
        val versoes = jdbc.queryForList(
            "select \"version\" from \"flyway_schema_history\" where \"version\" is not null order by \"installed_rank\"",
            String::class.java
        )

        assertEquals(0, pendentesOuFalhas)
        assertEquals(listOf("1", "2", "3"), versoes.take(3))
    }

    @Test
    fun `cria as categorias padrao`() {
        val padrao = jdbc.queryForObject("select count(*) from categorias where eh_padrao = true", Int::class.java)

        assertEquals(8, padrao)
    }

    @Test
    fun `serve o relatorio mensal`() {
        mockMvc.perform(get("/api/relatorios").param("mes", "2026-07"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.saldo").value(0.00))
            .andExpect(jsonPath("$.despesasPorCategoria").isEmpty)
    }
}
