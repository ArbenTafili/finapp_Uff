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

O [backlog da Issue #24](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-backlog-rodada2/docs/rodada2/01-backlog.md) define a priorização. A persistência está no [RDT-01 canônico](../adr/RDT-01.md), com [complementação da Sara](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-rdt01/docs/adr/RDT-01.md). A regra `data <= hoje` segue o [ADR 001](../rodada1/10-decisoes-tecnicas.md).

**Situação de integração:** RF03 está na main pelos PRs #36/#37. O [PR #39 da Giovana](https://github.com/ArbenTafili/finapp_Uff/pull/39) entrou na develop; os complementos de #18, #16 e #21 estão nas branches técnicas correspondentes. A versão integrada para o ensaio ainda deve receber essas entregas. As regras do [ADR 002](https://github.com/ArbenTafili/finapp_Uff/blob/develop/docs/rodada1/10-decisoes-tecnicas.md) permanecem propostas e com aceite pendente.

## 2. Preparação para execução

1. Utilizar uma instância local dedicada à demonstração, sem outras transações em setembro de 2026. Não apagar dados de uso real para preparar os cenários.
2. Preparar o ambiente conforme a [preparação técnica da Issue #29](https://github.com/ArbenTafili/finapp_Uff/blob/docs/29-alinhamento-demo-v2/docs/rodada2/05-preparacao-demo-v2.md). Para cadastro ao vivo, iniciar `docker compose -p finapp-demo up --build`. Para dados preparados, subir apenas o serviço `db` desse projeto e executar a aplicação com `dev,demo`, sem iniciar uma segunda aplicação na mesma porta.
3. Conferir as categorias em `GET /api/categorias`. Usar os IDs retornados, sem presumir números fixos. Se um registro da massa de demonstração já existir, conferir seu conteúdo antes de cadastrá-lo novamente.
4. Reservar a categoria personalizada `Curso Demo`, do tipo `DESPESA`, para criação e alteração para `Educação Demo` antes de associar transações. Fazer isso uma única vez, na preparação ou durante a apresentação. Se os dados já estiverem pré-carregados, reutilizar o ID de Educação Demo; demonstrar o CRUD de categoria com `Categoria Temporária Demo`, sem vínculos, para evitar duplicações.
5. Usar a interface em `http://localhost:8080/` para os fluxos de transações disponíveis. Para operações de categoria sem tela disponível, demonstrar a API com `curl` no terminal e comandos preparados pela dupla de desenvolvimento.
6. Demonstrar RF03 pelo endpoint implementado `GET /api/relatorios?mes=2026-09`, abrindo a resposta no navegador. A disponibilidade e os resultados no ambiente do ensaio devem ser registrados, mesmo com o código já integrado.
7. Separar os IDs dos registros temporários para editá-los ou excluí-los sem afetar outras transações. A massa base deve estar recomposta antes da conferência final do relatório.

Os cenários abaixo definem resultados esperados. Não afirmam que os endpoints ou regras já passaram nos testes.

### Contrato do relatório mensal

`GET /api/relatorios?mes=AAAA-MM` retorna HTTP 200 para um mês válido, inclusive quando não existem transações.

| Campo | Conteúdo esperado para a massa base de setembro |
|---|---|
| `mes` | `2026-09` |
| `mesAnterior` / `proximoMes` | `2026-08` / `2026-10`; informações da API, sem interface de navegação nesta rodada. |
| `totalReceitas` / `totalDespesas` / `saldo` | 3000.00 / 600.00 / 2400.00 |
| `despesasPorCategoria` | Lista de despesas, cada uma com `categoriaId`, `categoria`, `valor` e `percentual`; IDs obtidos na execução. |

Em mês vazio, os três totais são zero e a lista é vazia. Mês ausente, em branco ou fora do formato válido retorna HTTP 400, com `erro`, `mensagem` e `detalhes.mes` no corpo padronizado. Valores financeiros usam BigDecimal; percentuais são arredondados a duas casas decimais.

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
| RF01-12 | Tipo RECEITA com categoria Alimentação (DESPESA), ou edição que torne tipo e categoria incompatíveis. | O usuário cadastra ou edita a transação. | HTTP 422 com mensagem clara; nenhum registro incompatível é criado ou alterado. Depende da integração da Issue #18. |
| RF01-13 | Valor de entrada 10.999 e, separadamente, 10.5. | O usuário solicita cadastro ou edição. | 10.999 retorna HTTP 400; 10.5 é aceito como 10.50, sem arredondamento silencioso. Depende da Issue #16. |
| RF01-14 | Apenas D1–D4 presentes na instância, sem T1/T2 ou registros dos demais cenários. | É solicitado `GET /api/transacoes/saldo`. | Totais globais de receitas 3000.00, despesas 600.00 e saldo 2400.00. Esse endpoint não é mensal e depende da Issue #16. |
| RF01-15 | Dados válidos com descrição em branco ou com espaços nas extremidades. | O usuário cadastra a transação. | Descrição em branco resulta em null; espaços externos são removidos, preservando o texto. Depende da Issue #16. |
| RF01-16 | JSON malformado, tipo/data inválidos ou ID não numérico na URL, em tentativas separadas. | A requisição é enviada. | HTTP 400 com resposta de erro padronizada e sem criação/alteração de dados. Depende da Issue #16. |

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
| RF02-09 | Educação Demo já cadastrada como DESPESA. | O usuário tenta criar ou renomear outra categoria DESPESA para o mesmo nome, inclusive com maiúsculas diferentes. | HTTP 409; nenhuma duplicação no mesmo tipo. O mesmo nome em outro tipo é permitido. Depende da integração da Issue #17. |
| RF02-10 | Transporte é uma categoria padrão. | O usuário tenta alterar seu nome ou tipo. | HTTP 422 e os dados originais são preservados. Depende da integração da Issue #17. |
| RF02-11 | Educação Demo é DESPESA e está vinculada a D4. | O usuário tenta mudar seu tipo para RECEITA. | HTTP 422; a categoria e a transação mantêm seus tipos originais. Depende da integração da Issue #17. |
| RF02-12 | Instância de teste dedicada com categoria personalizada, mas sem todas as categorias padrão. | A aplicação inicia e depois é reiniciada. | As oito categorias padrão ficam disponíveis, sem duplicação por nome/tipo e sem apagar a personalizada. Depende da integração da Issue #17. |

### RF03 — Relatórios, entrega parcial

| ID | Dado | Quando | Então |
|---|---|---|---|
| RF03-01 | D1–D4 cadastrados e nenhuma outra transação em setembro de 2026. | É solicitado o relatório de `2026-09`. | HTTP 200; receitas de R$ 3.000,00, despesas de R$ 600,00, saldo de R$ 2.400,00 e meses vizinhos 2026-08/2026-10. |
| RF03-02 | A mesma massa base. | É consultada a distribuição das despesas por categoria. | Alimentação soma R$ 200,00, Transporte R$ 100,00 e Educação Demo R$ 300,00; percentuais correspondem ao denominador de R$ 600,00. |
| RF03-03 | Massa base de setembro e T2 registrada em agosto de 2026. | É solicitado o relatório de setembro. | T2 é excluída do período; receitas de setembro permanecem R$ 3.000,00. Ao consultar agosto, T2 é incluída naquele mês. |
| RF03-04 | Nenhuma transação em julho de 2026 na instância de teste. | É solicitado o relatório de `2026-07`. | Totais e saldo são zero; a distribuição não contém despesas e não há erro de divisão por zero. |
| RF03-05 | Mês inválido (`2026-13`, `abc`), em branco ou ausente, em tentativas separadas. | É solicitado o relatório. | HTTP 400 com mensagem clara e `detalhes.mes`, sem relatório enganoso ou erro interno. |
| RF03-06 | Ambiente Docker pronto e massa de teste registrada. | É medido o intervalo entre o envio da requisição mensal e o recebimento completo da resposta. | O carregamento ocorre em menos de 3 segundos; duração, ambiente e quantidade de registros utilizados são registrados como evidência do RNF02. |

## 5. Registro de execução e aceite

Os 34 cenários (RF01-01 a RF01-16, RF02-01 a RF02-12 e RF03-01 a RF03-06) começam como **Não executado — validação pendente**. Enzo e Giovana registram os resultados na tabela abaixo. Cada evidência deve identificar o commit da versão testada; resultados em uma branch técnica não comprovam aceite na versão integrada da demo.

| ID do cenário | Data e executor | Ambiente/versão | Resultado observado | Evidência | Situação |
|---|---|---|---|---|---|
| RF01-01 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-02 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-03 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-04 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-05 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-06 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-07 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-08 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-09 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-10 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-11 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-12 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-13 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-14 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-15 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF01-16 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-01 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-02 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-03 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-04 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-05 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-06 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-07 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-08 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-09 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-10 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-11 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF02-12 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF03-01 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF03-02 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF03-03 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF03-04 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF03-05 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |
| RF03-06 | A registrar | A registrar | A registrar | A registrar | Não executado — validação pendente |

Usar **Aprovado**, **Reprovado** ou **Bloqueado**, mantendo a situação inicial para cenários não executados. Se faltar a integração de uma entrega necessária, registrar o bloqueio e a Issue correspondente. Sara valida o aceite com base nos resultados. O aceite deste documento é separado do aceite funcional e não encerra automaticamente as Issues técnicas.

O RNF04 exige conferir os fluxos demonstrados nos três navegadores:

| Navegador | Versão e sistema operacional | Fluxos e evidência | Situação |
|---|---|---|---|
| Chrome | A registrar | A registrar | Não executado — validação pendente |
| Firefox | A registrar | A registrar | Não executado — validação pendente |
| Safari | A registrar | A registrar | Não executado — validação pendente |

Registrar RNF01 e RNF02 com duração observada, ambiente e volume de dados. A existência de testes automatizados não substitui o registro de seus resultados nem a medição no ambiente da demo.

## 6. Roteiro de demonstração — cinco minutos

A verificação completa dos casos limite ocorre no ensaio. A apresentação ao vivo seleciona os fluxos abaixo, com comandos e dados preparados previamente. Sara apresenta o escopo e valida os resultados; Giovana opera categorias/transações; Enzo apresenta o relatório mensal.

| Tempo | Duração | Responsável | Ação | Resultado esperado |
|---|---|---|---|---|
| 00:00–00:45 | 45 s | Sara e Giovana | Apresentar RF01/RF02 e RF03 parcial; mostrar categorias padrão; criar Curso Demo e renomear para Educação Demo pela API. Se a massa estiver pré-carregada, mostrar Educação Demo existente e demonstrar CRUD com Categoria Temporária Demo. | Categorias padrão disponíveis e categoria personalizada pronta para D4, sem duplicação da categoria base. |
| 00:45–02:00 | 75 s | Giovana | Cadastrar D1–D4 e consultar a listagem. Com massa pré-carregada, mostrar D1–D4 existentes e cadastrar somente T1. | Massa base sem duplicação; o cadastro é demonstrado ao vivo. |
| 02:00–02:45 | 45 s | Giovana | Demonstrar rejeição de valor zero e de data futura; explicar a permissão de registros retroativos. | Mensagens compreensíveis e nenhum registro inválido persistido. |
| 02:45–03:30 | 45 s | Giovana | Cadastrar T1 por R$ 20,00 se ainda não existir; editar para R$ 50,00 e excluir; mostrar o bloqueio da exclusão de Educação Demo com D4 vinculada. | CRUD demonstrado, integridade de categorias preservada e massa base recomposta. |
| 03:30–04:45 | 75 s | Enzo | Consultar o endpoint mensal implementado para setembro de 2026 e conferir os valores com a tabela de resultados esperados. | Receitas R$ 3.000,00, despesas R$ 600,00, saldo R$ 2.400,00 e distribuição correta. |
| 04:45–05:00 | 15 s | Sara | Encerrar e indicar as entregas restantes da rodada 3. | RF03 completo, RF04 e RF05 identificados como próximos passos, sem apresentá-los como entregues. |

Se D1–D4 tiverem sido pré-carregados para agilizar a apresentação, identificá-los como dados preparados e demonstrar o cadastro com T1, sem duplicar a massa base. O critério de totais exige apenas uma ocorrência de D1–D4.

## 7. Dependências e pendências do ensaio

- **Integração de RF01/RF02:** coordenar com Arben e Giovana a integração à main de #17 → #18 → #16 → #21. O PR #39 entrou na develop; os cenários das novas regras permanecem pendentes de integração e execução na versão da demo.
- **RF03:** código e contrato das Issues #19/#20 já integrados; executar os cenários e registrar resultados e desempenho no ambiente da demonstração.
- **Ambiente e schema:** validar as entregas das Issues [#14](https://github.com/ArbenTafili/finapp_Uff/issues/14) e [#15](https://github.com/ArbenTafili/finapp_Uff/issues/15), preservando os valores monetários e a execução local.
- **Preparação e duração:** Enzo e Giovana ensaiam o roteiro na Issue #29, conferem os IDs, a massa base e o tempo de cada etapa.
- **Contingência:** ensaiar o plano documentado na Issue #29 (H2, máquina alternativa e gravação), em alinhamento com a [análise de riscos da Issue #26](https://github.com/ArbenTafili/finapp_Uff/issues/26). Registrar falhas e impedimentos sem declarar o cenário validado.
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
