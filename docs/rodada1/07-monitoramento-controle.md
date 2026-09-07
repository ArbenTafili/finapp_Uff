# Monitoramento e Controle — FinApp

**Responsável:** Filipe (GP)

## Justificativa de Monitoramento
A inicialização das métricas de Earned Value Management (EVM) e gráficos de Burndown já na Rodada 1 baseia-se no princípio de que monitoramento é uma disciplina contínua. Estabelecer a linha de base de valor planejado (PV) desde o primeiro ciclo permite identificar desvios de prazo (SPI < 1) e custo (CPI < 1) de maneira preventiva, possibilitando intervenções corretivas imediatas.

## Burndown (Rodada 1)

*Acompanhamento diário das horas restantes planejadas vs. realizadas na iteração atual.*

| Dia da Iteração | Data | Tarefas Concluídas | Esforço Restante Planejado (h) | Esforço Restante Real (h) | Observações |
|---|---|---|---|---|---|
| Dia 0 | 01/09 | 0 | 120 | 120 | Início da Sprint |
| Dia 1 | 03/09 | 2 | 108 | 114 | Ajustes de setup |
| Dia 2 | 06/09 | 3 | 96 | 98 | |
| Dia 3 | 10/09 | 5 | 84 | 80 | |
| Dia 4 | 14/09 | 7 | 0 | 0 | Fim da Sprint |

**Observações do Burndown da Rodada:**
O leve desvio no Dia 1 (114h reais vs 108h planejadas) ocorreu devido a ajustes iniciais de setup da infraestrutura e alinhamento do ambiente de desenvolvimento. O ritmo foi normalizado no Dia 2. O ganho de produtividade no Dia 3 permitiu absorver o tempo extra gasto com o alinhamento de regras de negócio (validação de datas do RF01), garantindo o fechamento da sprint dentro da baseline planejada.

## EVM (Earned Value Management) - Rodada 1

| Métrica | Descrição | Valor |
|---|---|---|
| **PV (Planned Value)** | Valor planejado até o momento de medição. | R$ 0,00 |
| **EV (Earned Value)** | Valor agregado correspondente ao percentual físico concluído. | R$ 0,00 |
| **AC (Actual Cost)** | Custo real incorrido (horas reais trabalhadas). | R$ 0,00 |
| **SV (Schedule Variance)** | Variação de Prazos (EV - PV). | R$ 0,00 |
| **CV (Cost Variance)** | Variação de Custos (EV - AC). | R$ 0,00 |
| **SPI (EV/PV)** | Índice de Desempenho de Prazo (> 1.0 indica adiantado). | 1.00 |
| **CPI (EV/AC)** | Índice de Desempenho de Custo (> 1.0 indica economia). | 1.00 |

**Status Operacional da Rodada:** No prazo / No custo.

**Análise do GP:** 
*(Exemplo: O projeto encerra a Rodada 1 com SPI de 1.0 e CPI de 1.0, indicando que estamos rigorosamente dentro do prazo e do orçamento previstos).*
