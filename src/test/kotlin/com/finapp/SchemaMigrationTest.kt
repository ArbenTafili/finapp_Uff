package com.finapp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

/** Confere, em um PostgreSQL real, o schema criado pelas migrações do Flyway. */
@SpringBootTest
@ActiveProfiles("test")
class SchemaMigrationTest(@Autowired private val jdbc: JdbcTemplate) {

    @Test
    fun `valor da transacao deve ser NUMERIC(19,2)`() {
        val coluna = jdbc.queryForMap(
            """
            select data_type, numeric_precision, numeric_scale
            from information_schema.columns
            where table_name = 'transacoes' and column_name = 'valor'
            """.trimIndent()
        )

        assertEquals("numeric", coluna["data_type"])
        assertEquals(19, (coluna["numeric_precision"] as Number).toInt())
        assertEquals(2, (coluna["numeric_scale"] as Number).toInt())
    }

    @Test
    fun `deve criar os indices de data e categoria`() {
        val indices = jdbc.queryForList(
            "select indexname from pg_indexes where tablename = 'transacoes'",
            String::class.java
        )

        assertTrue(indices.containsAll(listOf("idx_transacoes_data", "idx_transacoes_categoria_data"))) {
            "Índices encontrados: $indices"
        }
    }
}
