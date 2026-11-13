# Registro de Participação Individual — Consolidado (Rodadas 1 a 3)

**Responsável:** Filipe (Gerente de Projeto), com consolidação de toda a equipe  
**Projeto:** FinApp — GPMS / UFF

---

## 1. Esforço por Iteração e Horas Finais (Previsto vs. Realizado)

| Integrante | Papel no Projeto | I1 (Prev. / Real.) | I2 (Prev. / Real.) | I3 (Prev. / Real.) | Total Previsto | Total Real | Desvio (h) |
|---|---|:---:|:---:|:---:|:---:|:---:|:---:|
| **Filipe** | Gerente de Projeto (GP) & Dev | 22 h / 21 h | 28 h / 28 h | 38 h / 38 h | 88 h | **87 h** | -1 h |
| **Sara** | Product Owner (PO) | 20 h / 20 h | 26 h / 26 h | 34 h / 33 h | 80 h | **79 h** | -1 h |
| **Emanuel** | Scrum Master | 16 h / 16 h | 26 h / 24 h | 30 h / 30 h | 72 h | **70 h** | -2 h |
| **Arben** | Gerente de Configuração | 18 h / 17 h | 30 h / 34 h | 42 h / 40 h | 90 h | **91 h** | +1 h |
| **Enzo** | Desenvolvedor (Infra/DB/Fullstack) | 24 h / 26 h | 34 h / 42 h | 82 h / 60 h | 140 h | **128 h** | -12 h |
| **Giovana** | Desenvolvedora (Backend/Frontend) | 20 h / 20 h | 36 h / 36 h | 86 h / 104 h | 142 h | **160 h** | +18 h |
| **Equipe** | **Total Consolidado** | **120 h / 120 h** | **180 h / 190 h** | **312 h / 305 h** | **612 h** | **615 h** | **+3 h (+0,49%)** |

