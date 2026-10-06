# Preparação técnica e plano B da Demo v2 — FinApp

**Responsáveis pela preparação e ensaio:** Enzo e Giovana

**Revisão de alinhamento documental:** Sara Marcomini (Product Owner)

**Issue:** [#29 — Preparar e ensaiar a Demo v2](https://github.com/ArbenTafili/finapp_Uff/issues/29)

O escopo, a sequência funcional, os responsáveis por etapa e os resultados esperados estão nos [Critérios de Aceite e Roteiro da Demo v2](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-aceite-demo-v2/docs/rodada2/04-aceite-demo-v2.md), Issue #25. Este documento define ambiente, preparação dos dados, contingência e registro dos ensaios.

O roteiro funcional permanece publicado na branch da Sara enquanto sua integração não ocorrer. Após a integração da Issue #25 à main, o link poderá apontar ao arquivo relativo `04-aceite-demo-v2.md`.

## 1. Versão e ambiente dedicado

As entregas da Giovana (#17, #18, #16 e #21) foram integradas à main pelo [PR #43](https://github.com/ArbenTafili/finapp_Uff/pull/43), junto da infraestrutura e dos relatórios do Enzo. A referência de versão é main `c86694f`; registrar o commit efetivamente usado em cada ensaio. A integração do código não substitui o registro de execução e aceite dos cenários.

As novas alterações documentais seguem o fluxo informado pela equipe: branch → develop → main, com um PR por Issue para develop. A configuração inicial em docs/adr/estrategia-branches.md ainda descreve GitHub Flow; Arben deve alinhar a estratégia formal e a Seção 8 ao fluxo atual.

Na máquina da apresentação, preparar o ambiente com antecedência para baixar imagens e dependências:

1. Salvar o trabalho e conferir `git status`. Depois da integração das entregas à main, atualizar com `git switch main` e `git pull --ff-only origin main`.
2. Abrir o Docker Desktop e aguardar sua disponibilidade.
3. Parar uma aplicação iniciada por Gradle com Ctrl+C, se estiver em execução, e parar o Compose de desenvolvimento com `docker compose down`, sem remover seus volumes.
4. Usar o projeto `finapp-demo`, cujo volume de banco é separado do volume do projeto de desenvolvimento.

Os nomes de contêiner são fixos no Compose atual (`finapp-db` e `finapp`). Por isso, os projetos de desenvolvimento e demonstração não podem rodar simultaneamente, mesmo em portas diferentes.

## 2. Escolher uma forma de execução

### A. Cadastro ao vivo — aplicação e banco no Compose

```bash
docker compose -p finapp-demo up --build
```

Em outro terminal, conferir:

```bash
docker compose -p finapp-demo ps
```

Abrir `http://localhost:8080/` e `http://localhost:8080/api/categorias`. O perfil Docker não pré-carrega D1–D4: Giovana cria a categoria personalizada e cadastra a massa base uma única vez, conforme o roteiro. Se o banco da demo já tiver dados, conferir seu conteúdo antes de repetir o cadastro.

### B. Massa pré-carregada — banco no Compose e aplicação pelo Gradle

Se a aplicação da opção A estiver rodando, pará-la antes de iniciar outra aplicação:

```bash
docker compose -p finapp-demo stop finapp
docker compose -p finapp-demo up -d db
```

Com as configurações padrão:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev,demo'
```

O Compose lê `.env` automaticamente; o Gradle não. Se banco, usuário, senha ou porta tiverem sido alterados, exportar os mesmos valores no terminal do Gradle antes do `bootRun`, conforme [.env.example](../../.env.example). Não iniciar `docker compose up -d` sem o nome `db` nessa opção: isso também inicia a aplicação em contêiner.

O perfil `demo` insere D1–D4, em setembro de 2026, e a categoria Educação Demo **somente se não existir nenhuma transação no banco**. Não limpa nem substitui dados existentes.

| Indicador de `2026-09` | Resultado esperado |
|---|---|
| Receitas | R$ 3.000,00 |
| Despesas | R$ 600,00 |
| Saldo | R$ 2.400,00 |
| Distribuição das despesas | Educação Demo: R$ 300,00 (50,00%); Alimentação: R$ 200,00 (33,33%); Transporte: R$ 100,00 (16,67%). |

```bash
curl 'http://localhost:8080/api/relatorios?mes=2026-09'
```

Com dados pré-carregados, mostrar D1–D4 existentes e demonstrar cadastro/edição/exclusão com T1. Criar T1 uma única vez; excluí-la antes da conferência final dos totais. Para demonstrar CRUD de categoria, usar Categoria Temporária Demo, sem duplicar Educação Demo.

### Reiniciar somente a massa da demo

Se for necessário recomeçar, parar o Gradle e remover exclusivamente o volume do projeto de demonstração:

```bash
docker compose -p finapp-demo down -v
```

Esse comando apaga os dados da demo. Não usar `docker compose down -v` no projeto de desenvolvimento. Após a limpeza, repetir A ou B.

## 3. Portas e configurações

| Execução | PostgreSQL | Porta da aplicação |
|---|---|---|
| Aplicação em contêiner | `DB_PORT` no `.env` ou no ambiente do Compose. | `APP_PORT` no `.env` ou no ambiente do Compose. |
| Aplicação pelo Gradle (`dev`) | Exportar `DB_PORT` e as demais variáveis do banco com os mesmos valores do Compose. | Passar `--server.port` ao Spring; `APP_PORT` não configura o Gradle. |

Exemplo de porta alternativa do banco, usando o mesmo terminal para Compose e Gradle:

```bash
export DB_PORT=5433
docker compose -p finapp-demo up -d db
./gradlew bootRun --args='--spring.profiles.active=dev,demo --server.port=8081'
```

Nesse exemplo, abrir a aplicação e as APIs em `http://localhost:8081/`. Se `.env` alterar `POSTGRES_DB`, `POSTGRES_USER` ou `POSTGRES_PASSWORD`, exportar também esses valores para o Gradle. Não registrar senhas nas evidências do ensaio.

## 4. Plano B

| Situação | Resposta |
|---|---|
| Docker/PostgreSQL indisponível | Parar a aplicação anterior e executar `./gradlew bootRun --args='--spring.profiles.active=h2,demo'`. H2 guarda dados em memória, perdidos ao encerrar a aplicação; informar o ambiente usado. |
| Porta 5432 ou 8080 ocupada | Usar a configuração correspondente da seção 3; ajustar as URLs do roteiro. |
| Máquina da apresentação indisponível | Usar a máquina alternativa previamente preparada por Enzo/Giovana. |
| Sem rede no local | Usar o ambiente preparado e o cache de imagens/dependências. Conferir antes do ensaio a interface, incluindo os recursos externos que ela utiliza. |
| Falha em todas as alternativas ao vivo | Mostrar a gravação do último ensaio e registrar o impedimento. A gravação não comprova funcionamento ao vivo. |

Informar a contingência H2 a Emanuel na Issue #26. O teste com H2 não substitui a validação da persistência PostgreSQL/RNF03 nem o cenário de reinicialização com dados persistentes.

## 5. Ensaios e evidências

Enzo e Giovana devem realizar um ensaio completo na versão integrada com PostgreSQL e um ensaio da contingência H2, cronometrar o roteiro de cinco minutos e gravar o último ensaio.

Registrar os 34 cenários no documento da Issue #25, com commit, ambiente, executor e evidência. A demonstração ao vivo seleciona os fluxos principais; os demais casos são verificados no ensaio. Para os testes automatizados existentes, executar `./gradlew test` com Docker disponível e registrar o resultado. Corrigir ou registrar falhas nas Issues técnicas correspondentes.

| Data e executores | Commit/ambiente | Duração da demo | Problemas encontrados | Ajuste ou Issue | Evidência |
|---|---|---|---|---|---|
| A registrar | PostgreSQL / a registrar | A registrar | A registrar | A registrar | A registrar |
| A registrar | H2 / a registrar | A registrar | A registrar | A registrar | A registrar |

A Issue #29 permanece pendente até registrar pelo menos um ensaio completo e os resultados da preparação e contingência.
