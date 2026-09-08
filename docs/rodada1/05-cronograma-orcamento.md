# Cronograma (Gantt) e Orçamento — FinApp

**Responsável:** Filipe (GP)

## Justificativa Metodológica
A representação temporal via Gráfico de Gantt foi selecionada por viabilizar a visualização clara de precedências e dependências técnicas entre pacotes de trabalho da EAP, além do paralelismo entre desenvolvedores. Essa clareza visual é essencial para que o GP identifique o caminho crítico do projeto e acompanhe se as iterações estão *on track*, permitindo ajustes rápidos de rota em caso de gargalos.

## Cronograma

```mermaid
gantt
    title Cronograma do Projeto FinApp
    dateFormat  YYYY-MM-DD
    section Rodada 1
    Planejamento & Setup       :a1, 2026-09-01, 14d
    Demo v1                    :milestone, m1, 2026-09-14, 0d
    section Rodada 2
    Execução Backend & Front   :a2, 2026-09-15, 21d
    Configuração & Testes      :a3, after a2, 7d
    Demo v2                    :milestone, m2, 2026-10-13, 0d
    section Rodada 3
    Ajustes Finais             :a4, 2026-10-14, 14d
    Entrega Final & Demo v3    :milestone, m3, 2026-10-28, 0d