*Notas sobre o esforço:*
*   *I1 (Baseline 120h):* As horas individuais diretas de planejamento e baseline inicial somaram 50h de atividades estruturais, e as 70h restantes corresponderam a setup de ambiente, estudo conjunto do framework Spring Boot/Kotlin e alinhamento de cerimônias, distribuídas por integrante (12h para Filipe, Sara, Emanuel, Arben e Enzo; 10h para Giovana) e já incluídas na coluna I1.
*   *I2 (Baseline 180h / Real 190h):* Variação de +10h decorrente da migração para Docker e PostgreSQL (RDT-01) absorvida por Enzo (+8h) e Arben (+4h).
*   *I3 (Baseline 312h / Real 305h):* Recuperação de produtividade. Alta colaboração técnica entre Giovana e Enzo para fechar os requisitos funcionais pendentes com 100% de cobertura de testes. Inclui o fechamento de 13/11: testes ampliados (#61), correção da EAP (#59), tela de login (#62), consolidação de GP (#63 a #65), tags de versão e histórico de versões. O detalhamento por entrega está na seção 3.

---

## 2. Entregas e Histórico de Contribuições (Conforme Git / GitHub)

| Integrante | Papel | Commits na `main` / Branches | Principais Entregas e Artefatos Produzidos |
|---|---|:---:|---|
| **Enzo** | Infra/DB/Fullstack | 23 | Arquitetura Docker/PostgreSQL (RDT-01), Flyway migrations, queries agregadas do RF03, modelagem e lógica do RF04 (Metas), deploy no Render com conta de teste e scripts de automação. |
| **Filipe** | Gerente de Projeto | 17 | Baseline de escopo/cronograma/orçamento, cálculo contínuo de EVM (PV, EV, AC, CPI, SPI), gráficos de Burndown consolidados, diagramação e geração do PDF consolidado master. |
| **Giovana** | Backend/Frontend | 14 | Implementação do RF01 e RF02, interface gráfica e gestão de categorias, RF05 (Exportação CSV sanitizado), testes unitários de domínio e testes de integração com banco (109 testes verdes). |
| **Arben** | Configuração | 9 | Estrutura inicial do repositório, branching model (GitHub Flow / develop / release), auditoria de repositório, templates de issues e PRs, resolução de conflitos de merge e releases. |
| **Sara** | Product Owner | 8 | Documento de Visão, backlog priorizado das 3 rodadas, EAP hierárquica e refinamento contínuo de User Stories, validação dos critérios de aceite e roteiro da Demo v3. |
| **Emanuel** | Scrum Master | 5 | Facilitação das cerimônias ágeis (Dailies, Plannings, Reviews e Retrospectivas), matriz de riscos e planos de mitigação, ata de cerimônias e gravação da retrospectiva final. |

---

## 3. Detalhamento de Atividades na Rodada 3

| Pacote EAP | Entrega Técnica / Gestão | Branch de Trabalho (PR) | Responsável (horas) | Horas Reais |
|---|---|---|:---:|:---:|
| **3.3.4–3.3.7** | Interface Web de Relatórios Mensais, Rosca e Navegação | `feature/rf03-interface-relatorio` (#58) | Giovana 28 h · Enzo 10 h | 38 h |
| **3.4.1–3.4.9** | Metas de Economia (Backend, Regras, Interface e Testes) | `feature/rf04-metas` (#58) | Enzo | 34 h |
| **3.5.1–3.5.5** | Exportação de Dados em CSV (Filtros, Encoding UTF-8 e Download) | `feature/rf05-exportacao-csv` (#58) | Giovana | 30 h |
| **3.2.3–3.2.5** | Gestão de Categorias na Interface Web | `feature/rf02-interface-categorias` (#58) | Giovana | 20 h |
| **4.9** | Infraestrutura Cloud Render, Perfil Prod e Conta de Teste | `infra/deploy-conta-teste` (#58) | Enzo 8 h · Arben 8 h | 16 h |
| **4.9** | Tela de Login com Sessão para a Conta de Teste | `feature/tela-login` (#62) | Giovana | 20 h |
| **4.1–4.3** | Testes do Relatório Mensal, do Perfil Demo e do Perfil H2 (17 testes novos; suíte com 130 testes) | `test/60-testes-relatorio-demo` (#61) | Enzo | 8 h |
| **1.1 / 1.6** | Gestão de Branches, Resolução de Conflitos e Release v3 | `release/rodada3` (#58) | Arben | 20 h |
| **1.1 / 1.6** | Tags de Versão (v0.1.0, v0.2.0, v1.0.0) e Histórico de Versões | `docs/historico-versoes-horas` (#66) | Arben | 12 h |
| **1.6 / docs** | EVM Consolidado, Curva S, Burndowns e Documento PDF Final | `docs/consolidacao-final-gp` (#63) | Filipe | 38 h |
| **1.2 / 1.6** | Correção da EAP (Árvore Única em Mermaid) | `feature/#24-correcao-eap` (#59) | Sara | 12 h |
| **1.2 / 5.1** | Homologação de Critérios de Aceite e Roteiro da Demonstração v3 | `docs/roteiro-demo-v3-login` (#65) | Sara | 21 h |
| **4.9 / docs** | Conferência do README e das Dificuldades e Decisões (Deploy e Login) | `docs/rodada3-conferencia` (#64) | Giovana | 6 h |
| **1.3** | Facilitação das Cerimônias da Iteração 3 (Planning, Dailies e Review) | — | Emanuel | 12 h |
| **1.3 / 5.2** | Facilitação da Retrospectiva Final, Gravação em Vídeo e Riscos | `main` | Emanuel | 18 h |
| | **Total da Iteração 3** | | Filipe 38 h · Sara 33 h · Emanuel 30 h · Arben 40 h · Enzo 60 h · Giovana 104 h | **305 h** |

---

## 4. Participação na Gravação da Reunião de Retrospectiva Final

Todos os 6 integrantes participaram ativamente da reunião gravada de retrospectiva (duração de ~10 minutos), cumprindo o requisito de entrega formal da disciplina:

- [x] **Filipe Moreira** (Gerente de Projeto) — Apresentou os números de EVM, Curva S, Burndown consolidado e orçamento final.
- [x] **Sara Marcomini** (Product Owner) — Apresentou o alinhamento de visão de produto, escopo concluído (RF01 a RF05) e critérios de aceite.
- [x] **Emanuel** (Scrum Master) — Facilitou a retrospectiva, conduziu a abertura, o quadro de melhorias e o encerramento.
- [x] **Arben Tafili** (Gerente de Configuração) — Relatou a governança de branches, auditoria do repositório e resolução de conflitos.
- [x] **Enzo** (Desenvolvedor Infra/DB) — Demonstrou as decisões técnicas do Docker/PostgreSQL, Flyway, cálculo de metas e deploy.
- [x] **Giovana** (Desenvolvedora Backend/Frontend) — Demonstrou a execução dos requisitos de ponta a ponta e a suíte de testes automatizados.
