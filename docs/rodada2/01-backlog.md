# Backlog e Priorização — Rodada 2 — FinApp

**Responsável:** Sara Marcomini (Product Owner)

**Situação:** revisão documental para aceite; integração e validação funcional discriminadas por entrega.

**Referência da conferência:** `main` em `c86694f`; `develop` em `6a5d558`.

## Objetivo e ponto de partida

Concluir RF01 (Transações) e RF02 (Categorias) e iniciar RF03 (Relatórios), preparando a Demo v2. A Demo v1, com CRUD de transações e categorias, é o ponto de partida informado pela equipe. Esse histórico não representa aceite automático das entregas da rodada 2: a conclusão depende de validação e evidências.

Esta revisão atende à [Issue #24 — Revisar backlog e escopo da Iteração 2](https://github.com/ArbenTafili/finapp_Uff/issues/24), atribuída a Sara. O escopo da Demo v2 inclui RF01/RF02 e RF03 parcial, com consultas e endpoint mensal; o roteiro e os critérios Dado/Quando/Então são a entrega da [Issue #25](https://github.com/ArbenTafili/finapp_Uff/issues/25).

## Backlog por rodada

P1 indica prioridade de conclusão na rodada 2; P2 indica a entrega parcial de relatórios nessa rodada. Os itens reservados para a rodada 3 terão sua ordem detalhada no planejamento daquela rodada.

| Requisito | Entrega prevista | Prioridade | Rodada | Responsável técnico | Situação |
|---|---|---|---|---|---|
| RF01 — Gerenciar Transações | Concluir CRUD; validar valor positivo com até duas casas decimais, categoria obrigatória e `data <= hoje`; conferir saldo e erros de entrada. | P1 | 2 | Giovana | Código integrado à main pelo PR #43 — aceite funcional pendente. |
| RF02 — Gerenciar Categorias | Concluir seed idempotente e CRUD de categorias personalizadas; bloquear edição/exclusão de padrão, exclusão de categoria vinculada e alteração incompatível de tipo; tratar nomes duplicados por tipo e integrar às transações. | P1 | 2 | Giovana | Código integrado à main pelo PR #43 — aceite funcional pendente. |
| RF03 — Relatórios (parcial) | Consultas e endpoint mensal com receitas, despesas, saldo e distribuição de despesas por categoria; validar resultados e medir RNF02. | P2 | 2 | Enzo | Código integrado à main pelos PRs #36/#37 — aceite funcional pendente. |
| RF01/RF02 — Correções | Corrigir falhas identificadas no aceite ou na integração; preservar o funcionamento dos fluxos entregues. | Conforme falhas identificadas | 3 | Giovana | Planejado — validação pendente |
| RF03 — Relatórios (conclusão) | Completar interface, gráficos e navegação entre meses, com validação integrada dos requisitos de desempenho e precisão. | Reservado para a rodada 3 | 3 | A definir pela equipe | Planejado — validação pendente |
| RF04 — Metas de Economia | Implementar criação, acompanhamento, edição e exclusão/cancelamento de metas, progresso, estados e integração com transações. | Reservado para a rodada 3 | 3 | A definir pela equipe | Planejado — validação pendente |
| RF05 — Exportação CSV | Implementar filtro de período, geração e disponibilização de CSV com colunas padronizadas e validação do conteúdo. | Reservado para a rodada 3 | 3 | A definir pela equipe | Planejado — validação pendente |

## Rastreabilidade com a EAP

Os IDs hierárquicos abaixo correspondem ao [escopo revisado](02-escopo-eap.md). A divisão de trabalho também utiliza códigos PTxx para os pacotes de estimativa; os dois sistemas de identificação devem ser preservados, sem renumeração.

| Entrega da rodada 2 | IDs da EAP | Pacotes PT da divisão de trabalho | Issues relacionadas |
|---|---|---|---|
| Infraestrutura de persistência e execução | 2.2 e 2.4 | PT01–PT02 | [#14 — Docker Compose e perfis](https://github.com/ArbenTafili/finapp_Uff/issues/14), [#15 — Schema monetário](https://github.com/ArbenTafili/finapp_Uff/issues/15) |
| RF01 | 3.1.1–3.1.7 | PT03–PT08 | [#16 — Transações](https://github.com/ArbenTafili/finapp_Uff/issues/16) |
| RF02 e integração Categoria × Transação | 3.2.1–3.2.6 | PT09–PT12 | [#17 — Categorias](https://github.com/ArbenTafili/finapp_Uff/issues/17), [#18 — Integração](https://github.com/ArbenTafili/finapp_Uff/issues/18) |
| RF03 parcial | 3.3.1–3.3.3; medição de desempenho vinculada a 3.3.7 | PT13–PT18, com entrega parcial nesta rodada | [#19 — Queries](https://github.com/ArbenTafili/finapp_Uff/issues/19), [#20 — Endpoint mensal](https://github.com/ArbenTafili/finapp_Uff/issues/20) |
| Testes das regras de cálculo | 4.1, com apoio aos fluxos de 4.2–4.3 | PT30–PT34, conforme divisão de testes da equipe | [#21 — Testes de cálculo](https://github.com/ArbenTafili/finapp_Uff/issues/21) |
| Preparação e ensaio da Demo v2 | 4.8 | Fase 4 | [#29 — Preparar e ensaiar a Demo v2](https://github.com/ArbenTafili/finapp_Uff/issues/29) |

Os pacotes 3.3.4–3.3.6 e a conclusão de 3.3.7 ficam para a rodada 3, junto de 3.4 (Metas) e 3.5 (Exportação CSV). A indicação de um intervalo de PTs não significa que todos estejam concluídos nesta rodada.

## Situação das entregas técnicas

| Issues | Entrega e evidência de configuração | Integração | Aceite funcional |
|---|---|---|---|
| #14/#15 | Compose, perfis e schema NUMERIC(19,2); [PR #34](https://github.com/ArbenTafili/finapp_Uff/pull/34) e [PR #35](https://github.com/ArbenTafili/finapp_Uff/pull/35). | Main; Issues fechadas. | Evidências de execução a consolidar no ensaio. |
| #17 | Seed idempotente, nomes únicos por tipo e regras de categoria; [PR #39](https://github.com/ArbenTafili/finapp_Uff/pull/39), seguido do [PR #43](https://github.com/ArbenTafili/finapp_Uff/pull/43). | Main; Issue fechada. | Pendente. |
| #18 | Tipo da transação compatível com a categoria; [PR #40](https://github.com/ArbenTafili/finapp_Uff/pull/40) e consolidação no PR #43. | Main; Issue fechada. | Pendente. |
| #16 | Precisão de entrada, descrição, erros e endpoint de saldo; [PR #41](https://github.com/ArbenTafili/finapp_Uff/pull/41), seguido do PR #43. | Main; Issue fechada. | Pendente. |
| #21 | Testes unitários de cálculo e precisão; [PR #42](https://github.com/ArbenTafili/finapp_Uff/pull/42), seguido do PR #43. | Main; Issue fechada. | Execução e resultados a consolidar. |
| #19/#20 | Queries e endpoint mensal; [PR #36](https://github.com/ArbenTafili/finapp_Uff/pull/36) e [PR #37](https://github.com/ArbenTafili/finapp_Uff/pull/37). | Main; Issues fechadas. | Pendente de validação por cenário. |
| #29 | Massa D1–D4 e preparação técnica; [PR #38](https://github.com/ArbenTafili/finapp_Uff/pull/38). | Main; Issue aberta, aguardando ensaio. | Ensaio completo e contingência a executar e registrar. |

As entregas da Giovana foram integradas à main pelo PR #43. A equipe informou o fluxo atual: criar a branch a partir da main, publicar commits e abrir um PR por Issue com destino à develop; a integração à main ocorre depois. Arben deve alinhar a estratégia formal e a Seção 8 do Plano, que ainda descrevem GitHub Flow, a esse fluxo operacional.

As novas regras estão registradas no [ADR 002 da Giovana](../rodada1/10-decisoes-tecnicas.md), ainda com status Proposto. O ADR 001 e o RDT-01 permanecem registros independentes.

## Revisão do escopo e justificativas

- **Priorização:** o início de RF04, previsto na distribuição inicial da EAP para a rodada 2, foi transferido para a rodada 3. A justificativa é concentrar a execução na conclusão de RF01/RF02 e na entrega parcial de RF03. RF04 permanece no escopo total do produto.
- **RF03 parcial:** a rodada 2 cobre consultas e endpoint mensal, já integrados à main. Os campos `mesAnterior` e `proximoMes` estão disponíveis na API; a interface completa, os gráficos e a navegação visual permanecem na rodada 3.
- **RNF03:** adotar a redação acordada: “Os dados devem ser armazenados em banco de dados executado localmente (Docker), sem envio a serviços externos.” A escolha está no [RDT-01 canônico](../adr/RDT-01.md), integrado pelo Arben na [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13). A [complementação da Sara](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-rdt01/docs/adr/RDT-01.md) está publicada em branch; data e aprovação formal da decisão permanecem a confirmar.
- **ADR 001:** preservar a regra `data <= hoje`, permitindo registros retroativos e bloqueando datas futuras. ADR 001 e RDT-01 são registros distintos.
- **Impactos:** a transferência de metas altera a distribuição do trabalho entre rodadas. Impacto em horas, prazo detalhado e custo: **a confirmar com Filipe**; esta revisão não estabelece novos valores de orçamento.

## Limites preservados

O produto continua limitado a RF01–RF05 e RNF01–RNF05. Permanecem fora do escopo autenticação/Usuario, sincronização bancária, investimentos, gestão avançada de cartão de crédito, orçamento futuro detalhado, empréstimos/dívidas e aplicativos móveis nativos.

## Cobertura dos critérios da Issue #24

| Critério de aceite da Issue | Onde está documentado |
|---|---|
| Itens da Iteração 2 revisados | Tabela de backlog por rodada e separação de RF03 parcial/conclusão. |
| Prioridades atualizadas | P1 para concluir RF01/RF02 e P2 para RF03 parcial. |
| Itens fora do escopo identificados | Limites preservados e itens reservados para a rodada 3. RF04/RF05 continuam no escopo total. |
| Mudanças relevantes possuem registro | Justificativa da transferência de metas, revisão do escopo e RDT-01 vinculado à Issue #13. |
| Backlog coerente com as Issues da Rodada 2 | Tabelas de rastreabilidade e situação técnica, distinguindo main, develop e branches pendentes. |
| Escopo da Demo v2 definido | RF01/RF02 e RF03 parcial; critérios e roteiro vinculados à Issue #25. |

Esta tabela registra a cobertura documental. O aceite da documentação e a execução das funcionalidades são verificações distintas; publicar o backlog não comprova que todos os itens de desenvolvimento estejam concluídos.

## Pendências para consolidação

| Pendência | Encaminhamento |
|---|---|
| Impacto em horas, prazo e custo | A confirmar com Filipe, incluindo a revisão de cronograma e o impacto associado ao RDT-01; tarefas de acompanhamento [#22](https://github.com/ArbenTafili/finapp_Uff/issues/22) e [#23](https://github.com/ArbenTafili/finapp_Uff/issues/23). |
| Validação formal do RDT-01 | Registro inicial integrado e complementação da Sara em branch; confirmar data, aprovadores e histórico das alternativas com a equipe. |
| Critérios Dado/Quando/Então e roteiro da Demo v2 | Documento da entrega de Sara na [Issue #25](https://github.com/ArbenTafili/finapp_Uff/issues/25), publicado na branch `feature/sara-aceite-demo-v2`; execução e ensaio pela equipe permanecem pendentes. |
| Evidências de conclusão | Giovana e Enzo apresentam os fluxos e resultados; Sara valida o aceite antes de atualizar a situação das entregas. |
| Aprovação do ADR 002 | Confirmar com Giovana e Enzo o status formal do registro, ainda identificado como Proposto. |
| Estratégia formal de integração | Arben deve atualizar a estratégia de branches e a Seção 8 para o fluxo branch → develop → main comunicado pela equipe. |
| Responsáveis técnicos da rodada 3 | A definir pela equipe no planejamento daquela rodada. |
