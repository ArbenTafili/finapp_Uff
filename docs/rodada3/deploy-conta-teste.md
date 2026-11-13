# Publicação e conta de teste — Rodada 3

O sistema publicado exige a **conta de teste**: ao abrir o endereço, o usuário cai na tela de login (`/login.html`) e informa usuário e senha; a sessão fica num cookie HttpOnly (8 h) e há um botão "Sair". Clientes de API (curl) ainda podem usar HTTP Basic. Ela existe só para a banca/avaliadores acessarem a versão publicada; o FinApp continua sem cadastro de usuários (fora do escopo, ver ADR 001 e o documento de escopo), e os dados ficam no PostgreSQL da própria hospedagem (RNF03).

## Opção A — Render (recomendada, `render.yaml` na raiz)

1. Faça o merge das branches da rodada 3 na `main` (ver ordem de merge abaixo).
2. No Render: **New + > Blueprint**, escolha o repositório e confirme. Serão criados o banco `finapp-db` e o serviço web `finapp`.
3. Aguarde o build (o Dockerfile compila com Gradle; leva alguns minutos). O health check é `/api/saude`.
4. Em **finapp > Environment**, copie o valor gerado de `FINAPP_ACESSO_TESTE_SENHA`. Usuário padrão: `teste`.
5. Abra a URL do serviço, informe a conta e confira: relatório de novembro com gráfico, `Metas de economia` (3 metas) e exportação CSV.
6. Informe URL, usuário e senha no Classroom junto da entrega.

Observações do plano gratuito: o serviço “dorme” após inatividade (a primeira requisição leva ~1 min), e o banco gratuito do Render expira após o período do plano; faça o deploy perto da data de entrega e teste no dia.

## Opção B — Qualquer host com Docker

```bash
docker build -t finapp .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod -e TZ=America/Sao_Paulo \
  -e DB_HOST=<host-do-postgres> -e DB_PORT=5432 \
  -e POSTGRES_DB=finapp -e POSTGRES_USER=finapp -e POSTGRES_PASSWORD=<senha-do-banco> \
  -e FINAPP_ACESSO_TESTE_USUARIO=teste -e FINAPP_ACESSO_TESTE_SENHA=<senha-da-conta-de-teste> \
  finapp
```

## Variáveis do perfil `prod`

| Variável | Obrigatória | Função |
|---|---|---|
| `DB_HOST`, `DB_PORT`, `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | sim | Conexão com o PostgreSQL |
| `FINAPP_ACESSO_TESTE_USUARIO`, `FINAPP_ACESSO_TESTE_SENHA` | sim | Conta de teste; a aplicação não sobe sem elas |
| `FINAPP_SEED_DEMO` | não (padrão `true`) | Insere 3 meses + mês corrente de transações e 3 metas na primeira subida, só se o banco estiver vazio |
| `PORT` | não (padrão 8080) | Porta HTTP (a hospedagem costuma definir) |

## Demonstração local (sem publicar)

Se a equipe preferir vídeo da versão local: `docker compose up --build` e, para carregar os dados de exemplo, `SPRING_PROFILES_ACTIVE=docker` com `FINAPP_SEED_DEMO=true` no serviço `finapp`.

## Ordem de merge

As branches saem da `main` e são independentes, exceto `infra/deploy-conta-teste`, que usa as entidades de RF04:

1. `feature/rf03-interface-relatorio`
2. `feature/rf05-exportacao-csv`
3. `feature/rf04-metas` — conflita com a anterior em `TransacaoRepository.kt` (as duas adicionam uma consulta no fim do arquivo; manter as duas)
4. `feature/rf02-interface-categorias`
5. `infra/deploy-conta-teste`

A branch `release/rodada3` já traz as quatro primeiras integradas (conflito resolvido) e serve de referência.

## Roteiro de verificação (Demo v3)

1. Acessar com a conta de teste → relatório do mês corrente (RF03), navegar para meses anteriores.
2. Cadastrar uma receita e uma despesa (RF01) → totais e rosca atualizam.
3. Criar categoria personalizada e usá-la (RF02).
4. Criar meta com prazo futuro; registrar receita e ver o progresso subir (RF04).
5. Exportar CSV de um período e abrir no Excel/LibreOffice (RF05).
