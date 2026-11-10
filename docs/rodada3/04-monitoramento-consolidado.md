# Monitoramento e controle consolidados — Iterações 1, 2 e 3

**Responsável:** Filipe (GP). Valores das Iterações 1 e 2 transcritos de [rodada1/07](../rodada1/07-monitoramento-controle.md) e da [Seção 7](../plano-projeto/secao-7-monitoramento-controle.md). **As células da Iteração 3 só podem ser preenchidas com as horas reais da equipe; nenhum valor foi estimado aqui.**

Baseline: 153 SP · 612 h · R$ 30.600 (R$ 50/h).

## EVM por iteração

| Iteração | PV (h) | EV (h) | AC (h) | SPI | CPI | CV (h) | SV (h) |
|---|---|---|---|---|---|---|---|
| 1 | 120 | 120 | 120 | 1,00 | 1,00 | 0 | 0 |
| 2 (acumulado I1+I2) | 300 | 304 | 310 | 1,01 | 0,98 | −6 | +4 |
| 3 (acumulado final) | 612 | ______ | ______ | ______ | ______ | ______ | ______ |

Fórmulas: SPI = EV/PV · CPI = EV/AC · CV = EV − AC · SV = EV − PV · EAC = BAC / CPI (BAC = 612 h).
Exemplo de preenchimento para conferir a conta: com AC = 625 h e EV = 612 h (escopo 100% entregue), CPI = 0,98, SPI = 1,00 e EAC ≈ 625 h. **Troquem pelos valores reais.**

Nota sobre a I1: o documento da Rodada 1 traz a tabela de EVM em R$ 0,00 e o burndown de 120 h; aqui adotou-se PV = EV = AC = 120 h (SPI = CPI = 1,00, conforme o status “No prazo / No custo” registrado). **(GP) confirmar.**

## Burndown

| Iteração | Dia / data | Restante planejado (h) | Restante real (h) |
|---|---|---|---|
| 1 | 01/09 | 120 | 120 |
| 1 | 03/09 | 108 | 114 |
| 1 | 06/09 | 96 | 98 |
| 1 | 10/09 | 84 | 80 |
| 1 | 14/09 | 0 | 0 |
| 2 | (copiar do slide da Rodada 2: `docs/slides/rodada2-apresentacao-dados.md`) | | |
| 3 | ______ | | |

Para o gráfico final, usar uma única série contínua (horas restantes do baseline de 612 h ao longo das três iterações) ou um gráfico por iteração; ambos os formatos atendem à entrega.

## Análise do GP — a preencher

- Situação final de prazo e custo (SPI/CPI), desvio total frente ao baseline e causas.
- Efeito das ações corretivas da Rodada 2 (Docker/PostgreSQL) sobre o CPI da I3.
- Itens do escopo entregues versus planejados (RF01–RF05: todos entregues nas branches de [histórico de versões](02-historico-versoes.md)).
- Riscos que se materializaram e os que não (ver [análise de riscos](../rodada2/analise-riscos-atualizada.md)).
