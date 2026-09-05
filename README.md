# FinApp — Controle Financeiro Pessoal

Aplicativo web para controle de gastos e receitas pessoais, desenvolvido para as disciplinas de
Engenharia de Software (ES - TCC00225) e Gerência de Projeto e Manutenção de Software (GPMS - TCC00363).

## Sobre o projeto

O FinApp permite que o usuário registre transações (receitas e despesas), organize-as em categorias,
acompanhe metas de economia e visualize relatórios de seus gastos.

Veja o Documento de Visão completo em [`docs/rodada1/01-documento-visao.md`](docs/rodada1/01-documento-visao.md).

## Stack técnica

- **Linguagem:** Kotlin
- **Build:** Gradle
- **Containerização:** Docker / Docker Compose
- **Banco de dados:** H2 em memória (dev local) / PostgreSQL (via Docker Compose)

## Equipe e papéis

| Nome | Papel |
|---|---|
| Filipe | Gerente de Projeto (GP) |
| Sara | Product Owner (PO) |
| Emanuel | Scrum Master / Facilitador |
| Arben | Responsável por Configuração |
| Enzo | Desenvolvedor |
| (Você) | Desenvolvedora |

Detalhamento completo em [`docs/rodada1/03-papeis-responsabilidades.md`](docs/rodada1/03-papeis-responsabilidades.md).

## Estrutura do repositório

```
finapp/
├── docs/                    # Documentação de gestão do projeto (Plano de Projeto)
│   └── rodada1/             # Artefatos entregues na Rodada 1
├── slides/                  # Slides usados nas apresentações
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

```bash
# Build e execução via Docker Compose (sobe a API + banco Postgres)
docker compose up --build
```

A API fica disponível em `http://localhost:8080/api` e uma interface web simples de
demonstração (cadastrar, listar, editar e excluir transações) em `http://localhost:8080/`.

Para rodar localmente sem Docker (usa H2 em memória): `./gradlew bootRun`.

## Estratégia de branches

- `main` — versão estável, sempre funcional
- `develop` — integração das features em desenvolvimento
- `feature/<nome-da-feature>` — uma branch por funcionalidade/pacote de trabalho

Merge para `develop` via Pull Request. Merge de `develop` para `main` ao final de cada iteração.

## Licença

Este projeto está sob a licença MIT — veja [`LICENSE`](LICENSE) para detalhes.
