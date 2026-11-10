# Backlog e Priorização — Rodada 2 — FinApp

**Responsável:** Sara Marcomini (Product Owner)

**Base de código:** `main`, versão `c86694f`.

## Objetivo e ponto de partida

A rodada 2 tem como objetivo concluir RF01 (Transações) e RF02 (Categorias) e entregar as consultas e o endpoint mensal de RF03 (Relatórios). A Demo v1, com CRUD de transações e categorias, é o ponto de partida.

Este backlog atende à [Issue #24](https://github.com/ArbenTafili/finapp_Uff/issues/24). Os [critérios de aceite e o roteiro da Demo v2](04-aceite-demo-v2.md) estão vinculados à [Issue #25](https://github.com/ArbenTafili/finapp_Uff/issues/25).

## Backlog por rodada

P1 indica prioridade de conclusão na rodada 2; P2 indica a entrega parcial de relatórios nessa rodada. Os itens reservados para a rodada 3 terão sua ordem detalhada no planejamento daquela rodada.

| Requisito | Entrega prevista | Prioridade | Rodada | Responsável técnico | Situação |
|---|---|---|---|---|---|
| RF01 — Gerenciar Transações | Concluir CRUD; validar valor positivo com até duas casas decimais, categoria obrigatória e `data <= hoje`; conferir saldo e erros de entrada. | P1 | 2 | Giovana | Integrado à main — PR #43. |
| RF02 — Gerenciar Categorias | Concluir seed idempotente e CRUD de categorias personalizadas; bloquear edição/exclusão de padrão, exclusão de categoria vinculada e alteração incompatível de tipo; tratar nomes duplicados por tipo e integrar às transações. | P1 | 2 | Giovana | Integrado à main — PR #43. |
| RF03 — Relatórios (parcial) | Consultas e endpoint mensal com receitas, despesas, saldo e distribuição de despesas por categoria; validar resultados e medir RNF02. | P2 | 2 | Enzo | Integrado à main — PRs #36/#37. |
| RF01/RF02 — Correções | Corrigir falhas identificadas no aceite ou na integração; preservar o funcionamento dos fluxos entregues. | Conforme falhas identificadas | 3 | Giovana | Planejado |
| RF03 — Relatórios (conclusão) | Completar interface, gráficos e navegação entre meses, com validação integrada dos requisitos de desempenho e precisão. | Reservado para a rodada 3 | 3 | A definir pela equipe | Planejado |
| RF04 — Metas de Economia | Implementar criação, acompanhamento, edição e exclusão/cancelamento de metas, progresso, estados e integração com transações. | Reservado para a rodada 3 | 3 | A definir pela equipe | Planejado |
| RF05 — Exportação CSV | Implementar filtro de período, geração e disponibilização de CSV com colunas padronizadas e validação do conteúdo. | Reservado para a rodada 3 | 3 | A definir pela equipe | Planejado |

## Rastreabilidade com a EAP

A tabela relaciona os IDs do [escopo revisado](02-escopo-eap.md), os pacotes de estimativa PTxx e as Issues da rodada 2.

| Entrega da rodada 2 | IDs da EAP | Pacotes PT da divisão de trabalho | Issues relacionadas |
|---|---|---|---|
| Infraestrutura de persistência e execução | 2.2 e 2.4 | PT01–PT02 | [#14 — Docker Compose e perfis](https://github.com/ArbenTafili/finapp_Uff/issues/14), [#15 — Schema monetário](https://github.com/ArbenTafili/finapp_Uff/issues/15) |
| RF01 | 3.1.1–3.1.7 | PT03–PT08 | [#16 — Transações](https://github.com/ArbenTafili/finapp_Uff/issues/16) |
| RF02 e integração Categoria × Transação | 3.2.1–3.2.6 | PT09–PT12 | [#17 — Categorias](https://github.com/ArbenTafili/finapp_Uff/issues/17), [#18 — Integração](https://github.com/ArbenTafili/finapp_Uff/issues/18) |
| RF03 parcial | 3.3.1–3.3.3; medição de desempenho vinculada a 3.3.7 | PT13–PT18, com entrega parcial nesta rodada | [#19 — Queries](https://github.com/ArbenTafili/finapp_Uff/issues/19), [#20 — Endpoint mensal](https://github.com/ArbenTafili/finapp_Uff/issues/20) |
| Testes das regras de cálculo | 4.1, com apoio aos fluxos de 4.2–4.3 | PT30–PT34, conforme divisão de testes da equipe | [#21 — Testes de cálculo](https://github.com/ArbenTafili/finapp_Uff/issues/21) |
| Preparação e ensaio da Demo v2 | 4.8 | Fase 4 | [#29 — Preparar e ensaiar a Demo v2](https://github.com/ArbenTafili/finapp_Uff/issues/29) |

Os pacotes 3.3.4–3.3.6 e a conclusão de 3.3.7 ficam para a rodada 3, junto de 3.4 (Metas) e 3.5 (Exportação CSV).

## Implementação e integração

| Issues | Implementação | Integração |
|---|---|---|
| #14/#15 | Compose, perfis e schema NUMERIC(19,2); [PR #34](https://github.com/ArbenTafili/finapp_Uff/pull/34) e [PR #35](https://github.com/ArbenTafili/finapp_Uff/pull/35). | Main |
| #17 | Seed idempotente, nomes únicos por tipo e regras de categoria; [PR #39](https://github.com/ArbenTafili/finapp_Uff/pull/39). | Main, pelo [PR #43](https://github.com/ArbenTafili/finapp_Uff/pull/43) |
| #18 | Compatibilidade entre o tipo da transação e o da categoria; [PR #40](https://github.com/ArbenTafili/finapp_Uff/pull/40). | Main |
| #16 | Precisão de entrada, descrição, tratamento de erros e endpoint de saldo; [PR #41](https://github.com/ArbenTafili/finapp_Uff/pull/41). | Main, pelo PR #43 |
| #21 | Testes unitários de cálculo e precisão; [PR #42](https://github.com/ArbenTafili/finapp_Uff/pull/42). | Main, pelo PR #43 |
| #19/#20 | Queries e endpoint mensal; [PR #36](https://github.com/ArbenTafili/finapp_Uff/pull/36) e [PR #37](https://github.com/ArbenTafili/finapp_Uff/pull/37). | Main |
| #29 | Massa D1–D4 e preparação técnica; [PR #38](https://github.com/ArbenTafili/finapp_Uff/pull/38). | Documento e massa de demonstração na main |

O fluxo de publicação é branch criada a partir da main → PR para develop → main.

As regras de categorias estão descritas no [ADR 002](../rodada1/10-decisoes-tecnicas.md). A persistência local está registrada no [RDT-01](../adr/RDT-01.md).

## Revisão do escopo e justificativas

- **Priorização:** o início de RF04, previsto na distribuição inicial da EAP para a rodada 2, foi transferido para a rodada 3. A justificativa é concentrar a execução na conclusão de RF01/RF02 e na entrega parcial de RF03. RF04 permanece no escopo total do produto.
- **RF03 parcial:** a rodada 2 cobre consultas e endpoint mensal, já integrados à main. Os campos `mesAnterior` e `proximoMes` estão disponíveis na API; a interface completa, os gráficos e a navegação visual permanecem na rodada 3.
- **RNF03:** “Os dados devem ser armazenados em banco de dados executado localmente (Docker), sem envio a serviços externos.” A decisão está no [RDT-01](../adr/RDT-01.md), vinculado à [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13).
- **ADR 001:** preservar a regra `data <= hoje`, permitindo registros retroativos e bloqueando datas futuras. ADR 001 e RDT-01 são registros distintos.
- **Impactos:** a transferência de metas reorganiza o trabalho entre rodadas. O acompanhamento de horas e custo está no [Monitoramento e Controle](../plano-projeto/secao-7-monitoramento-controle.md); a revisão de cronograma e orçamento está vinculada à [Issue #23](https://github.com/ArbenTafili/finapp_Uff/issues/23).

## Limites preservados

O produto continua limitado a RF01–RF05 e RNF01–RNF05. Permanecem fora do escopo autenticação/Usuario, sincronização bancária, investimentos, gestão avançada de cartão de crédito, orçamento futuro detalhado, empréstimos/dívidas e aplicativos móveis nativos.

## Cobertura dos critérios da Issue #24

| Critério de aceite da Issue | Onde está documentado |
|---|---|
| Itens da Iteração 2 revisados | Tabela de backlog por rodada e separação de RF03 parcial/conclusão. |
| Prioridades atualizadas | P1 para concluir RF01/RF02 e P2 para RF03 parcial. |
| Itens fora do escopo identificados | Limites preservados e itens reservados para a rodada 3. RF04/RF05 continuam no escopo total. |
| Mudanças relevantes possuem registro | Justificativa da transferência de metas, revisão do escopo e RDT-01 vinculado à Issue #13. |
| Backlog coerente com as Issues da Rodada 2 | Tabelas de rastreabilidade e integração, com os links das Issues e dos PRs. |
| Escopo da Demo v2 definido | RF01/RF02 e RF03 parcial; critérios e roteiro vinculados à Issue #25. |
