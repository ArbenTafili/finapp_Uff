# Seção 8 — Controle de Versões

Na Rodada 2, o FinApp usa GitHub Flow. Cada trabalho parte da `main` protegida em uma branch ligada à Issue correspondente. A integração ocorre por Pull Request para `main`, após revisão, com **squash merge**. A branch `develop` da Rodada 1 fica preservada apenas como histórico.

Branches seguem `tipo/numero-da-issue-descricao`; commits seguem `tipo(escopo): descrição #issue`. O PR referencia a Issue com `Closes #N`. A [estratégia detalhada](../adr/estrategia-branches.md) registra as regras de revisão e proteção.

As Issues registram o trabalho; os milestones agrupam `Iteração 1 / Rodada 1`, `Iteração 2 / Rodada 2` e `Iteração 3 / Rodada 3`. As labels existentes são `config`, `infra`, `feature`, `teste`, `gestão`, `demo`, `bug` e `mudança-de-escopo`. Os templates solicitam pacote EAP, horas previstas e critérios de aceite, sem preencher estimativas ausentes.

A decisão [RDT-01](../adr/RDT-01.md), associada à [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13), documenta PostgreSQL local via Docker e a interpretação do RNF03. O registro é mantido junto às demais decisões técnicas.
