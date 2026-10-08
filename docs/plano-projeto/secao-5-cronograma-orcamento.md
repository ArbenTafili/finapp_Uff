# Cronograma (Gantt) e Orçamento — Atualização Rodada 2

**Responsável:** Filipe (GP)

## 1. Cronograma Atualizado (Gantt)

O cronograma foi readequado para refletir a consolidação e aprovação dos módulos RF01 (Transações) e RF02 (Categorias). A entrega do RF03 (Relatório) foi puxada para frente (entrega parcial adiantada), garantindo margem de tempo para focar em Testes e Deploy na Rodada 3. O impacto de tempo causado pela adoção do Docker (RDT-01) foi contornado pelo paralelismo da equipe, conforme detalhado na distribuição de tarefas abaixo:

```mermaid
gantt
    title Cronograma Detalhado - Rodada 2
    dateFormat  YYYY-MM-DD
    
    section Giovana
    Concluir RF01 Transacoes          :a1, 2026-09-15, 6d
    Concluir RF02 Categorias e Seeds  :a2, after a1, 7d
    Integracao e Testes Unitarios     :a3, after a2, 6d
    
    section Enzo
    Docker e Schema RDT-01            :b1, 2026-09-15, 5d
    RF03 Queries de Agregacao         :b2, after b1, 8d
    RF03 Endpoint Relatorio           :b3, after b2, 6d
    
    section Arben
    Estrategia Branches e Auditoria   :c1, 2026-09-15, 4d
    Setup Issues no GitHub            :c2, after c1, 3d
    
    section Sara
    Revisar Backlog e Escopo          :d1, 2026-09-15, 5d
    Criterios de Aceite e Roteiro     :d2, 2026-09-28, 5d
    
    section Emanuel
    Atas das Cerimonias               :e1, 2026-09-15, 21d
    Atualizacao de Riscos             :e2, 2026-10-01, 4d
    
    section Filipe
    Monitoramento EVM e Burndown      :f1, 2026-10-04, 2d
    Atualizar Cronograma e Orcamento  :f2, 2026-10-04, 2d
    Slides da Rodada 2                :f3, 2026-10-04, 2d
    
    section Demonstracao
    Ensaio da Demo v2                 :g1, 2026-10-05, 1d
    Entrega da Rodada                 :milestone, m1, 2026-10-06, 0d
```

## 2. Orçamento Atualizado (Pós RDT-01)

- **Baseline Original:** 612 horas | Orçamento: R$ 30.600 (R$ 50/h).
- **Impacto da RDT-01:** Aproximadamente +8h extras divididas entre infraestrutura e documentação de contêineres Docker/PostgreSQL.
- **Orçamento Externo Variável:** R$ 0,00. As horas foram reabsorvidas dentro da capacidade do time, sem contratação externa.
- **Estimativa no Término (EAC):** Ajustada para ~624,5 horas ou **R$ 31.225**. O desvio de R$ 625 (2%) encontra-se dentro do contingenciamento aceitável e deve estabilizar na Iteração 3 devido à superação da curva de aprendizado.
