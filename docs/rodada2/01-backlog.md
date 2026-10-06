# Backlog e Priorização — Rodada 2 — FinApp

**Responsável:** Sara Marcomini (Product Owner)

**Situação das entregas:** Planejado — validação pendente

## Objetivo e ponto de partida

Concluir RF01 (Transações) e RF02 (Categorias) e iniciar RF03 (Relatórios), preparando a Demo v2. A Demo v1, com CRUD de transações e categorias, é o ponto de partida informado pela equipe. Esse histórico não representa aceite automático das entregas da rodada 2: a conclusão depende de validação e evidências.

Esta revisão corresponde à tarefa de backlog e escopo identificada como #15 na divisão de trabalho. Esse número é uma referência do planejamento; o número real da Issue será informado pelo Arben.

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

| Entrega da rodada 2 | IDs da EAP | Pacotes PT da divisão de trabalho |
|---|---|---|
| RF01 | 3.1.1–3.1.7 | PT03–PT08 |
| RF02 e integração Categoria × Transação | 3.2.1–3.2.6 | PT09–PT12 |
| RF03 parcial | 3.3.1–3.3.3; medição de desempenho vinculada a 3.3.7 | PT13–PT18, com entrega parcial nesta rodada |
| Testes correspondentes e Demo v2 | 4.1–4.3, 4.5–4.6 conforme os fluxos disponíveis; 4.8 | Conforme divisão de testes e demonstração da equipe |

Os pacotes 3.3.4–3.3.6 e a conclusão de 3.3.7 ficam para a rodada 3, junto de 3.4 (Metas) e 3.5 (Exportação CSV). A indicação de um intervalo de PTs não significa que todos estejam concluídos nesta rodada.

## Revisão do escopo e justificativas

- **Priorização:** o início de RF04, previsto na distribuição inicial da EAP para a rodada 2, foi transferido para a rodada 3. A justificativa é concentrar a execução na conclusão de RF01/RF02 e na entrega parcial de RF03. RF04 permanece no escopo total do produto.
- **RF03 parcial:** a rodada 2 cobre consultas e endpoint mensal. Interface completa, gráficos e navegação entre meses permanecem previstos para a rodada 3.
- **RNF03:** adotar a redação acordada: “Os dados devem ser armazenados em banco de dados executado localmente (Docker), sem envio a serviços externos.” A escolha de PostgreSQL em Docker local será formalizada no RDT-01.
- **ADR 001:** preservar a regra `data <= hoje`, permitindo registros retroativos e bloqueando datas futuras. ADR 001 e RDT-01 são registros distintos.
- **Impactos:** a transferência de metas altera a distribuição do trabalho entre rodadas. Impacto em horas, prazo detalhado e custo: **a confirmar com Filipe**; esta revisão não estabelece novos valores de orçamento.

## Limites preservados

O produto continua limitado a RF01–RF05 e RNF01–RNF05. Permanecem fora do escopo autenticação/Usuario, sincronização bancária, investimentos, gestão avançada de cartão de crédito, orçamento futuro detalhado, empréstimos/dívidas e aplicativos móveis nativos.

## Pendências para consolidação

| Pendência | Encaminhamento |
|---|---|
| Número real da Issue de backlog e escopo | A informar pelo Arben; substituir a referência provisória #15 nos registros de rastreabilidade. |
| Impacto em horas, prazo e custo | A confirmar com Filipe, incluindo a revisão de cronograma e o impacto associado ao RDT-01. |
| Registro formal RDT-01 | Será preparado na entrega seguinte, separado do ADR 001; número real da Issue a informar pelo Arben. |
| Critérios Dado/Quando/Então e roteiro da Demo v2 | Serão preparados na entrega específica de Sara, identificada provisoriamente como #16. |
| Evidências de conclusão | Giovana e Enzo apresentam os fluxos e resultados; Sara valida o aceite antes de atualizar a situação das entregas. |
| Responsáveis técnicos da rodada 3 | A definir pela equipe no planejamento daquela rodada. |
