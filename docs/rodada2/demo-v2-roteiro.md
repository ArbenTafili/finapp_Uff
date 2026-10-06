# Demo v2 — Roteiro e preparação

**Definição de escopo e aceite:** Sara Marcomini (Product Owner)

**Preparação técnica e ensaio:** Enzo e Giovana

**Duração da demonstração:** cinco minutos

## Documentos de referência

- [Critérios de Aceite e Roteiro da Demo v2 — Issue #25](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-aceite-demo-v2/docs/rodada2/04-aceite-demo-v2.md): histórias, sequência, responsáveis por etapa, cenários e registro de resultados. O documento permanece em branch enquanto sua integração não ocorrer.
- [Preparação técnica e plano B — Issue #29](05-preparacao-demo-v2.md): ambiente dedicado, dados ao vivo ou pré-carregados, portas, contingência e ensaios.
- [Backlog e escopo — Issue #24](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-backlog-rodada2/docs/rodada2/01-backlog.md): prioridades e situação de integração das entregas.

## Escopo e dados da demonstração

A Demo v2 cobre RF01/RF02 e RF03 parcial pelo endpoint mensal. Interface completa de relatórios, gráficos, navegação visual, metas e CSV ficam para a rodada 3.

A massa base contém quatro transações em setembro de 2026: receita de R$ 3.000,00 em Salário e despesas de R$ 200,00 em Alimentação, R$ 100,00 em Transporte e R$ 300,00 em Educação Demo. O relatório de `2026-09` deve retornar despesas de R$ 600,00 e saldo de R$ 2.400,00.

Usar o roteiro da Issue #25 e a preparação da Issue #29. Os dados pré-carregados não devem ser cadastrados novamente; T1 é a transação temporária para demonstrar CRUD e deve ser excluída antes da conferência final.

## Condições do ensaio

As entregas da Giovana foram integradas à main pelo PR #43, incluindo os complementos de RF01/RF02 e testes de cálculo. Enzo e Giovana registram commit, ambiente, resultados e impedimentos dos ensaios; Sara valida o aceite funcional com essas evidências. Novas alterações seguem branch → develop → main, com PR por Issue para develop.
