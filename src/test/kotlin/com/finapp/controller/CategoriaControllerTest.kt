package com.finapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.finapp.model.TipoTransacao
import com.finapp.repository.CategoriaRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/** Integração dos endpoints de categoria (RF02, PT09–PT11). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoriaControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val categoriaRepository: CategoriaRepository
) {

    private val objectMapper = ObjectMapper()

    private fun corpo(nome: String, tipo: String = "DESPESA") = """{"nome": "$nome", "tipo": "$tipo"}"""

    private fun criar(nome: String, tipo: String = "DESPESA") =
        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON).content(corpo(nome, tipo)))

    private fun criarEObterId(nome: String, tipo: String = "DESPESA"): Long =
        objectMapper.readTree(criar(nome, tipo).andExpect(status().isCreated).andReturn().response.contentAsString)
            .get("id").asLong()

    private fun idPadrao() = requireNotNull(categoriaRepository.findAll().first { it.ehPadrao }.id)

    @Test
    fun `seed deve disponibilizar as categorias padrao de receita e despesa`() {
        mockMvc.perform(get("/api/categorias"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[?(@.nome == 'Salário' && @.ehPadrao == true && @.tipo == 'RECEITA')]").exists())
            .andExpect(jsonPath("$[?(@.nome == 'Alimentação' && @.ehPadrao == true && @.tipo == 'DESPESA')]").exists())
    }

    @Test
    fun `deve criar, editar e excluir categoria personalizada`() {
        val id = criarEObterId("Pets")

        mockMvc.perform(get("/api/categorias/$id"))
            .andExpect(jsonPath("$.ehPadrao").value(false))

        mockMvc.perform(put("/api/categorias/$id").contentType(MediaType.APPLICATION_JSON).content(corpo("Animais")))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.nome").value("Animais"))

        mockMvc.perform(delete("/api/categorias/$id")).andExpect(status().isNoContent)
        mockMvc.perform(get("/api/categorias/$id")).andExpect(status().isNotFound)
    }

    @Test
    fun `deve rejeitar nome em branco e tipo ausente`() {
        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON).content("""{"nome": "  "}"""))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.detalhes.nome").exists())
            .andExpect(jsonPath("$.detalhes.tipo").exists())
    }

    @Test
    fun `deve rejeitar nome duplicado ignorando maiusculas com 409`() {
        criarEObterId("Pets")

        criar("pets")
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.mensagem").value("Já existe uma categoria de DESPESA chamada 'pets'"))
    }

    @Test
    fun `deve permitir mesmo nome em tipos diferentes`() {
        criarEObterId("Extras", "DESPESA")
        criar("Extras", "RECEITA").andExpect(status().isCreated)
    }

    @Test
    fun `nao deve permitir excluir nem editar categoria padrao`() {
        val id = idPadrao()

        mockMvc.perform(delete("/api/categorias/$id"))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.mensagem").value("Categorias padrão não podem ser excluídas"))

        mockMvc.perform(put("/api/categorias/$id").contentType(MediaType.APPLICATION_JSON).content(corpo("Renomeada")))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `nao deve excluir categoria com transacoes vinculadas`() {
        val id = criarEObterId("Pets")
        mockMvc.perform(
            post("/api/transacoes").contentType(MediaType.APPLICATION_JSON)
                .content("""{"valor": 30.00, "tipo": "DESPESA", "data": "${LocalDate.now()}", "categoriaId": $id}""")
        ).andExpect(status().isCreated)

        mockMvc.perform(delete("/api/categorias/$id"))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("1 transação(ões)")))
    }

    @Test
    fun `nao deve mudar o tipo de categoria com transacoes vinculadas`() {
        val id = criarEObterId("Pets")
        mockMvc.perform(
            post("/api/transacoes").contentType(MediaType.APPLICATION_JSON)
                .content("""{"valor": 30.00, "tipo": "DESPESA", "data": "${LocalDate.now()}", "categoriaId": $id}""")
        ).andExpect(status().isCreated)

        mockMvc.perform(put("/api/categorias/$id").contentType(MediaType.APPLICATION_JSON).content(corpo("Pets", "RECEITA")))
            .andExpect(status().isUnprocessableEntity)
        kotlin.test.assertEquals(TipoTransacao.DESPESA, categoriaRepository.findById(id).get().tipo)
    }
}
