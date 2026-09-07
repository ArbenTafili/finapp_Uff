# Registro de Participação Individual e Papéis — FinApp

**Responsável:** Filipe (GP), com input de todos

## 1. Estrutura da Equipe e Alocação de Papéis
A distribuição dos papéis foi estabelecida como uma decisão formal de alocação de recursos orientada à redução de riscos. Os integrantes assumiram responsabilidades diretamente alinhadas à sua experiência prévia e contribuições consolidadas na fase de Engenharia de Software (ES). Esse racional metodológico elimina o tempo de ambientação, atenua a curva de aprendizado da equipe e garante maior previsibilidade nas estimativas de esforço.

| Integrante | Papel | Principais Responsabilidades |
|---|---|---|
| **Filipe** | Gerente de Projeto (GP) & Dev | Planejamento, controle de cronograma, baseline orçamentária, monitoramento de EVM/Burndown, consolidação dos artefatos e desenvolvimento técnico. |
| **Sara** | Product Owner (PO) | Gestão do Backlog, priorização de valor das histórias de usuário, validação de critérios de aceite e alinhamento com stakeholders. |
| **Emanuel** | Scrum Master | Facilitação das cerimônias ágeis (Planning, Dailies, Retrospectivas), remoção de impedimentos técnicos ou de comunicação e conformidade com o processo ágil. |
| **Arben** | Gerente de Configuração | Gestão de repositórios (Git/GitHub), branching model, versionamento de código, pipelines de integração contínua (CI) e integridade das versões. |
| **Enzo** | Desenvolvedor | Implementação de arquitetura, desenvolvimento full stack, codificação dos modelos de dados, execução de testes e estimativas técnicas. |
| **Giovana** | Desenvolvedora | Desenvolvimento frontend/backend, implementação de fluxos de navegação, diagramação comportamental e resolução de regras técnicas/negócio. |

## 2. Registro de Esforço e Horas (Iteração Atual)
A tabela abaixo rastreia o esforço real individual da equipe, contrastando as horas previstas no *Planning Poker* com o tempo efetivamente executado por pacote de trabalho (EAP).

| ID EAP | Descrição da Atividade | Responsável | Rodada / Sprint | Horas Estimadas | Horas Realizadas | Variação (h) | Status |
|---|---|---|---|---|---|---|---|
| 1.1 | Configuração do Repositório e CI/CD | Arben | Rodada 1 | 6 | 5 | -1 | Concluído |
| 1.2 | Refinamento do Backlog e User Stories | Sara | Rodada 1 | 8 | 8 | 0 | Concluído |
| 1.3 | Planejamento da Rodada e Facilitação | Emanuel | Rodada 1 | 4 | 4 | 0 | Concluído |
| 1.4 | Estruturação de Classes e Entidades | Enzo | Rodada 1 | 12 | 14 | +2 | Concluído |
| 1.5 | Implementação da Lógica de Datas e Regras | Giovana | Rodada 1 | 10 | 10 | 0 | Concluído |
| 1.6 | Gestão de Baseline e Documentação de GP | Filipe | Rodada 1 | 10 | 9 | -1 | Concluído |

**Observações da Rodada:**
* **Variação na EAP 1.4 (Enzo, +2h):** O acréscimo de 2 horas na estruturação das classes ocorreu devido a uma ambiguidade identificada no RF01/UC01 referente à restrição de "datas anteriores ao cadastro". O Enzo precisou pausar a implementação para alinhar a decisão técnica com a Giovana (refletida no documento ADR 001), o que consumiu mais tempo de modelagem e codificação do que o estimado no Planning Poker.
* **Compensação de Esforço:** O esforço adicional da EAP 1.4 foi absorvido e equilibrado pela execução mais rápida do que o previsto nas tarefas de configuração de CI/CD pelo Arben (EAP 1.1, -1h) e na documentação de GP pelo Filipe (EAP 1.6, -1h).
