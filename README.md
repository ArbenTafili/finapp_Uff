# FinApp — Controle Financeiro Pessoal

Aplicativo web para controle de gastos e receitas pessoais, desenvolvido para as disciplinas de
Engenharia de Software (ES - TCC00225) e Gerência de Projeto e Manutenção de Software (GPMS - TCC00363).

## Sobre o projeto

O FinApp permite que o usuário registre transações (receitas e despesas), organize-as em categorias,
acompanhe metas de economia e visualize relatórios de seus gastos.

Veja o [Documento de Visão](docs/visao/README.md) e o [Plano de Projeto](docs/plano-projeto/README.md).

## Stack técnica

- **Linguagem:** Kotlin
- **Aplicação:** Spring Boot e Spring Data JPA
- **Build:** Gradle
- **Containerização:** Docker / Docker Compose
- **Banco de dados na execução integrada:** PostgreSQL local via Docker
- **Opção para desenvolvimento sem Docker:** H2 em memória

## Equipe e papéis

| Nome | Papel |
|---|---|
| Filipe | Gerente de Projeto (GP) |
| Sara | Product Owner (PO) |
| Emanuel | Scrum Master / Facilitador |
| Arben | Responsável por Configuração |
| Enzo | Desenvolvedor |
| Giovana | Desenvolvedora |

Detalhamento completo em [`docs/rodada1/03-papeis-responsabilidades.md`](docs/rodada1/03-papeis-responsabilidades.md).

## Estrutura do repositório

```
finapp/
├── docs/
│   ├── visao/               # Acesso ao Documento de Visão
│   ├── plano-projeto/       # Plano de Projeto e Seção 8
│   ├── slides/              # Resumos e acesso aos slides
│   ├── adr/                 # Registros de decisões
│   └── rodada1/             # Artefatos originais da Rodada 1
├── slides/                  # Arquivos originais das apresentações
├── src/                     # Código-fonte da aplicação (Kotlin)
│   ├── main/kotlin/...
│   └── test/kotlin/...
├── build.gradle.kts
├── settings.gradle.kts
├── Dockerfile
├── docker-compose.yml
└── LICENSE
```

## Como rodar o projeto

Pré-requisitos para a execução integrada: Docker com Docker Compose. Execute na raiz do repositório:

```bash
docker compose up --build
```

Esse comando sobe a aplicação e o banco PostgreSQL. A API fica disponível em
`http://localhost:8080/api` e uma interface web simples de demonstração (cadastrar, listar,
editar e excluir transações) em `http://localhost:8080/`. Os dados ficam no volume
`finapp-db-data` e sobrevivem a `docker compose down`.

### Desenvolvimento local

Para rodar a aplicação fora do Docker basta ter qualquer Java 8 ou superior instalado, só para
iniciar o Gradle: o JDK 17 usado pelo projeto é baixado pelo próprio Gradle na primeira
execução (`gradle/gradle-daemon-jvm.properties`). No Windows (PowerShell ou cmd), use
`.\gradlew.bat` no lugar de `./gradlew`.

Sem Docker, com H2 em memória (os dados somem ao parar a aplicação):

```bash
./gradlew bootRun
```

Com a aplicação na máquina e o PostgreSQL do Docker, suba só o banco e ative o perfil `dev`:

```bash
docker compose up -d db
```

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Perfis do Spring

| Perfil | Quando é usado | Banco |
|---|---|---|
| `h2` (padrão) | `./gradlew bootRun`, sem Docker | H2 em memória |
| `docker` | Aplicação em container (`docker compose up --build`) | PostgreSQL do Compose (host `db`) |
| `dev` | Aplicação na máquina, com `--spring.profiles.active=dev` | PostgreSQL do Compose em `localhost:5432` |
| `test` | `./gradlew test` (ativado automaticamente) | PostgreSQL em container criado pelo Testcontainers (exige Docker em execução, não o `docker compose`) |
| `demo` | Somado a `dev` ou `h2` (ex.: `dev,demo`) no ensaio da demo | Pré-carrega a massa base da Demo v2 se o banco estiver sem transações; ver [`docs/rodada2/05-preparacao-demo-v2.md`](docs/rodada2/05-preparacao-demo-v2.md) |

A conexão com o PostgreSQL fica em `src/main/resources/application-docker.properties` e
`application-dev.properties`; a do H2, em `application-h2.properties`.

### Relatório mensal (RF03)

