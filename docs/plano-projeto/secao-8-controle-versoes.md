# Seção 8 — Controle de Versões e Modificações

**Responsável:** Arben (Gerente de Configuração)

## 1. Estratégia de Branches
O projeto adota o modelo **GitHub Flow** adaptado para as entregas das rodadas.
- A branch `main` é a principal, protegida, e sempre contém código integrado em estado de produção.
- É proibido realizar commits diretamente na `main`.
- Todo novo trabalho deve ser feito em uma *feature branch* derivada da `main`, seguindo o padrão `feature/#numero-da-issue-descricao` (ex.: `feature/#24-correcao-eap`).
- A integração das alterações na branch principal ocorre exclusivamente via **Pull Request (PR)**, com a obrigatoriedade de revisão de código por pelo menos um membro diferente do autor da branch.
- Nas Rodadas 1 e 2 as branches passaram pela `develop` antes da `main`. Na Rodada 3 os PRs foram direto para a `main`.

## 2. Convenção de Commits
Os commits seguem o padrão *Conventional Commits*:
- **Formato obrigatório:** `tipo(escopo): descrição #issue`
- **Tipos Permitidos:** `feat` (nova funcionalidade), `fix` (correção), `docs` (documentação), `test` (testes automatizados) e `config` (infraestrutura/setup).
- **Exemplo real:** `feat(categorias): seed idempotente de categorias padrão #17`

## 3. Controle de Modificações e Issues
Todas as tarefas, *bugs* e melhorias de requisitos devem ser rastreadas como **Issues** no repositório GitHub.
- Cada Issue criada precisa conter, obrigatoriamente: o identificador do pacote na EAP (ex.: 3.1.2, o antigo PT04 da Rodada 1), a estimativa de horas (baseada no Planning Poker) e Critérios de Aceite claros.
- As Issues são associadas a *Milestones* (Iteração 1, Iteração 2, etc) e organizadas através de *Labels* como `feature`, `infra`, `teste`, `gestão`, `demo`, `config`, `bug` e `mudança de escopo`.

## 4. Gerenciamento de Mudança de Escopo: Caso RDT-01
Qualquer alteração arquitetural que impacte prazo ou custo deve ser tratada como Mudança de Escopo.
- **RDT-01:** Decisão formal de migrar o banco de dados do armazenamento em memória/arquivo (H2/SQLite - RNF03) para uma instância local do **PostgreSQL** orquestrada via **Docker**.
- **Justificativa Técnica:** Ter o mesmo banco em todas as máquinas e usar recursos do PostgreSQL nas consultas do relatório mensal (RF03).
- **Rastreabilidade e Impacto:** Decisão rastreada na Issue #13. Esta alteração técnica injetou aproximadamente 8 horas extras no esforço da equipe, registradas no EVM da iteração e compensadas graças à antecipação do RF03.
