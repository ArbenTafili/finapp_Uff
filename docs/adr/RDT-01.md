# RDT-01 — PostgreSQL local via Docker e RNF03

**ID:** RDT-01

**Issue:** [#13 — PostgreSQL local via Docker](https://github.com/ArbenTafili/finapp_Uff/issues/13)

**Requisito afetado:** RNF03 — Segurança/Privacidade

**Registro inicial:** Arben Tafili, integrado à `main` pelo [PR #33](https://github.com/ArbenTafili/finapp_Uff/pull/33)

**Responsável pela complementação documental:** Sara Marcomini (Product Owner)

**Data da decisão e aprovadores:** a confirmar com a equipe

**Estado:** escolha técnica adotada e registro inicial integrado; aprovação formal da decisão e aceite desta complementação pendentes.

**Pacotes relacionados:** PT01–PT02; EAP 2.2 (Persistência) e 2.4 (Execução Containerizada).

O RDT-01 mantém o registro inicial do Arben e a complementação da Sara em um único documento. A Issue #13 está fechada; a data e os participantes da aprovação formal permanecem a confirmar.

## 1. Contexto

O RNF03 do [Documento de Visão da Rodada 1](../rodada1/01-documento-visao.md) previa armazenamento local dos dados por privacidade. A arquitetura utiliza Kotlin, Spring Boot e Spring Data JPA. PostgreSQL via Docker já estava presente no ponto de partida da rodada; esta decisão formaliza a escolha existente, sem afirmar que a migração de H2 para PostgreSQL ocorreu nesta iteração.

O [Docker Compose](../../docker-compose.yml) inicia aplicação e banco localmente. O perfil sem Docker usa H2 em memória. A restrição de armazenamento refere-se aos dados financeiros da aplicação e deve ser conferida na configuração e na execução.

## 2. Decisão e redação do RNF03

Adotar PostgreSQL executado localmente em contêiner Docker como persistência da execução integrada, acessado pela aplicação através de JPA.

Na seção 2 do Plano de Projeto, adotar a redação:

> Os dados devem ser armazenados em banco de dados executado localmente (Docker), sem envio a serviços externos.

| Perfil | Aplicação e banco | Uso |
|---|---|---|
| `docker` | Aplicação e PostgreSQL em contêineres locais | Execução integrada pelo Compose. |
| `dev` | Aplicação na máquina e PostgreSQL no Docker local | Desenvolvimento com o banco persistente. |
| `h2` | Aplicação na máquina e H2 em memória | Desenvolvimento sem Docker e contingência prevista para a demo; os dados desaparecem ao encerrar a aplicação. |
| `test` | PostgreSQL isolado via Testcontainers | Testes automatizados; requer Docker em execução. |

H2 é uma alternativa de desenvolvimento e contingência, sem comprovar o aceite do ambiente PostgreSQL definido no RNF03.

## 3. Justificativa

- Alinhar o requisito à arquitetura Kotlin/Spring Boot/JPA e à persistência já adotada.
- Reproduzir a execução local por Docker Compose para a equipe e a demonstração.
- Manter os dados financeiros sob controle de quem executa a aplicação.
- Preservar os limites do produto: autenticação, entidade `Usuario` e funcionalidades multiusuário permanecem fora do escopo.

A execução local não comprova, isoladamente, todas as propriedades de segurança nem substitui o aceite do requisito.

## 4. Alternativas e histórico

Os materiais não identificam todas as alternativas efetivamente discutidas na data da decisão. A tabela é uma comparação documental para revisão, e não uma ata de deliberação:

| Alternativa | Análise | Situação |
|---|---|---|
| H2 em memória | Simplifica o ambiente sem Docker; os dados não permanecem após encerrar a aplicação. | Mantido para desenvolvimento e contingência da demo. |
| PostgreSQL instalado diretamente | Mantém armazenamento local, com instalação e configuração próprias em cada máquina. | Alternativa de comparação; discussão histórica a confirmar. |
| PostgreSQL em Docker local | Mantém o banco local e descreve sua execução no Compose já existente. | Escolha técnica adotada. |

## 5. Impactos em escopo, prazo e custo

| Dimensão | Registro |
|---|---|
| Escopo | Explicita a implementação técnica do RNF03; mantém RF01–RF05 e os limites do produto. |
| Arquitetura | Persistência por JPA com PostgreSQL local; não inclui serviço externo de armazenamento financeiro. |
| Execução | Compose, perfis e schema documentados nas Issues #14/#15, integradas pelos PRs #34/#35. Flyway versiona o schema; alterações futuras em entidades precisam de migração correspondente. |
| Documentação | RNF03 atualizado na revisão da seção 2; este registro dá rastreabilidade ao controle de mudanças. |
| Prazo e esforço | Impacto no cronograma e horas previstas/realizadas a confirmar com Filipe e Enzo. |
| Custo | Impacto no orçamento a confirmar com Filipe; nenhum custo zero foi presumido. |

## 6. Riscos e contingência

Há risco de dificuldade para instalar ou configurar Docker/PostgreSQL nas máquinas da equipe e da avaliadora.

- **Contenção documentada:** instruções no README, Compose, perfis e preparação antecipada do ambiente.
- **Contingência prevista:** perfil `h2,demo`, máquina alternativa e gravação do ensaio, conforme a [preparação técnica da Demo v2](../rodada2/05-preparacao-demo-v2.md), vinculada à Issue #29. Execução e ensaio permanecem pendentes de evidência.
- **Probabilidade, impacto, exposição e responsável pelo risco:** confirmação por Emanuel na [Issue #26](https://github.com/ArbenTafili/finapp_Uff/issues/26).

## 7. Rastreabilidade e pendências

| Referência | Relação |
|---|---|
| [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13) e [PR #33](https://github.com/ArbenTafili/finapp_Uff/pull/33) | Registro técnico inicial integrado pelo Arben. O campo de link no corpo da Issue ainda deve apontar ao registro canônico. |
| [Escopo e EAP da rodada 1](../rodada1/02-escopo-eap.md) | Referência histórica preservada. |
| [Seção 2 — revisão da rodada 2](https://github.com/ArbenTafili/finapp_Uff/blob/feature/sara-backlog-rodada2/docs/rodada2/02-escopo-eap.md) | Nova redação do RNF03 e escopo revisado na Issue #24; documento ainda publicado em branch. |
| [Seção 8 — Controle de Versões](../plano-projeto/secao-8-controle-versoes.md) | Uso de Issues e registros no controle de mudanças. |
| [README](../../README.md) e [Docker Compose](../../docker-compose.yml) | Instruções e configuração de execução local. |
| [Perfis H2](../../src/main/resources/application-h2.properties), [dev](../../src/main/resources/application-dev.properties) e [Docker](../../src/main/resources/application-docker.properties) | Configurações dos modos de execução. |
| [Issue #14](https://github.com/ArbenTafili/finapp_Uff/issues/14) e [Issue #15](https://github.com/ArbenTafili/finapp_Uff/issues/15) | Infraestrutura e schema integrados à `main`; aceite funcional deve usar evidências de execução. |
| [Issues #22](https://github.com/ArbenTafili/finapp_Uff/issues/22) e [#23](https://github.com/ArbenTafili/finapp_Uff/issues/23) | Confirmação de esforço, indicadores, cronograma e orçamento por Filipe. |
| [ADR 001](../rodada1/10-decisoes-tecnicas.md) | Registro independente sobre `data <= hoje`, preservado. |

Confirmar data da decisão, participantes/aprovadores e histórico das alternativas com a equipe. Confirmar horas, prazo e custo com Filipe. A complementação é publicada em branch para revisão e não reabre nem encerra Issues automaticamente.

## 8. Resumo de quatro linhas para slide

- O RNF03 original previa armazenamento local dos dados por privacidade.
- O FinApp adota PostgreSQL em Docker local, alinhado à arquitetura Kotlin/Spring Boot/JPA.
- A redação revisada explicita banco local via Docker, sem envio dos dados a serviços externos.
- RDT-01 vinculado à Issue #13; impactos e aprovação formal a confirmar, com contingência H2 prevista na Issue #29.
