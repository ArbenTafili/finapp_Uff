# Critérios de Aceite e Roteiro da Demo v2 — FinApp

**Responsável pela definição:** Sara Marcomini (Product Owner)

**Issue:** [#25 — Definir critérios de aceite e roteiro da Demo v2](https://github.com/ArbenTafili/finapp_Uff/issues/25)

**Situação dos cenários:** Não executado — validação pendente

**Preparação técnica e ensaio:** Enzo e Giovana, na [Issue #29](https://github.com/ArbenTafili/finapp_Uff/issues/29).

## 1. Escopo e histórias de usuário

A Demo v2 cobre a conclusão de RF01/RF02 e a entrega parcial de RF03. Interface completa de relatórios, gráficos e navegação entre meses ficam para a rodada 3, assim como RF04 (Metas) e RF05 (Exportação CSV). Autenticação/Usuario e as demais funcionalidades excluídas do produto permanecem fora do escopo.

| História | Necessidade do usuário | Entrega demonstrável na rodada 2 | Issues técnicas |
|---|---|---|---|
| RF01 — Transações | Como usuário, quero registrar, editar e excluir receitas e despesas para acompanhar minhas movimentações financeiras. | CRUD com valor, tipo, categoria, data e descrição opcional; validações e conferência de saldo. | [#16](https://github.com/ArbenTafili/finapp_Uff/issues/16), com testes de cálculo na [#21](https://github.com/ArbenTafili/finapp_Uff/issues/21) |
| RF02 — Categorias | Como usuário, quero usar categorias padrão e personalizadas para organizar minhas transações. | Seed, CRUD de categorias personalizadas, regras de exclusão e vínculo com transações. | [#17](https://github.com/ArbenTafili/finapp_Uff/issues/17) e [#18](https://github.com/ArbenTafili/finapp_Uff/issues/18) |
| RF03 — Relatórios, parcial | Como usuário, quero consultar os totais mensais e a distribuição das despesas para compreender meus gastos. | Consultas e endpoint mensal com receitas, despesas, saldo e distribuição por categoria. | [#19](https://github.com/ArbenTafili/finapp_Uff/issues/19) e [#20](https://github.com/ArbenTafili/finapp_Uff/issues/20) |

O [backlog e escopo da Issue #24](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-backlog-rodada2/docs/rodada2/01-backlog.md) é a referência de priorização. A persistência local e a redação do RNF03 estão documentadas no [RDT-01 da Issue #13](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-rdt01/docs/rodada2/03-rdt-01.md). A regra `data <= hoje` segue o [ADR 001](../rodada1/10-decisoes-tecnicas.md).

## 2. Preparação para execução

1. Utilizar uma instância local dedicada à demonstração, sem outras transações em setembro de 2026. Não apagar dados de uso real para preparar os cenários.
2. Iniciar o ambiente Docker conforme o [README](../../README.md), com `docker compose up --build`, e aguardar a aplicação e o banco ficarem disponíveis.
3. Conferir as categorias em `GET /api/categorias`. Usar os IDs retornados, sem presumir números fixos. Se um registro da massa de demonstração já existir, conferir seu conteúdo antes de cadastrá-lo novamente.
4. Reservar a categoria personalizada `Curso Demo`, do tipo `DESPESA`, para criação e alteração para `Educação Demo` antes de associar transações. Fazer isso uma única vez, na preparação ou durante a apresentação. Se os dados já estiverem pré-carregados, reutilizar o ID de Educação Demo; demonstrar o CRUD de categoria com `Categoria Temporária Demo`, sem vínculos, para evitar duplicações.
5. Usar a interface em `http://localhost:8080/` para os fluxos de transações disponíveis. Para operações de categoria sem tela disponível, demonstrar a API com `curl` no terminal e comandos preparados pela dupla de desenvolvimento.
6. Demonstrar RF03 pelo endpoint planejado `GET /api/relatorios?mes=2026-09`, abrindo a resposta no navegador. Esse endpoint depende da entrega da Issue #20; sua existência e funcionamento ainda devem ser validados pela equipe.
7. Separar os IDs dos registros temporários para editá-los ou excluí-los sem afetar outras transações. A massa base deve estar recomposta antes da conferência final do relatório.

Os cenários abaixo definem resultados esperados. Não afirmam que os endpoints ou regras já passaram nos testes.

## 3. Dados de exemplo e resultados esperados

Os valores são dados fictícios de demonstração. Para receitas, usar categorias do tipo `RECEITA`; para despesas, categorias do tipo `DESPESA`.

| Identificação | Data | Tipo | Categoria | Valor | Descrição |
|---|---|---|---|---|---|
| D1 | 2026-09-01 | RECEITA | Salário | R$ 3.000,00 | Salário Demo v2 |
| D2 | 2026-09-02 | DESPESA | Alimentação | R$ 200,00 | Mercado Demo v2 |
| D3 | 2026-09-03 | DESPESA | Transporte | R$ 100,00 | Transporte Demo v2 |
| D4 | 2026-09-04 | DESPESA | Educação Demo | R$ 300,00 | Curso Demo v2 |

| Indicador de setembro de 2026 | Resultado esperado |
|---|---|
| Total de receitas | R$ 3.000,00 |
| Total de despesas | R$ 600,00 |
| Saldo: receitas menos despesas | R$ 2.400,00 |
| Despesas de Alimentação | R$ 200,00; aproximadamente 33,33% das despesas |
| Despesas de Transporte | R$ 100,00; aproximadamente 16,67% das despesas |
| Despesas de Educação Demo | R$ 300,00; 50,00% das despesas |

Os percentuais usam o total de despesas como denominador. A categoria Salário não entra na distribuição de despesas. Os percentuais apresentados estão arredondados para duas casas decimais, enquanto os valores monetários devem permanecer exatos.

Dados adicionais para cenários específicos:

- **T1 — transação temporária:** despesa de R$ 20,00 em Educação Demo, em 2026-09-05; editar para R$ 50,00 e depois excluir. Após a exclusão, os totais devem voltar aos da massa base.
- **T2 — outro mês:** receita de R$ 100,00 em Outras Receitas, em 2026-08-15. Usar apenas para o cenário de filtragem por mês e excluir depois do teste. Ela não pode alterar os totais de setembro.
- **Valores inválidos:** zero e valor negativo, enviados separadamente; não devem gerar transações.
- **Data futura:** dia seguinte à data corrente da máquina; não deve gerar transação.
- **Precisão decimal:** em junho de 2026, sem outras transações nesse mês na instância de teste, cadastrar receitas de R$ 10,10 em 2026-06-01 e R$ 0,20 em 2026-06-02 e despesa de R$ 0,10 em 2026-06-03. O relatório de junho deve produzir saldo de R$ 10,20. Excluir apenas esses registros depois do cenário.

## 4. Critérios de aceite — Dado/Quando/Então

### RF01 — Gerenciar Transações

| ID | Dado | Quando | Então |
|---|---|---|---|
| RF01-01 | Categoria válida e dados de receita ou despesa com valor positivo e data permitida. | O usuário cadastra a transação com valor, tipo, categoria, data e descrição. | O registro é salvo e pode ser consultado com os dados informados. |
| RF01-02 | Uma transação válida sem descrição. | O usuário solicita o cadastro. | O registro é aceito, pois a descrição é opcional. |
| RF01-03 | A massa D1–D4 cadastrada. | O usuário consulta a listagem e cada registro por seu ID. | As quatro transações são encontradas com valores, tipos, datas e categorias corretos. |
| RF01-04 | T1 cadastrada como despesa de R$ 20,00. | O usuário edita seu valor para R$ 50,00 e consulta novamente. | O registro apresenta R$ 50,00; as despesas do mês passam a R$ 650,00 e o saldo a R$ 2.350,00. |
| RF01-05 | T1 editada e os registros D1–D4 preservados. | O usuário exclui T1. | T1 deixa de aparecer na listagem; o relatório volta a despesas de R$ 600,00 e saldo de R$ 2.400,00. |
| RF01-06 | Uma tentativa de cadastro ou edição com valor zero ou negativo. | O usuário envia cada valor inválido. | A operação é rejeitada com mensagem clara; os registros válidos e os totais permanecem inalterados. |
| RF01-07 | Uma tentativa com data posterior ao dia corrente. | O usuário cadastra ou edita a transação. | A operação é rejeitada sem persistir a data futura, conforme ADR 001. |
| RF01-08 | Dados válidos com data passada ou igual ao dia corrente. | O usuário solicita o cadastro. | A data é aceita; não é criada restrição de data de cadastro de Usuario. |
| RF01-09 | Uma categoria ausente ou um ID de categoria inexistente. | O usuário tenta cadastrar ou editar uma transação. | A operação é rejeitada com erro compreensível e sem associação inválida no banco. |
| RF01-10 | O cenário separado de precisão decimal em junho de 2026. | O sistema calcula os totais e o saldo daquele mês. | Receitas de R$ 10,30 e despesa de R$ 0,10 resultam em saldo exato de R$ 10,20. |
| RF01-11 | Aplicação pronta e uma categoria válida disponível. | O usuário realiza o fluxo de cadastro de uma transação enquanto o tempo é medido. | Consegue concluir o registro em menos de 30 segundos; tempo, navegador e dados utilizados ficam registrados como evidência do RNF01. |

### RF02 — Gerenciar Categorias

| ID | Dado | Quando | Então |
|---|---|---|---|
| RF02-01 | Banco de demonstração inicializado para receber as categorias padrão. | A aplicação é iniciada e as categorias são consultadas. | As categorias padrão previstas no seed ficam disponíveis, incluindo Salário, Alimentação e Transporte, com indicação de categoria padrão. |
| RF02-02 | Categorias padrão já carregadas e banco persistente. | A aplicação é reiniciada e as categorias são consultadas novamente. | As categorias padrão não são duplicadas e os dados persistentes são preservados. |
| RF02-03 | Nome Curso Demo e tipo DESPESA válidos. | O usuário cria uma categoria personalizada. | A categoria é salva, identificada como personalizada e aparece na consulta. |
| RF02-04 | A categoria personalizada Curso Demo, ainda sem transações. | O usuário muda seu nome para Educação Demo. | A consulta mostra o novo nome e o ID da categoria é preservado. |
| RF02-05 | Categoria Temporária Demo personalizada e sem transações vinculadas. | O usuário solicita sua exclusão. | A exclusão é permitida e a categoria deixa de aparecer na consulta. |
| RF02-06 | Uma categoria padrão, como Transporte. | O usuário solicita sua exclusão. | A exclusão é bloqueada com mensagem clara e a categoria permanece disponível. |
| RF02-07 | Educação Demo personalizada e vinculada à transação D4. | O usuário solicita sua exclusão. | A exclusão é bloqueada; a categoria e a transação permanecem válidas. |
| RF02-08 | Uma categoria válida disponível. | O usuário cadastra e consulta uma transação associada a ela. | A associação é persistida e a resposta da transação identifica a categoria correta, atendendo à integração da Issue #18. |

### RF03 — Relatórios, entrega parcial

| ID | Dado | Quando | Então |
|---|---|---|---|
| RF03-01 | D1–D4 cadastrados e nenhuma outra transação em setembro de 2026. | É solicitado o relatório de `2026-09`. | A resposta apresenta receitas de R$ 3.000,00, despesas de R$ 600,00 e saldo de R$ 2.400,00. |
| RF03-02 | A mesma massa base. | É consultada a distribuição das despesas por categoria. | Alimentação soma R$ 200,00, Transporte R$ 100,00 e Educação Demo R$ 300,00; percentuais correspondem ao denominador de R$ 600,00. |
| RF03-03 | Massa base de setembro e T2 registrada em agosto de 2026. | É solicitado o relatório de setembro. | T2 é excluída do período; receitas de setembro permanecem R$ 3.000,00. Ao consultar agosto, T2 é incluída naquele mês. |
| RF03-04 | Nenhuma transação em julho de 2026 na instância de teste. | É solicitado o relatório de `2026-07`. | Totais e saldo são zero; a distribuição não contém despesas e não há erro de divisão por zero. |
| RF03-05 | Parâmetro de mês inválido, como `2026-13` ou `abc`. | É solicitado o relatório. | A requisição é rejeitada com mensagem clara, sem apresentar um relatório enganoso ou erro interno não tratado. |
| RF03-06 | Ambiente Docker pronto e massa de teste registrada. | É medido o intervalo entre o envio da requisição mensal e o recebimento completo da resposta. | O carregamento ocorre em menos de 3 segundos; duração, ambiente e quantidade de registros utilizados são registrados como evidência do RNF02. |

## 5. Registro de execução e aceite

Todos os IDs de RF01-01 a RF01-11, RF02-01 a RF02-08 e RF03-01 a RF03-06 começam com a situação **Não executado — validação pendente**. A dupla de desenvolvimento deverá registrar uma linha por cenário após sua execução:

| ID do cenário | Data e executor | Ambiente/versão | Resultado observado | Evidência | Situação |
|---|---|---|---|---|---|
| A preencher após executar o cenário | A preencher | A preencher | A preencher | A preencher | Não executado — validação pendente |

Usar as situações **Aprovado**, **Reprovado** ou **Bloqueado**, mantendo a situação inicial para os cenários ainda não executados. Registrar falhas e impedimentos nas Issues técnicas correspondentes. Sara valida o aceite com base nos resultados; a publicação deste documento não encerra automaticamente nenhuma Issue.

Os cenários de compatibilidade dos fluxos demonstrados devem identificar Chrome, Firefox ou Safari e sua versão, conforme RNF04. A medição de usabilidade e desempenho não deve ser inferida apenas do sucesso de um teste funcional.

## 6. Roteiro de demonstração — cinco minutos

A verificação completa dos casos limite ocorre no ensaio. A apresentação ao vivo seleciona os fluxos abaixo, com comandos e dados preparados previamente. Sara apresenta o escopo e valida os resultados; Giovana opera categorias/transações; Enzo apresenta o relatório mensal.

| Tempo | Duração | Responsável | Ação | Resultado esperado |
|---|---|---|---|---|
| 00:00–00:45 | 45 s | Sara e Giovana | Apresentar RF01/RF02 e RF03 parcial; mostrar categorias padrão; criar Curso Demo e renomear para Educação Demo pela API. Se a massa estiver pré-carregada, mostrar Educação Demo existente e demonstrar CRUD com Categoria Temporária Demo. | Categorias padrão disponíveis e categoria personalizada pronta para D4, sem duplicação da categoria base. |
| 00:45–02:00 | 75 s | Giovana | Cadastrar D1–D4 e consultar a listagem. | Receita e despesas aparecem com as categorias e datas corretas. |
| 02:00–02:45 | 45 s | Giovana | Demonstrar rejeição de valor zero e de data futura; explicar a permissão de registros retroativos. | Mensagens compreensíveis e nenhum registro inválido persistido. |
| 02:45–03:30 | 45 s | Giovana | Cadastrar T1 por R$ 20,00, editar para R$ 50,00 e excluir; mostrar o bloqueio da exclusão de Educação Demo com D4 vinculada. | CRUD demonstrado, integridade de categorias preservada e massa base recomposta. |
| 03:30–04:45 | 75 s | Enzo | Consultar o endpoint mensal planejado para setembro de 2026 e conferir os valores com a tabela de resultados esperados. | Receitas R$ 3.000,00, despesas R$ 600,00, saldo R$ 2.400,00 e distribuição correta. |
| 04:45–05:00 | 15 s | Sara | Encerrar e indicar as entregas restantes da rodada 3. | RF03 completo, RF04 e RF05 identificados como próximos passos, sem apresentá-los como entregues. |

Se D1–D4 tiverem sido pré-carregados para agilizar a apresentação, identificá-los como dados preparados e demonstrar o cadastro com T1, sem duplicar a massa base. O critério de totais exige apenas uma ocorrência de D1–D4.

## 7. Dependências e pendências do ensaio

- **RF03:** aguardar a entrega das Issues #19/#20 e confirmar o contrato da resposta com Enzo. Os critérios descrevem informações esperadas, sem impor nomes de campos JSON ainda não confirmados.
- **Ambiente e schema:** validar as entregas das Issues [#14](https://github.com/ArbenTafili/finapp_Uff/issues/14) e [#15](https://github.com/ArbenTafili/finapp_Uff/issues/15), preservando os valores monetários e a execução local.
- **Preparação e duração:** Enzo e Giovana ensaiam o roteiro na Issue #29, conferem os IDs, a massa base e o tempo de cada etapa.
- **Contingência:** definir o plano de resposta a falhas de Docker na Issue #29, em alinhamento com a [análise de riscos da Issue #26](https://github.com/ArbenTafili/finapp_Uff/issues/26). Se o ambiente ou um endpoint estiver indisponível, registrar o impedimento e não apresentar o cenário como validado.
- **Participação e esforço:** registrar horas previstas e realizadas e encaminhar a Filipe. Os valores ainda não informados permanecem pendentes; não são presumidos como zero.

## 8. Cobertura da Issue #25

| Critério da Issue | Evidência documental |
|---|---|
| Funcionalidades da Demo v2 definidas | Escopo e histórias de usuário. |
| Critérios de aceite registrados | Cenários Dado/Quando/Então de RF01, RF02 e RF03 parcial. |
| Ordem da demonstração definida | Roteiro cronometrado de cinco minutos. |
| Dados/cenários necessários identificados | Massa D1–D4, registros temporários e casos limite. |
| Resultado esperado de cada etapa documentado | Tabelas de resultados, cenários e roteiro. |
| Roteiro disponibilizado para preparação do ensaio | Documento publicado na branch `feature/sara-aceite-demo-v2`, vinculado à Issue #25 e à preparação da Issue #29. |
