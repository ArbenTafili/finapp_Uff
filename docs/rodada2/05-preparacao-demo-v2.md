# Preparação técnica e plano B da Demo v2 — FinApp

**Responsáveis:** Enzo e Giovana

**Issue:** [#29 — Preparar e ensaiar a Demo v2](https://github.com/ArbenTafili/finapp_Uff/issues/29)

O que será demonstrado, a ordem, os responsáveis por cada etapa, os dados e os resultados
esperados estão definidos pela Sara em [Critérios de Aceite e Roteiro da Demo v2](04-aceite-demo-v2.md)
(Issue #25). Este documento cobre só a parte técnica: como subir o ambiente, como preparar os
dados e o que fazer se algo falhar.

## 1. Ambiente dedicado à demonstração

O roteiro pede uma instância sem outras transações em setembro de 2026 e sem apagar dados de
uso real. Para isso, a demo usa um projeto do Docker Compose separado (`finapp-demo`), com
volume próprio; o banco de desenvolvimento de cada um fica intacto.

Fazer na máquina da apresentação, com antecedência, porque a primeira execução baixa imagens
do Docker e dependências do Gradle.

1. Atualizar o código: `git switch main` e `git pull`.
2. Abrir o Docker Desktop e esperar ele ficar pronto.
3. Parar os containers de desenvolvimento, se estiverem rodando (os dados são mantidos):

   ```bash
   docker compose down
   ```

4. Subir a aplicação e o banco da demo, como no roteiro:

   ```bash
   docker compose -p finapp-demo up --build
   ```

5. Conferir `http://localhost:8080/` e `http://localhost:8080/api/categorias`.

Para recomeçar a demo do zero (apaga só os dados da demo):

```bash
docker compose -p finapp-demo down -v
```

## 2. Dados

| Forma | Quando usar | Como |
|---|---|---|
| Cadastro ao vivo | Padrão do roteiro: a Giovana cria a categoria e cadastra D1–D4 na apresentação | Subir como na seção 1, com o banco da demo vazio |
| Massa pré-carregada | Ensaio, ou se o tempo estiver curto | Subir só o banco da demo e rodar a aplicação na máquina com o perfil `demo` (comandos abaixo) |

```bash
docker compose -p finapp-demo up -d db
```

```bash
./gradlew bootRun --args='--spring.profiles.active=dev,demo'
```

O perfil `demo` insere exatamente a massa base do roteiro (D1–D4, em setembro de 2026) e a
categoria personalizada Educação Demo, apenas se o banco não tiver nenhuma transação. Com a
massa pré-carregada, o relatório de `2026-09` deve mostrar receitas de R$ 3.000,00, despesas de
R$ 600,00 e saldo de R$ 2.400,00, como na tabela de resultados esperados do roteiro.

Se a massa for pré-carregada, vale o que o roteiro determina: identificar os dados como
preparados e demonstrar o cadastro com a transação temporária T1, sem duplicar D1–D4.

## 3. Plano B

| Situação | O que fazer |
|---|---|
| Docker não sobe ou o banco não fica `healthy` | Rodar sem Docker, com banco em memória e a massa base: `./gradlew bootRun --args='--spring.profiles.active=h2,demo'`. O roteiro não muda, mas os dados somem ao parar a aplicação. |
| Porta 5432 ou 8080 ocupada | `DB_PORT=5433` ou `APP_PORT=8081` no `.env` antes de subir os containers. |
| A máquina da demo falha | Usar a máquina do outro apresentador, preparada com os mesmos passos da seção 1. |
| Sem rede no local | Nada muda se a preparação foi feita antes: imagens e dependências já estão em cache. |
| Nada funciona ao vivo | Mostrar a gravação de tela do último ensaio e registrar o impedimento, sem apresentar o cenário como validado. |

O plano B com H2 deve ser informado ao Emanuel para a análise de riscos (Issue #26).

## 4. Ensaios

Fazer pelo menos um ensaio completo e cronometrado seguindo o roteiro da Sara, e um ensaio do
plano B com H2. Gravar a tela do último ensaio. Os resultados de cada cenário de aceite são
registrados na tabela da seção 5 do roteiro; aqui ficam só os problemas de ambiente.

| Data | Duração | Problemas encontrados | Ajuste feito |
|---|---|---|---|
| | | | |
| | | | |
