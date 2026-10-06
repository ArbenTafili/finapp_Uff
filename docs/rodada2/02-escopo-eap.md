# Escopo do Produto e EAP — Revisão da Rodada 2 — FinApp

**Responsável:** Sara Marcomini (Product Owner)

**Situação:** escopo revisado; integração e aceite das funcionalidades conforme o backlog.

Esta revisão atualiza a seção 2 do Plano de Projeto para a rodada 2. O [documento da rodada 1](../rodada1/02-escopo-eap.md) permanece como referência histórica. O CRUD de transações e categorias da Demo v1 é o ponto de partida informado pela equipe; as entregas desta rodada dependem de validação de aceite. A distribuição revisada está detalhada no [backlog](01-backlog.md).

**Issue da revisão:** [#24 — Revisar backlog e escopo da Iteração 2](https://github.com/ArbenTafili/finapp_Uff/issues/24).

## Escopo do Produto (requisitos)

### RF01 — Gerenciar Transações
Permitir que o usuário registre receitas e despesas informando valor, tipo (receita/despesa), categoria, data e descrição opcional. O sistema deve permitir também edição e exclusão das transações cadastradas.

Para a validação da data, será adotada a regra de que a transação pode possuir data passada ou a data atual, mas **não pode possuir data futura** (`data <= hoje`). Essa interpretação preserva a possibilidade de registrar despesas retroativas sem introduzir autenticação ou entidade de usuário fora do escopo atual.

### RF02 — Gerenciar Categorias
Disponibilizar categorias padrão pré-carregadas e permitir que o usuário crie, edite e exclua categorias personalizadas.

As regras propostas no [ADR 002 da Giovana](../rodada1/10-decisoes-tecnicas.md) detalham seed idempotente, nome único por tipo sem distinção entre maiúsculas/minúsculas, categorias padrão imutáveis e bloqueio de exclusão ou mudança de tipo de categoria vinculada. A integração exige tipo da transação igual ao da categoria. As regras e os complementos de integração/transações foram consolidados na main pelo PR #43. O aceite funcional e a aprovação formal do ADR 002, ainda Proposto, permanecem pendentes.

### RF03 — Visualizar Relatórios
Permitir a visualização de relatórios mensais contendo total de receitas, total de despesas, distribuição de gastos por categoria e navegação entre meses.

### RF04 — Gerenciar Metas de Economia
Permitir que o usuário crie metas de economia com valor alvo e prazo, acompanhe visualmente seu progresso e possa editar ou excluir/cancelar metas existentes.

### RF05 — Exportar Dados
Permitir a exportação das transações para arquivo CSV, com seleção de intervalo de datas e colunas padronizadas.

### Requisitos não funcionais relacionados

- **RNF01 — Usabilidade:** o registro de uma transação deve poder ser realizado em menos de 30 segundos.
- **RNF02 — Desempenho:** o carregamento dos relatórios deve ocorrer em menos de 3 segundos.
- **RNF03 — Segurança/Privacidade:** Os dados devem ser armazenados em banco de dados executado localmente (Docker), sem envio a serviços externos.
- **RNF04 — Compatibilidade:** a interface web deve ser compatível com Chrome, Firefox e Safari.
- **RNF05 — Confiabilidade:** os cálculos financeiros devem preservar precisão e consistência.

## Limites de escopo

O desenvolvimento desta versão está limitado aos requisitos RF01 a RF05 e RNF01 a RNF05.

Não fazem parte desta EAP:

- autenticação e gerenciamento de contas de usuário;
- sincronização automática com contas bancárias;
- controle de investimentos;
- gestão avançada de cartões de crédito;
- planejamento orçamentário futuro detalhado;
- gestão de empréstimos ou dívidas;
- aplicativos móveis nativos.

A ausência de autenticação é intencional nesta fase. Não será criada uma entidade `Usuario` apenas para suportar regras que podem ser atendidas sem expansão do escopo.

---

> Este artefato define o escopo funcional do FinApp e decompõe o trabalho do projeto em pacotes técnicos identificáveis.
> Os IDs da EAP devem ser reutilizados nos artefatos de estimativas, cronograma, monitoramento e registro de participação, garantindo rastreabilidade entre planejamento e execução.


## Escopo do Projeto (EAP)

A EAP foi organizada de forma hierárquica para permitir que seus pacotes sejam utilizados diretamente nas estimativas de Planning Poker, no cronograma, no acompanhamento de horas e nas métricas de monitoramento do projeto.

### 1. Planejamento, configuração e baseline da Rodada 1

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **1.1** | Configuração do Repositório e CI/CD | Preparar o repositório Git/GitHub, estratégia de branches, organização de configuração e base para integração contínua e execução do projeto. | Projeto |
| **1.2** | Refinamento do Backlog e User Stories | Revisar RF01–RF05, critérios de aceite, prioridades e transformar os requisitos em itens executáveis de backlog. | RF01–RF05 |
| **1.3** | Planejamento da Rodada e Facilitação | Organizar a primeira rodada, dependências, cerimônias, distribuição inicial de trabalho e preparação para Planning Poker. | Projeto |
| **1.4** | Estruturação de Classes e Entidades | Traduzir o modelo existente para a estrutura técnica do código, definindo entidades, responsabilidades e relacionamentos necessários à implementação. Não inclui autenticação ou entidade `Usuario` fora do escopo. | RF01–RF05 |
| **1.5** | Implementação da Lógica de Datas e Regras | Implementar as regras iniciais de validação de transações, incluindo `valor > 0` e bloqueio de datas futuras (`data <= hoje`). | RF01 / RNF01 / RNF05 |
| **1.6** | Gestão de Baseline e Documentação de GP | Consolidar a baseline inicial de escopo, cronograma e esforço para permitir acompanhamento de desvios ao longo das rodadas. | Projeto |

### 2. Arquitetura e infraestrutura de implementação

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **2.1** | Estrutura da Aplicação Spring Boot | Organizar a aplicação Kotlin/Spring Boot em camadas coerentes de domínio, serviço, persistência e interface/API. | Base técnica |
| **2.2** | Persistência com JPA/PostgreSQL e H2 | Utilizar JPA com PostgreSQL local via Docker nos perfis docker/dev; manter H2 no perfil h2 para desenvolvimento sem Docker e contingência da demo. Testes usam PostgreSQL isolado via Testcontainers. Schema versionado por Flyway. Decisão no [RDT-01](../adr/RDT-01.md), Issue #13. | RNF03 / RNF05 |
| **2.3** | Validação de Dados | Centralizar validações de entrada e regras de domínio utilizando os recursos de validação da aplicação. | RF01–RF05 / RNF05 |
| **2.4** | Execução Containerizada | Manter a execução reproduzível da aplicação por Docker/Docker Compose e documentar o processo de inicialização. | Base técnica |
| **2.5** | Estrutura de Testes Automatizados | Preparar a base de testes unitários e de integração para os serviços e regras do domínio. | RNF02 / RNF05 |

### 3. Implementação dos requisitos funcionais

#### 3.1 RF01 — Gerenciar Transações

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **3.1.1** | Persistência e Serviço de Transações | Implementar armazenamento, consulta e regras de negócio da entidade `Transacao`. | RF01 |
| **3.1.2** | Cadastro de Transações | Implementar o fluxo de inclusão de receita ou despesa com valor, tipo, categoria, data e descrição opcional. | RF01 / RNF01 |
| **3.1.3** | Validações de Transação | Integrar as regras definidas em 1.5 ao fluxo de cadastro e edição, incluindo valor válido e proibição de data futura. | RF01 / RNF01 / RNF05 |
| **3.1.4** | Listagem de Transações | Exibir as transações cadastradas com as informações necessárias para acompanhamento financeiro. | RF01 |
| **3.1.5** | Edição de Transações | Permitir a alteração de uma transação existente, reaplicando as regras de validação. | RF01 |
| **3.1.6** | Exclusão de Transações | Permitir a remoção de uma transação e atualizar os dados derivados afetados. | RF01 |
| **3.1.7** | Cálculo de Saldo | Calcular saldo a partir das receitas e despesas registradas com consistência financeira. | RF01 / RNF05 |

#### 3.2 RF02 — Gerenciar Categorias

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **3.2.1** | Persistência e Serviço de Categorias | Implementar armazenamento e regras da entidade `Categoria`. | RF02 |
| **3.2.2** | Categorias Padrão | Disponibilizar categorias padrão pré-carregadas para uso imediato. | RF02 / RNF01 |
| **3.2.3** | Cadastro de Categoria Personalizada | Permitir criação de novas categorias pelo usuário. | RF02 |
| **3.2.4** | Edição de Categoria | Permitir alteração de categorias personalizadas. | RF02 |
| **3.2.5** | Exclusão de Categoria | Permitir exclusão de categorias personalizadas respeitando vínculos existentes. | RF02 / RF01 |
| **3.2.6** | Integração Categoria x Transação | Disponibilizar categorias válidas nos fluxos de cadastro e edição de transações. | RF01 / RF02 |

#### 3.3 RF03 — Visualizar Relatórios

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **3.3.1** | Consulta Mensal de Transações | Recuperar as transações correspondentes ao mês selecionado. | RF03 |
| **3.3.2** | Totalização de Receitas e Despesas | Calcular os totais financeiros do período selecionado. | RF03 / RNF05 |
| **3.3.3** | Agrupamento por Categoria | Consolidar despesas por categoria para uso nos relatórios. | RF03 / RNF05 |
| **3.3.4** | Visualização do Relatório | Implementar a apresentação dos totais e informações mensais. | RF03 |
| **3.3.5** | Gráfico por Categoria | Implementar gráfico de pizza ou barras com a proporção dos gastos. | RF03 |
| **3.3.6** | Navegação entre Meses | Permitir consulta a meses anteriores e posteriores. | RF03 |
| **3.3.7** | Otimização de Relatórios | Medir e ajustar a consulta/geração do relatório para atender ao limite de 3 segundos. | RF03 / RNF02 |

#### 3.4 RF04 — Gerenciar Metas de Economia

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **3.4.1** | Persistência e Serviço de Metas | Implementar armazenamento e regras da entidade `Meta`. | RF04 |
| **3.4.2** | Cadastro de Metas | Permitir a criação de meta com valor alvo e prazo. | RF04 |
| **3.4.3** | Listagem de Metas | Exibir metas e seus principais dados de acompanhamento. | RF04 |
| **3.4.4** | Edição de Metas | Permitir alteração dos dados editáveis de uma meta. | RF04 |
| **3.4.5** | Exclusão/Cancelamento de Metas | Permitir encerramento da meta conforme regras do domínio. | RF04 |
| **3.4.6** | Cálculo de Progresso | Calcular valor economizado em relação ao valor alvo. | RF04 / RNF05 |
| **3.4.7** | Barra de Progresso | Apresentar visualmente o percentual de evolução da meta. | RF04 |
| **3.4.8** | Controle de Status | Tratar os estados relevantes da meta, como Em Andamento, Concluída, Vencida e Cancelada. | RF04 |
| **3.4.9** | Integração Transações x Metas | Integrar o progresso das metas ao fluxo financeiro adotado pelo sistema, conforme as regras definidas para o domínio. | RF01 / RF04 / RNF05 |

#### 3.5 RF05 — Exportar Dados

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **3.5.1** | Seleção de Período | Permitir definição de data inicial e final para exportação. | RF05 |
| **3.5.2** | Consulta de Dados para Exportação | Recuperar as transações pertencentes ao intervalo selecionado. | RF05 |
| **3.5.3** | Geração de CSV | Implementar o componente de exportação com colunas padronizadas. | RF05 |
| **3.5.4** | Disponibilização do Arquivo | Permitir que o arquivo CSV gerado seja obtido pelo usuário. | RF05 |
| **3.5.5** | Validação do CSV | Validar conteúdo, período, caracteres e abertura do arquivo em ferramentas de planilha. | RF05 / RNF05 |

### 4. Qualidade, integração e demonstrações

| ID EAP | Pacote de Trabalho | Descrição | Requisito relacionado |
|---|---|---|---|
| **4.1** | Testes das Regras Financeiras | Validar saldo, totais, agrupamentos, progresso de metas e demais cálculos do domínio. | RNF05 |
| **4.2** | Testes de CRUD e Integração | Validar os fluxos de transações, categorias, metas e suas relações. | RF01 / RF02 / RF04 |
| **4.3** | Testes de Relatórios | Validar resultados, navegação mensal e limite de desempenho. | RF03 / RNF02 / RNF05 |
| **4.4** | Testes de Exportação | Validar filtro de período e conteúdo do CSV. | RF05 / RNF05 |
| **4.5** | Validação de Usabilidade | Verificar se o fluxo principal de cadastro atende ao objetivo de menos de 30 segundos. | RNF01 |
| **4.6** | Validação de Compatibilidade | Testar a aplicação nos navegadores Chrome, Firefox e Safari. | RNF04 |
| **4.7** | Integração da Demo v1 | Preparar uma primeira demonstração funcional com os fluxos disponíveis na Rodada 1. | Projeto |
| **4.8** | Integração da Demo v2 | Consolidar os recursos implementados na Rodada 2 em um cenário integrado de demonstração. | Projeto |
| **4.9** | Estabilização e Demo v3 | Corrigir falhas críticas, preparar dados de demonstração e executar o fluxo final integrado do produto. | RF01–RF05 / RNF01–RNF05 |

---

## Dependências principais entre os pacotes

As dependências abaixo orientam o cronograma e permitem que o Gráfico de Gantt represente precedências, paralelismo e possíveis gargalos.

1. **1.1, 1.2 e 1.3** estabelecem as condições de execução e planejamento da primeira rodada.
2. **1.4 e 2.1–2.3** antecedem a maior parte da implementação funcional.
3. **3.2.1–3.2.2** devem estar disponíveis antes da conclusão do cadastro de transações, pois RF01 depende de categorias.
4. **3.1** fornece os dados utilizados por **3.3 (Relatórios)** e **3.5 (Exportação)**.
5. **3.4.9 (Integração Transações x Metas)** depende dos serviços de transações e metas já estarem funcionais.
6. Os testes de **4.1–4.6** acompanham a implementação incrementalmente e não devem ficar concentrados apenas no final.
7. **4.7, 4.8 e 4.9** representam marcos demonstráveis ao final de cada rodada.

## Distribuição macro por Rodada

Esta distribuição revisa o planejamento por rodada conforme a divisão de trabalho da equipe. Os pacotes mantêm seus IDs; sua inclusão na tabela indica planejamento, não comprovação de conclusão.

| Rodada | Objetivo | Pacotes prioritários | Marco |
|---|---|---|---|
| **Rodada 1** | Planejamento, configuração, arquitetura e primeiro fluxo funcional | 1.1–1.6, 2.1–2.5, início de 3.1 e 3.2, 4.7 | **Planejamento + Demo v1** |
| **Rodada 2** | Concluir RF01/RF02 e entregar RF03 parcial: consultas e endpoint mensal | conclusão de 3.1 e 3.2; 3.3.1–3.3.3, com medição de desempenho vinculada a 3.3.7; testes correspondentes aos fluxos disponíveis; 4.8 | **Execução + Configuração + Demo v2** |
| **Rodada 3** | Concluir RF03, implementar RF04/RF05, corrigir falhas e estabilizar | 3.3.4–3.3.6 e conclusão de 3.3.7; 3.4; 3.5; correções de 3.1/3.2 conforme necessário; conclusão de 4.1–4.6 e 4.9 | **Entrega final + Demo v3** |

### Justificativas da revisão

O início de 3.4 (RF04 — Metas), anteriormente previsto para a rodada 2, foi transferido para a rodada 3 para priorizar a conclusão de RF01/RF02 e a entrega parcial de RF03. Essa alteração reorganiza as entregas por rodada e preserva RF04 no escopo total do produto.

Na rodada 2, RF03 contempla consultas e endpoint mensal com total de receitas, total de despesas, saldo e distribuição de despesas por categoria, integrados à main pelos PRs #36/#37. O endpoint também informa os meses vizinhos. A interface completa, os gráficos e a navegação visual ficam para a rodada 3. O limite de desempenho do RNF02 permanece como requisito; os testes existentes e a medição no ambiente da demo devem ter resultados registrados para o aceite.

O RNF03 recebe a redação acordada para explicitar a execução local do banco via Docker e a ausência de envio de dados a serviços externos. A escolha está no [RDT-01 canônico](../adr/RDT-01.md), Issue #13, com [complementação da Sara em branch](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-rdt01/docs/adr/RDT-01.md). A data e os aprovadores da decisão permanecem a confirmar. O [ADR 001](../rodada1/10-decisoes-tecnicas.md), relativo a `data <= hoje`, permanece independente e preservado.

### Rastreabilidade e pendências de impacto

- **Issue de backlog e escopo:** [#24](https://github.com/ArbenTafili/finapp_Uff/issues/24), com os vínculos às tarefas técnicas apresentados no backlog.
- **Impacto em horas, prazo detalhado e custo:** a confirmar com Filipe, considerando a transferência de metas e a formalização do RDT-01. Nenhuma nova estimativa é estabelecida nesta revisão.
- **RDT-01:** registro inicial integrado pelo Arben na [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13), com complementação da Sara em branch; confirmar data, aprovação e histórico das alternativas.
- **Critérios de aceite e roteiro da Demo v2:** entrega da [Issue #25](https://github.com/ArbenTafili/finapp_Uff/issues/25); ensaio e preparação técnica da demonstração vinculados à [Issue #29](https://github.com/ArbenTafili/finapp_Uff/issues/29).
- **Situação das entregas:** infraestrutura, RF03 parcial e complementos de RF01/RF02/testes integrados à main; entregas da Giovana consolidadas pelo PR #43. A situação detalhada está no backlog; a conclusão exige integração, evidências e aceite de Sara.

## Relação com os demais artefatos do projeto

A presente EAP é a referência de decomposição do escopo e deve alimentar os demais documentos de gestão:

- **Estimativas de Esforço (Planning Poker):** cada pacote estimável desta EAP recebe pontuação relativa pela equipe.
- **Cronograma (Gantt):** utiliza os IDs, dependências e distribuição por rodada definidos aqui.
- **Orçamento:** utiliza a baseline de esforço/custo derivada das estimativas e do cronograma.
- **Monitoramento e Controle:** acompanha o progresso da baseline por Burndown e EVM.
- **Registro de Participação:** associa horas previstas e realizadas aos IDs desta EAP.
- **Registro de Decisões Técnicas (ADRs):** documenta decisões que alterem ou esclareçam a execução dos pacotes da EAP.

## Apoio à demonstração

O roteiro recomendado para as demos deve priorizar um cenário único e integrado:

1. exibir categorias padrão e criar uma categoria personalizada;
2. registrar receitas e despesas;
3. visualizar o saldo e o histórico;
4. consultar o relatório mensal e a distribuição por categoria;
5. criar/acompanhar uma meta de economia;
6. exportar as transações do período em CSV.

Na Demo v2, o escopo é limitado às etapas 1–4, com RF01/RF02 e a apresentação de RF03 parcial pelas consultas e pelo endpoint mensal. Metas e exportação CSV ficam fora dessa demonstração. O roteiro de cinco minutos e os critérios de aceite estão vinculados à Issue #25; a preparação e o ensaio, à Issue #29. Na Demo v3, o objetivo é apresentar o fluxo completo RF01–RF05.
