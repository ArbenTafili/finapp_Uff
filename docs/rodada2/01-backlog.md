# Backlog e Priorização — Rodada 2 — FinApp

**Responsável:** Sara Marcomini (Product Owner)

**Situação das entregas:** Planejado — validação pendente

## Objetivo e ponto de partida

Concluir RF01 (Transações) e RF02 (Categorias) e iniciar RF03 (Relatórios), preparando a Demo v2. A Demo v1, com CRUD de transações e categorias, é o ponto de partida informado pela equipe. Esse histórico não representa aceite automático das entregas da rodada 2: a conclusão depende de validação e evidências.

Esta revisão atende à [Issue #24 — Revisar backlog e escopo da Iteração 2](https://github.com/ArbenTafili/finapp_Uff/issues/24), atribuída a Sara. O escopo da Demo v2 inclui RF01/RF02 e RF03 parcial, com consultas e endpoint mensal; o roteiro e os critérios Dado/Quando/Então são a entrega da [Issue #25](https://github.com/ArbenTafili/finapp_Uff/issues/25).

## Backlog por rodada

P1 indica prioridade de conclusão na rodada 2; P2 indica a entrega parcial de relatórios nessa rodada. Os itens reservados para a rodada 3 terão sua ordem detalhada no planejamento daquela rodada.

| Requisito | Entrega prevista | Prioridade | Rodada | Responsável técnico | Situação |
|---|---|---|---|---|---|
| RF01 — Gerenciar Transações | Concluir cadastro, listagem, edição e exclusão de receitas e despesas; validar valor maior que zero, categoria obrigatória e `data <= hoje`; conferir cálculo de saldo. | P1 | 2 | Giovana | Planejado — validação pendente |
| RF02 — Gerenciar Categorias | Concluir categorias padrão pré-carregadas e CRUD de categorias personalizadas; impedir exclusão de categoria padrão, tratar vínculos com transações e integrar categorias aos fluxos de transação. | P1 | 2 | Giovana | Planejado — validação pendente |
| RF03 — Relatórios (parcial) | Implementar consultas e endpoint mensal com total de receitas, total de despesas, saldo e distribuição de despesas por categoria. Validar os resultados e medir o atendimento ao RNF02. | P2 | 2 | Enzo | Planejado — validação pendente |
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

## Revisão do escopo e justificativas

- **Priorização:** o início de RF04, previsto na distribuição inicial da EAP para a rodada 2, foi transferido para a rodada 3. A justificativa é concentrar a execução na conclusão de RF01/RF02 e na entrega parcial de RF03. RF04 permanece no escopo total do produto.
- **RF03 parcial:** a rodada 2 cobre consultas e endpoint mensal. Interface completa, gráficos e navegação entre meses permanecem previstos para a rodada 3.
- **RNF03:** adotar a redação acordada: “Os dados devem ser armazenados em banco de dados executado localmente (Docker), sem envio a serviços externos.” A escolha de PostgreSQL em Docker local está documentada no [RDT-01](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-rdt01/docs/rodada2/03-rdt-01.md), vinculado à [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13). A validação formal do registro permanece pendente.
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
| Backlog coerente com as Issues da Rodada 2 | Tabela de rastreabilidade com links para as tarefas técnicas e de demonstração. |
| Escopo da Demo v2 definido | RF01/RF02 e RF03 parcial; critérios e roteiro vinculados à Issue #25. |

Esta tabela registra a cobertura documental. O aceite da documentação e a execução das funcionalidades são verificações distintas; publicar o backlog não comprova que todos os itens de desenvolvimento estejam concluídos.

## Pendências para consolidação

| Pendência | Encaminhamento |
|---|---|
| Impacto em horas, prazo e custo | A confirmar com Filipe, incluindo a revisão de cronograma e o impacto associado ao RDT-01; tarefas de acompanhamento [#22](https://github.com/ArbenTafili/finapp_Uff/issues/22) e [#23](https://github.com/ArbenTafili/finapp_Uff/issues/23). |
| Validação formal do RDT-01 | Registro redigido na entrega da Issue #13, separado do ADR 001; confirmar data, aprovação e histórico das alternativas com a equipe. |
| Critérios Dado/Quando/Então e roteiro da Demo v2 | Documento da entrega de Sara na [Issue #25](https://github.com/ArbenTafili/finapp_Uff/issues/25), publicado na branch `feature/sara-aceite-demo-v2`; execução e ensaio pela equipe permanecem pendentes. |
| Evidências de conclusão | Giovana e Enzo apresentam os fluxos e resultados; Sara valida o aceite antes de atualizar a situação das entregas. |
| Responsáveis técnicos da rodada 3 | A definir pela equipe no planejamento daquela rodada. |
