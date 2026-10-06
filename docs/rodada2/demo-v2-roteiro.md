# Demo v2 — Roteiro, dados de exemplo e plano B

**Responsáveis:** Enzo e Giovana · **Duração:** 5 minutos · **Escopo:** RF01, RF02 e RF03

## 1. Preparação (antes da apresentação)

Fazer na máquina que será usada na demo, com antecedência, porque a primeira execução baixa
imagens do Docker e dependências do Gradle.

1. Atualizar o código: `git switch main` e `git pull`.
2. Abrir o Docker Desktop e esperar ele ficar pronto.
3. Zerar o banco, para a demo começar sempre do mesmo estado: `docker compose down -v`.
4. Subir o banco: `docker compose up -d` e conferir `healthy` em `docker compose ps`.
5. Subir a aplicação com os dados de exemplo:

   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev,demo'
   ```

6. Abrir `http://localhost:8080/` e conferir que o seletor de período mostra três meses.
7. Deixar abertos: o navegador na aplicação, um terminal com `docker compose ps` e a aba do
   repositório no GitHub (Issues e PRs da Iteração 2).

### Dados de exemplo

O perfil `demo` insere 17 transações em três meses (os dois anteriores e o atual), apenas se
o banco ainda não tiver nenhuma transação. As datas são calculadas a partir do dia da
execução; no mês atual, nenhuma fica no futuro. Para refazer do zero, repita os passos 3 a 5.

| Mês | Receitas | Despesas | Saldo |
|---|---|---|---|
| Dois meses atrás | R$ 4.200,00 | R$ 2.456,30 | R$ 1.743,70 |
| Mês passado | R$ 4.550,00 | R$ 2.770,75 | R$ 1.779,25 |
| Mês atual | R$ 4.200,00 | R$ 1.827,17 | R$ 2.372,83 |

## 2. Roteiro (5 minutos)

| Tempo | Quem | O que mostrar | O que dizer |
|---|---|---|---|
| 0:00–0:30 | Enzo | Terminal com `docker compose ps` (banco `healthy`) e a aplicação aberta | Desde a v1 a aplicação roda com PostgreSQL no Docker. Nesta iteração o ambiente ficou reprodutível: dois comandos sobem tudo em qualquer máquina. |
| 0:30–1:45 | Giovana | RF01: cadastrar uma despesa de hoje, editar o valor e tentar salvar uma data futura | Cadastro, edição e exclusão de transações; valor maior que zero e data futura bloqueada (ADR 001). |
| 1:45–2:30 | Giovana | RF02: categorias por tipo no formulário e a regra de exclusão | Categorias padrão e próprias; categoria com transações não pode ser excluída. |
| 2:30–4:00 | Enzo | RF03: selecionar o mês atual, mostrar totais, saldo e o gráfico por categoria; navegar para os meses anteriores com as setas | Os totais e a distribuição são calculados no banco (GROUP BY), não no navegador. A despesa cadastrada há pouco já aparece no relatório. |
| 4:00–4:30 | Enzo | `http://localhost:8080/api/relatorios?mes=AAAA-MM` no navegador e um mês inválido (`mes=2026-13`) | O endpoint devolve totais, saldo e percentual por categoria; parâmetro inválido responde 400 com mensagem clara. |
| 4:30–5:00 | Enzo | Resultado de `./gradlew test` (rodado antes) e as Issues fechadas no GitHub | Valores em `NUMERIC(19,2)`/`BigDecimal` (RNF05), schema versionado com Flyway, testes de integração em PostgreSQL real e relatório abaixo de 3 s (RNF02). |

Combinar antes: quem fala em cada bloco, quem opera o teclado e quem controla o tempo.

## 3. Plano B

| Situação | O que fazer |
|---|---|
| Docker não sobe ou o banco não fica `healthy` | Rodar sem Docker, com banco em memória e os mesmos dados de exemplo: `./gradlew bootRun --args='--spring.profiles.active=h2,demo'`. O roteiro não muda. |
| Porta 5432 ou 8080 ocupada | `DB_PORT=5433` no `.env` e na variável de ambiente do terminal, ou `--server.port=8081` nos argumentos do `bootRun`. |
| A máquina da demo falha | Usar a máquina do outro apresentador, preparada com os mesmos passos da seção 1. |
| Sem rede no local | Nada muda se a preparação foi feita antes: as imagens e dependências já estão em cache. |
| Nada funciona ao vivo | Mostrar a gravação de tela do ensaio. |

## 4. Ensaios

Fazer pelo menos dois ensaios cronometrados, um deles executando o plano B com H2 do início
ao fim, e gravar a tela do último ensaio.

| Data | Duração | Problemas encontrados | Ajuste feito |
|---|---|---|---|
| | | | |
| | | | |
