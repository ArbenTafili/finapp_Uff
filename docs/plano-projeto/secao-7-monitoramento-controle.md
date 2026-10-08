# Monitoramento e Controle (Rodada 2)

**Responsável:** Filipe (GP)

## EVM (Earned Value Management) Acumulado - Iteração 1 + 2

| Métrica | Descrição | Valor |
|---|---|---|
| **PV (Planned Value)** | Valor planejado até o momento de medição. | 300h |
| **EV (Earned Value)** | Valor agregado correspondente ao percentual físico concluído. | 304h |
| **AC (Actual Cost)** | Custo real incorrido (horas reais trabalhadas). | 310h |
| **SPI (EV/PV)** | Índice de Desempenho de Prazo (> 1.0 indica adiantado). | 1,01 |
| **CPI (EV/AC)** | Índice de Desempenho de Custo (> 1.0 indica economia). | 0,98 |
| **CV (EV - AC)** | Variação de Custos. | -6h (-R$ 300) |
| **SV (EV - PV)** | Variação de Prazos. | +4h (+R$ 200) |
| **EAC (Estimate at Completion)** | Estimativa de custo total no término. | 624,5h (~R$ 31.225) |

*(Nota: O Baseline do projeto é de 153 SP / 612 horas / R$ 30.600 a R$ 50/h).*

## Interpretação do Gráfico de Burndown da Iteração 2

- **Situação:** O cronograma apresenta-se levemente adiantado (SPI de 1,01), com entregas parciais antecipadas. No entanto, há um ligeiro desvio de custo acumulado (CPI de 0,98), o qual encontra-se contido dentro do limite operacional seguro.
- **Causa do Desvio:** Foram alocadas aproximadamente 8 horas de esforço adicional (não previstas inicialmente) na configuração e testes de ambiente via Docker/PostgreSQL (RDT-01), além da auditoria técnica das branches do repositório.
- **Ação Corretiva:** O ambiente agora encontra-se totalmente dockerizado e documentado. A curva de aprendizado de setup foi superada e não deverá se repetir, o que garante a estabilização da produtividade (e o retorno do CPI à meta) para a Iteração 3.
