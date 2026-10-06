# Apresentação e Defesa da Rodada 2 — FinApp

Este documento unifica os textos base para preenchimento dos slides da apresentação e o roteiro de fala da equipe.

---

## 1. CONTEÚDO DOS SLIDES (04 a 10)

**Slide 04 & 05: Monitoramento (EVM) e Burndown**
*   **Métricas Acumuladas:** PV: 300h | EV: 304h | AC: 310h | SPI: 1,01 | CPI: 0,98.
*   **Situação (Burndown):** Sprint finalizada hoje (06/10) com cronograma levemente adiantado (SV acumulado de +4h). O desvio de custo (CV de -6h) foi contido dentro do limite operacional.
*   **Causa do Desvio:** ~8h adicionais alocadas no setup e testes de Docker/PostgreSQL (RDT-01).
*   **Ação Corretiva:** Ambiente 100% estabilizado para a Rodada 3.

**Slide 06: Mudança de Escopo (Impacto RDT-01)**
*   **Impacto no Prazo:** As 8 horas extras foram compensadas pelo paralelismo da equipe. O escopo foi concluído com sucesso dentro da iteração.
*   **Impacto Financeiro:** R$ 0 de variação de orçamento externo. 

**Slide 07: Cronograma e Orçamento Atualizado**
*   **Fechamento:** A Rodada 2 foi oficialmente encerrada na data de hoje (06/10) com a entrega total dos RF01 e RF02, além do adiantamento do RF03 (Relatórios).
*   **Orçamento Ajustado (EAC):** Estimativa ajustada para ~R$ 31.225,00.

**Slide 08: Análise de Riscos**
*   **Risco Concretizado:** Dificuldade de execução/configuração do ambiente Docker. Exposição: 16 (Crítico).
*   **Contenção Executada:** `docker-compose.yml` finalizado e homologado.

**Slide 09: Status da Demonstração v2 (06/10)**
*   **RF01 (Transações):** 100% Concluído.
*   **RF02 (Categorias):** 100% Concluído (com seeds padrão).
*   **RF03 (Relatórios):** Entrega parcial adiantada (infraestrutura de dados).

**Slide 10: Tabela de Participação**
*   **Totais da Equipe:** Planejado: 180h | Entregue: 184h | Custo: 190h.
*(Filipe, Sara, Emanuel, Arben, Enzo e Giovana cumpriram seus papéis conforme planejado no burndown).*

---

## 2. ROTEIRO DE APRESENTAÇÃO (15 MINUTOS)

*   **00:00 - 02:30 | Filipe (Gerente de Projeto):** Abertura. Relato da saúde do projeto via EVM, Burndown e Cronograma, destacando o fechamento bem-sucedido na data de hoje (06/10).
*   **02:30 - 05:00 | Sara (Product Owner):** Visão de negócio e escopo concluído. Justificativa da adoção do Docker (RDT-01).
*   **05:00 - 07:30 | Enzo (Dev Infra/Relatórios):** Explicação do Docker/PostgreSQL e Demonstração parcial do RF03.
*   **07:30 - 10:00 | Giovana (Dev Backend):** Demonstração da aplicação rodando (RF01 e RF02).
*   **10:00 - 12:30 | Arben (Configuração):** Estratégia de versionamento, fluxo estrito de PRs que protegeu a main até o fechamento da sprint.
*   **12:30 - 15:00 | Emanuel (Scrum Master):** Fechamento. Análise de riscos e próximos passos para a última rodada.

---

## 3. PERGUNTAS E RESPOSTAS DE DEFESA (BANCA)

**1. O prazo era curto, por que arriscar migrar para Docker/PostgreSQL agora?**
*   **Resposta:** Risco calculado. O esforço nos forneceu escalabilidade e padronizou os ambientes locais de todos os 6 integrantes, o que permitiu fecharmos a sprint hoje sem problemas de "na minha máquina não roda".

**2. O SV de vocês está positivo (+4h) mas o CV está negativo (-6h). Vão estourar na última rodada?**
*   **Resposta:** Não. O desvio não é recorrente, foi o custo de aprendizado do Docker. Com a arquitetura baseada, o CPI tende a voltar para 1.0 na Rodada 3.

**3. Vocês tinham várias features sendo feitas ao mesmo tempo. Como garantiram que a "main" não quebrasse a tempo da entrega de hoje?**
*   **Resposta:** A `main` foi bloqueada contra pushes diretos. PRs de backend só eram aprovados se rodassem com sucesso no contêiner preparado pela Infra, com revisão obrigatória.
