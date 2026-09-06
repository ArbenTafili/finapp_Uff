# Registro de Decisões Técnicas e de Processo (ADRs)

Este documento registra o histórico de decisões arquiteturais, técnicas e processuais tomadas ao longo das iterações do projeto **FinApp**. Os registros acumulados aqui compõem a base para a entrega formal do **Item 10 ("Dificuldades, decisões técnicas e de processo tomadas")** na Rodada 3.

---

## ADR 001: Validação de Data em Transações (RF01 / UC01)

* **Data:** 01/09/2026
* **Status:** Aprovado
* **Responsáveis:** Giovana Nogueira e Enzo Pavanelli
* **Requisitos Afetados:** RF01 (Gerenciar Transações), RNF01 (Usabilidade), UC01

### 1. Contexto e Problema
A especificação do Caso de Uso UC01 (Gerenciar Transações) determina que a regra de negócio do sistema deve impedir o registro de "datas anteriores ao cadastro". Durante o início da implementação, a equipe identificou uma ambiguidade arquitetural:
* O escopo formal delimitado para esta fase (RF01 a RF05) não contempla uma entidade `Usuario` com fluxo de autenticação e persistência de `dataCadastro`.
* Interpretar a restrição de forma literal tornaria a implementação inviável sem provocar *scope creep* (expansão não planejada de escopo).

### 2. Opções Avaliadas
1. **Impedir apenas datas futuras:** A transação não pode ter data posterior ao dia corrente (`data <= hoje`).
2. **Impedir datas anteriores à criação do registro:** Bloquear qualquer transação com data anterior ao instante de persistência no banco (proibição de *backdating*).
3. **Criar entidade Usuario mínima:** Modelar e persistir uma entidade simplificada unicamente para registrar a `dataCadastro` e utilizá-la como barreira de validação.

### 3. Decisão Tomada
Optou-se pela **Opção 1 (Impedir apenas datas futuras)**.

### 4. Justificativa
* **Usabilidade e Tempo de Registro (RNF01):** O sistema exige fluxo ágil (menos de 30 segundos por registro). A Opção 2 inviabilizaria casos de uso reais das personas mapeadas (ex.: registrar no dia seguinte despesas realizadas na véspera).
* **Controle de Escopo e Orçamento:** A Opção 3 adicionaria esforço de modelagem, banco de dados e testes não computados no Planning Poker inicial, comprometendo a capacidade de entrega das iterações.
* **Priorização da Experiência do Usuário:** Priorizou-se a usabilidade prática do produto em detrimento da interpretação estrita e descontextualizada do texto preliminar de requisitos.

### 5. Consequências
* A validação no backend/frontend checa apenas se `input_date <= current_date()`.
* Redução de atrito no lançamento de despesas retroativas pelo usuário.
* Manutenção da integridade do escopo acordado para as rodadas de entrega.
