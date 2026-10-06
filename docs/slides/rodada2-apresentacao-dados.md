# Apresentação e Defesa da Rodada 2 — FinApp

Este documento unifica os textos base para preenchimento dos slides da apresentação e o roteiro de fala da equipe.

---

## 1. CONTEÚDO DOS SLIDES (04 a 10)

**Slide 04 & 05: Monitoramento (EVM) e Burndown**
*   **Métricas Acumuladas:** PV: 300h | EV: 304h | AC: 310h | SPI: 1,01 | CPI: 0,98.
*   **Situação (Burndown):** Cronograma levemente adiantado (SV acumulado de +4h), com um leve desvio de custo (CV de -6h) contido dentro do limite operacional. Entregas no prazo.
*   **Causa do Desvio:** ~8h adicionais alocadas em configuração, testes de Docker/PostgreSQL (RDT-01) e auditoria técnica das branches.
*   **Ação Corretiva:** Ambiente dockerizado estabilizado e documentado, mitigando repetição do problema e garantindo retomada do CPI ideal para a Iteração 3.

**Slide 06: Mudança de Escopo (Impacto RDT-01)**
*   **Impacto no Prazo:** Acréscimo de 8 horas de esforço técnico na iteração, reabsorvidas internamente graças à otimização das tarefas da Demo v2 e adiantamento do RF03.
*   **Impacto Financeiro:** R$ 0 de variação de orçamento externo. O desvio foi absorvido pela capacidade ociosa do time.

**Slide 07: Cronograma e Orçamento Atualizado**
*   **Ajuste no Gantt:** O cronograma reflete a finalização do escopo de RF01, RF02 e a entrega parcial adiantada do RF03 (Relatórios), abrindo margem para a última rodada.
*   **Orçamento Ajustado (EAC):** De R$ 30.600,00 projetado levemente para ~R$ 31.225,00 devido à eficiência atual.

**Slide 08: Análise de Riscos**
*   **Novo Risco Mapeado:** Dificuldade de execução/configuração do ambiente Docker nas máquinas locais e na máquina do avaliador.
*   **Exposição:** Probabilidade (4) × Impacto (4) = **16 (Crítico)**.
*   **Contenção:** `docker-compose.yml` finalizado e `README.md` detalhado com passo a passo de setup.
*   **Contingência:** Manter perfil "fallback" para executar banco em memória (H2) via Spring Boot caso o contêiner falhe.

**Slide 09: Status da Demonstração v2**
*   **RF01 (Transações):** Concluído.
*   **RF02 (Categorias):** Concluído com seeds/padrões.
*   **RF03 (Relatórios):** Entrega parcial adiantada (infraestrutura e modelagem pronta).

**Slide 10: Tabela de Participação**
*   Filipe (GP): PV 28h / Real 28h / Agregado 28h
*   Sara (PO): PV 26h / Real 26h / Agregado 26h
*   Emanuel (SM): PV 26h / Real 24h / Agregado 24h
*   Arben (Config): PV 30h / Real 34h / Agregado 32h
*   Enzo (Dev Infra/DB): PV 34h / Real 42h / Agregado 38h
*   Giovana (Dev Backend): PV 36h / Real 36h / Agregado 36h
*   **Totais:** Planejado: 180h | Entregue: 184h | Custo: 190h

---

## 2. ROTEIRO DE APRESENTAÇÃO (15 MINUTOS)

*   **00:00 - 02:30 | Filipe (Gerente de Projeto):** Abertura. Relato da saúde do projeto via EVM, Burndown e Cronograma (Slides 4, 5 e 7). Demonstra transparência sobre o leve desvio de custo (CPI 0.98).
*   **02:30 - 05:00 | Sara (Product Owner):** Visão de negócio. Retomada do escopo (Slides 2, 3 e 6). Justificativa técnica e de produto da mudança arquitetural (RDT-01) visando o mercado.
*   **05:00 - 07:30 | Enzo (Dev Infra/Relatórios):** Explicação da transição para Docker/PostgreSQL. Como resolveu o gargalo e Demonstração parcial do RF03 (Queries).
*   **07:30 - 10:00 | Giovana (Dev Backend):** Demonstração da aplicação (RF01 e RF02). Tratamento de exceções, sementes de banco (seeds) e integração com a estrutura criada pelo Enzo.
*   **10:00 - 12:30 | Arben (Configuração):** Estratégia de versionamento, fluxo estrito de PRs para proteger a main, padronização de branches via Issues (Slide 10).
*   **12:30 - 15:00 | Emanuel (Scrum Master):** Fechamento. Análise do novo risco (Docker - Slide 8) com suas respostas táticas, próximos passos para a Rodada 3 e abertura para perguntas.

---

## 3. PERGUNTAS E RESPOSTAS DE DEFESA (BANCA/PROFESSORA REBECA)

**Pergunta 1: Sobre a mudança RDT-01. O prazo é curto, por que arriscar migrar para Docker/PostgreSQL agora e absorver esse desvio no orçamento (CPI 0.98), em vez de usar H2/SQLite?**
*   **Resposta (Sara/Enzo):** Foi um risco calculado ('CAPEX'). O esforço inicial nos fornece um ganho gigante de escalabilidade e padroniza os ambientes da equipe, eliminando falhas de setup local na reta final. As funcionalidades nativas do PostgreSQL vão acelerar as agregações do RF03.

**Pergunta 2: O SV de vocês está positivo (+4h) mas o CV está negativo (-6h). Com a estimativa no término (EAC) subindo para R$ 31.225, como garantem que não vão estourar de vez na última rodada?**
*   **Resposta (Filipe/Emanuel):** O desvio não é recorrente. Foi inteiramente causado pela curva de aprendizado da infraestrutura (Docker). Agora que a arquitetura está baseada, a produtividade nos próximos requisitos de regra de negócio tenderá a aumentar, puxando o CPI de volta para o patamar de 1.0 e re-estabilizando o orçamento final.

**Pergunta 3: Vocês tinham várias features sendo feitas ao mesmo tempo (infra e backend). Como a gerência de configuração garantiu que a "main" não quebrasse a um dia da Demo v2?**
*   **Resposta (Arben/Giovana):** A `main` foi bloqueada contra pushes diretos. Adotamos *Feature Branches* associadas rigorosamente ao painel de Issues. O PR de backend só era aprovado se rodasse com sucesso no contêiner preparado pela equipe de Infraestrutura, passando por validação em pares obrigatória antes do *merge*.
