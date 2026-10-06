# Seção 8 — Controle de Versões e Modificações

**Responsável:** Arben (Gerente de Configuração)

## 1. Estratégia de Branches
O projeto adota o modelo **GitHub Flow** adaptado para as entregas das rodadas.
- A branch `main` é a principal, protegida, e sempre contém código integrado em estado de produção.
- É proibido realizar commits diretamente na `main`.
- Todo novo trabalho deve ser feito em uma *feature branch* derivada da `main`, seguindo a rigorosa nomenclatura: `feature/#numero-da-issue-descricao` (ex: `feature/#8-seed`).
- A integração das alterações na branch principal ocorre exclusivamente via **Pull Request (PR)**, com a obrigatoriedade de revisão de código por pelo menos um membro diferente do autor da branch.

## 2. Convenção de Commits
Para manter o histórico legível e profissional, a equipe adota o padrão *Conventional Commits*:
- **Formato obrigatório:** `tipo(escopo): descrição #issue`
- **Tipos Permitidos:** `feat` (nova funcionalidade), `fix` (correção), `docs` (documentação), `test` (testes automatizados) e `config` (infraestrutura/setup).
- **Exemplo de Commit Real:** `feat(cat): seed #8`

## 3. Controle de Modificações e Issues
Todas as tarefas, *bugs* e melhorias de requisitos devem ser rastreadas como **Issues** no repositório GitHub.
- Cada Issue criada precisa conter, obrigatoriamente: o identificador do pacote na EAP (ex: PT04), a estimativa de horas (baseada no Planning Poker) e Critérios de Aceite claros.
- As Issues são associadas a *Milestones* (Iteração 1, Iteração 2, etc) e organizadas através de *Labels* como `feature`, `infra`, `teste`, `gestão`, `demo`, `config`, `bug` e `mudança de escopo`.

## 4. Gerenciamento de Mudança de Escopo: Caso RDT-01
Qualquer alteração arquitetural que impacte prazo ou custo deve ser tratada como Mudança de Escopo.
- **RDT-01:** Decisão formal de migrar o banco de dados do armazenamento em memória/arquivo (H2/SQLite - RNF03) para uma instância local do **PostgreSQL** orquestrada via **Docker**.
- **Justificativa Técnica:** Garantir paridade idêntica entre os ambientes de desenvolvimento de todos os integrantes e fornecer capacidades relacionais nativas robustas para viabilizar as queries do relatório mensal (RF03).
- **Rastreabilidade e Impacto:** Decisão rastreada na Issue #4. Esta alteração técnica injetou aproximadamente 8 horas extras no esforço da equipe, registradas no EVM da iteração e compensadas graças à antecipação do RF03.
