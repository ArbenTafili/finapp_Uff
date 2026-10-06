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

A API fica disponível em `http://localhost:8080/api` e uma interface web simples de
demonstração (cadastrar, listar, editar e excluir transações) em `http://localhost:8080/`.

Para desenvolvimento local sem Docker, use JDK 17 e `./gradlew bootRun`; esse perfil usa H2 em memória.

## Controle de versões — GitHub Flow

Cada alteração parte da `main` protegida, em uma branch vinculada à Issue. O fluxo é
`Issue → branch → commits → Pull Request → revisão → squash merge → main`.
O PR deve usar `Closes #N` e a convenção de commits é `tipo(escopo): descrição #issue`.

A antiga branch `develop` permanece como histórico da Rodada 1. Veja a
[estratégia detalhada](docs/adr/estrategia-branches.md) e a
[Seção 8 do Plano de Projeto](docs/plano-projeto/secao-8-controle-versoes.md).

## Licença

Este projeto está sob a licença MIT — veja [`LICENSE`](LICENSE) para detalhes.