`GET /api/relatorios?mes=AAAA-MM` devolve os totais do mês, o saldo e a distribuição das
despesas por categoria. Os campos `mesAnterior` e `proximoMes` servem para navegar entre meses.
Um mês sem transações responde 200 com totais zerados; `mes` ausente ou fora do formato
responde 400.

```bash
curl "http://localhost:8080/api/relatorios?mes=2026-09"
```

```json
{
  "mes": "2026-09",
  "mesAnterior": "2026-08",
  "proximoMes": "2026-10",
  "totalReceitas": 3000.00,
  "totalDespesas": 600.00,
  "saldo": 2400.00,
  "despesasPorCategoria": [
    { "categoriaId": 9, "categoria": "Educação Demo", "valor": 300.00, "percentual": 50.00 },
    { "categoriaId": 3, "categoria": "Alimentação", "valor": 200.00, "percentual": 33.33 },
    { "categoriaId": 5, "categoria": "Transporte", "valor": 100.00, "percentual": 16.67 }
  ]
}
```

O `percentual` é a participação da categoria no total de despesas do mês, arredondada a duas
casas. Receitas entram em `totalReceitas`, mas não na distribuição.

### Schema e migrações (Flyway)

O schema é versionado em `src/main/resources/db/migration` e aplicado pelo Flyway na subida da
aplicação, em qualquer perfil; o Hibernate só valida (`ddl-auto=validate`). Valores monetários
são `NUMERIC(19,2)` no banco e `BigDecimal` no código (RNF05).

- Para alterar o schema (coluna, tabela ou restrição nova em uma entidade), crie um novo
  arquivo `V<n>__descricao.sql`. Sem ele a aplicação não sobe, porque a validação falha.
- Nunca edite uma migração que já foi para a `main`.
- Quem já tinha o banco da Demo v1 não precisa apagar o volume: o Flyway marca o schema
  existente como V1 e aplica só as migrações seguintes.

### Portas e variáveis de ambiente

Todas as variáveis têm valor padrão, então nada precisa ser configurado. Para mudar algum
valor, copie `.env.example` para `.env` (o `.env` não é versionado).

| Variável | Padrão | Uso |
|---|---|---|
| `POSTGRES_DB` | `finapp` | Nome do banco |
| `POSTGRES_USER` | `finapp` | Usuário do banco |
| `POSTGRES_PASSWORD` | `finapp` | Senha do banco |
| `DB_PORT` | `5432` | Porta do PostgreSQL exposta na máquina |
| `APP_PORT` | `8080` | Porta da aplicação quando roda em container |

O `docker compose` lê o `.env` sozinho. O `./gradlew bootRun` não: se você mudou algum valor
e usa o perfil `dev`, exporte a mesma variável no terminal antes de rodar a aplicação.

### Comandos úteis

| Comando | O que faz |
|---|---|
| `docker compose ps` | Mostra o estado dos containers (o banco deve aparecer `healthy`) |
| `docker compose logs -f` | Acompanha os logs da aplicação e do banco |
| `docker compose down` | Para os containers e mantém os dados |
| `docker compose down -v` | Para os containers e **apaga os dados** (volume) |
| `docker compose exec db psql -U finapp -d finapp` | Abre o `psql` dentro do container |
| `./gradlew test` | Roda os testes automatizados |

### Problemas comuns

- **Porta 5432 em uso** (PostgreSQL instalado na máquina): crie o `.env` com `DB_PORT=5433` e
  suba os containers de novo.
- **Porta 8080 em uso:** a aplicação em container e o `./gradlew bootRun` usam a mesma porta.
  Pare um dos dois, ou rode com `./gradlew bootRun --args='--server.port=8081'`.
- **`Connection refused` no perfil `dev`:** o banco ainda não está pronto ou o Docker está
  parado; confira com `docker compose ps`.

## Controle de versões — GitHub Flow

Cada alteração parte da `main` protegida, em uma branch vinculada à Issue. O fluxo é
`Issue → branch → commits → Pull Request → revisão → squash merge → main`.
O PR deve usar `Closes #N` e a convenção de commits é `tipo(escopo): descrição #issue`.

A antiga branch `develop` permanece como histórico da Rodada 1. Veja a
[estratégia detalhada](docs/adr/estrategia-branches.md) e a
[Seção 8 do Plano de Projeto](docs/plano-projeto/secao-8-controle-versoes.md).

## Licença

Este projeto está sob a licença MIT — veja [`LICENSE`](LICENSE) para detalhes.
