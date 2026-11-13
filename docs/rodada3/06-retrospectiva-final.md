# Retrospectiva final

Reunião de retrospectiva gravada, com até 10 minutos, participação dos seis integrantes (ver [registro de participação](05-registro-participacao-consolidado.md)) e facilitação do Emanuel (Scrum Master). O vídeo da reunião é entregue junto com este relatório.

## Roteiro da reunião

| Min | Bloco | Quem conduz | Conteúdo |
|---|---|---|---|
| 0:00–0:45 | Abertura | Emanuel | Objetivo, regras (todos falam), contexto das 3 rodadas |
| 0:45–2:00 | Resultado do produto | Sara | Escopo entregue RF01–RF05 versus visão; aceite da Demo v3 |
| 2:00–3:15 | Demo curta | Giovana / Enzo | Passos 1, 6 e 9 do [roteiro](01-roteiro-demo-v3.md) (relatório, metas, CSV) |
| 3:15–4:30 | Números do projeto | Filipe | EVM e burndown consolidados ([monitoramento](04-monitoramento-consolidado.md)): o que desviou e por quê |
| 4:30–5:30 | Versões e processo técnico | Arben | Histórico de versões, branches, PRs, o conflito da Rodada 3 e como foi resolvido |
| 5:30–6:30 | Decisões técnicas e dificuldades | Enzo | Três decisões: PostgreSQL/Flyway/NUMERIC, progresso de metas, CSV/segurança ([dificuldades e decisões](03-dificuldades-decisoes.md)) |
| 6:30–8:30 | Retrospectiva (cada um 20–30 s) | todos | Começar a fazer / parar de fazer / continuar fazendo |
| 8:30–9:30 | Ações para um próximo ciclo e encerramento | Emanuel | 2–3 ações concretas |

## Pauta de discussão

- Distribuição de commits na `main` (Enzo 20, Filipe 14, Giovana 11, Sara 7, Arben 5, Emanuel 2): reflexo dos papéis (documentação e gestão) ou desbalanceamento.
- Desvio de cerca de 8 h com o setup de Docker e PostgreSQL na Iteração 2 e o efeito das ações corretivas na Iteração 3.
- Revisão de PRs e o fluxo `develop → main` das Rodadas 1 e 2 comparado à integração direta na `main` da Rodada 3.
- Deslocamento do RF04 (Metas) da Iteração 2 para a Iteração 3 no backlog.

## Lições aprendidas a partir dos registros do projeto

| Situação registrada | Lição para um próximo ciclo |
|---|---|
| O setup de Docker, PostgreSQL e Flyway foi feito na Rodada 2 e custou cerca de 8 h fora do plano (CPI de 0,98 na iteração). | Montar a infraestrutura de execução e o banco versionado já na primeira rodada. |
| A Demo v3 foi apresentada em 28/10, mas as entregas da Rodada 3 só entraram na `main` entre 09/11 e 13/11, com oito PRs no mesmo dia. | Integrar na `main` ao longo da rodada, com PRs menores. |
| Seis dos nove PRs da Rodada 3 foram aprovados pela mesma pessoa. | Distribuir as revisões entre os integrantes. |
| O perfil `h2` permitiu desenvolver sem Docker e serviu de plano B das demonstrações. | Manter um modo de execução simples e testado para contingência. |
| O teste do limite de 3 s do relatório (RNF02) entrou junto com o endpoint, na Rodada 2; já os testes do relatório sem banco e dos perfis `demo` e `h2` só vieram em 13/11 (#61). | Manter o hábito da Rodada 2: escrever os testes de cada requisito junto com a funcionalidade. |
