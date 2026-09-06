# Governança, Planejamento e Monitoramento — FinApp

Documento de alinhamento metodológico do projeto FinApp para a disciplina de Gerência de Projetos.

---

## 1. Estrutura da Equipe e Alocação de Recursos

A distribuição dos papéis foi estabelecida como uma decisão formal de alocação de recursos orientada à redução de riscos. Os integrantes assumiram responsabilidades diretamente alinhadas à sua experiência prévia e contribuições consolidadas na fase de Engenharia de Software (ES):

| Integrante | Papel | Principais Responsabilidades |
| :--- | :--- | :--- |
| **Filipe** | Gerente de Projeto (GP) & Dev | Planejamento, controle de cronograma, baseline orçamentária, monitoramento de EVM/Burndown, consolidação dos artefatos e desenvolvimento técnico. |
| **Sara** | Product Owner (PO) | Gestão do Backlog, priorização de valor das histórias de usuário (RF01 a RF05), validação de critérios de aceite e alinhamento com stakeholders. |
| **Emanuel** | Scrum Master | Facilitação das cerimônias ágeis (Planning, Dailies, Retrospectivas), remoção de impedimentos e conformidade com o processo ágil. |
| **Arben** | Gerente de Configuração | Gestão de repositórios (Git/GitHub), branching model, pipelines de integração contínua (CI) e integridade das versões. |
| **Enzo** | Desenvolvedor | Implementação de arquitetura, desenvolvimento full stack, codificação dos modelos de dados e execução de testes. |
| **Giovana** | Desenvolvedora | Desenvolvimento frontend/backend, implementação de fluxos de navegação, diagramação comportamental e resolução de regras técnicas. |

> **Racional de Gestão:** A alocação baseada no histórico do projeto anterior elimina o tempo de ambientação e atenua a curva de aprendizado da equipe, garantindo maior previsibilidade nas estimativas de esforço.

---

## 2. Decisões Metodológicas

### 2.1 Formato do Cronograma (Gráfico de Gantt)
A representação temporal via Gráfico de Gantt foi selecionada por viabilizar a visualização clara de:
* Precedências e dependências técnicas entre pacotes de trabalho da EAP.
* Paralelismo e sobreposição de atividades entre desenvolvedores.
* Caminho crítico do projeto, permitindo ao GP identificar gargalos antes que afetem a entrega das rodadas.

### 2.2 Monitoramento Precoce desde a Rodada 1 (EVM e Burndown)
A inicialização das métricas de Earned Value Management (EVM) e gráficos de Burndown já na Rodada 1 baseia-se no princípio de que monitoramento é uma disciplina contínua. Estabelecer a linha de base de valor planejado (PV) desde o primeiro ciclo permite identificar desvios de prazo (SPI < 1) e custo (CPI < 1) de maneira preventiva, possibilitando intervenções corretivas imediatas.

---

## 3. Templates de Monitoramento e Controle

### 3.1 Acompanhamento de Iteração (Burndown Chart)

| Dia da Iteração | Data | Tarefas Concluídas | Esforço Restante Planejado (h) | Esforço Restante Real (h) | Observações |
| :---: | :---: | :---: | :---: | :---: | :--- |
| Dia 0 | DD/MM/AAAA | 0 | 120 | 120 | Início da Sprint |
| Dia 1 | DD/MM/AAAA | 2 | 108 | 114 | Ajustes de setup |
| Dia 2 | DD/MM/AAAA | 3 | 96 | 98 | |
| Dia 3 | DD/MM/AAAA | 5 | 84 | 80 | |
| Dia 4 | DD/MM/AAAA | 7 | 72 | 70 | |

---

### 3.2 Análise de Valor Agregado (EVM)

* **PV (Planned Value):** Valor planejado até o momento de medição.
* **EV (Earned Value):** Valor agregado correspondente ao percentual físico concluído.
* **AC (Actual Cost):** Custo real incorrido (horas reais trabalhadas).
* **SPI (Schedule Performance Index):** `EV / PV` (Valores < 1.0 indicam atraso).
* **CPI (Cost Performance Index):** `EV / AC` (Valores < 1.0 indicam estouro de custo).

| Iteração / Rodada | PV (R$) | EV (R$) | AC (R$) | SV (EV - PV) | CV (EV - AC) | SPI | CPI | Status Operacional |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **Rodada 1** | R$ 0,00 | R$ 0,00 | R$ 0,00 | R$ 0,00 | R$ 0,00 | 1.00 | 1.00 | No prazo / No custo |
| **Rodada 2** | R$ 0,00 | R$ 0,00 | R$ 0,00 | R$ 0,00 | R$ 0,00 | - | - | A planejar |
| **Rodada 3** | R$ 0,00 | R$ 0,00 | R$ 0,00 | R$ 0,00 | R$ 0,00 | - | - | A planejar |

---

## 4. Registro de Participação Individual e Horas

| ID EAP | Descrição da Atividade | Responsável | Rodada / Sprint | Horas Estimadas | Horas Realizadas | Variação (h) | Status |
| :---: | :--- | :--- | :---: | :---: | :---: | :---: | :--- |
| 1.1 | Configuração do Repositório e CI/CD | Arben | Rodada 1 | 6 | 5 | -1 | Concluído |
| 1.2 | Refinamento do Backlog e User Stories | Sara | Rodada 1 | 8 | 8 | 0 | Concluído |
| 1.3 | Planejamento da Rodada e Facilitação | Emanuel | Rodada 1 | 4 | 4 | 0 | Concluído |
| 1.4 | Estruturação de Classes e Entidades | Enzo | Rodada 1 | 12 | 14 | +2 | Concluído |
| 1.5 | Implementação da Lógica de Datas e Regras | Giovana | Rodada 1 | 10 | 10 | 0 | Concluído |
| 1.6 | Gestão de Baseline e Documentação de GP | Filipe | Rodada 1 | 10 | 9 | -1 | Concluído |
