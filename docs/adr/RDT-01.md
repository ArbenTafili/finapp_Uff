# RDT-01 — PostgreSQL local via Docker

**Registro:** [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13)  
**Requisito relacionado:** RNF03 — armazenamento local dos dados  
**Estado:** decisão técnica adotada; registro sujeito à revisão da equipe

## Contexto

O RNF03 do [Documento de Visão da Rodada 1](../rodada1/01-documento-visao.md) previa armazenamento local dos dados por privacidade. A aplicação foi construída com Kotlin, Spring Boot e Spring Data JPA. O repositório já contém PostgreSQL no `docker-compose.yml` e um perfil Spring para a execução em Docker. O perfil local sem Docker usa H2 em memória.

## Decisão

Usar PostgreSQL executado localmente via Docker Compose como mecanismo de persistência da execução integrada. A aplicação Spring Boot acessa o banco por JPA. Assim, os dados permanecem no ambiente local de execução, usando um SGBD em contêiner. H2 continua disponível somente como opção de desenvolvimento local sem Docker.

## Impacto

- **Arquitetura:** PostgreSQL integra a solução; a persistência da aplicação passa por JPA.
- **Execução:** Docker Compose inicia aplicação e banco; o perfil Docker usa PostgreSQL. O [README](../../README.md) documenta `docker compose up --build`.
- **Escopo:** a decisão explicita a implementação técnica do RNF03, sem alterar os requisitos funcionais.
- **Documentação:** este registro e a [Seção 8 do Plano de Projeto](../plano-projeto/secao-8-controle-versoes.md) dão rastreabilidade à [Issue #13](https://github.com/ArbenTafili/finapp_Uff/issues/13).

Horas, custo e data de aprovação não foram informados e não são atribuídos neste registro.
