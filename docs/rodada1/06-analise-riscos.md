# Análise de Riscos — FinApp

**Responsável:** Emanuel (Scrum Master)

| # | Risco | Probabilidade | Impacto | Exposição (P×I) | Contenção (preventiva) | Contingência (se ocorrer) |
|---|---|---|---|---:|---|---|
| 1 | Erros nos cálculos de saldo, totais e agrupamentos financeiros | 4 — Alta | 5 — Muito alto | 20 | Centralizar as regras de cálculo e criar testes automatizados para saldo, totais mensais e agrupamentos. | Bloquear a entrega da funcionalidade afetada, corrigir a regra e executar novamente os testes financeiros. |
| 2 | Integração incorreta entre receitas e metas | 4 — Alta | 5 — Muito alto | 20 | Definir as regras de integração antes da implementação e criar testes de integração entre os módulos. | Desacoplar temporariamente a atualização automática das metas e corrigir o mecanismo de integração. |
| 3 | Atrasos na implementação dos módulos e na integração final | 4 — Alta | 5 — Muito alto | 20 | Priorizar funcionalidades centrais, acompanhar o progresso em Story Points e estabelecer marcos intermediários. | Repriorizar o backlog e postergar itens de menor prioridade que não sejam essenciais para a entrega. |
| 4 | Aumento não planejado do escopo | 4 — Alta | 4 — Alto | 16 | Controlar mudanças de escopo e avaliar impacto em prazo, esforço e prioridades antes de aprovar novas funcionalidades. | Retornar ao escopo-base aprovado e registrar solicitações adicionais para uma versão futura. |
| 5 | Grande volume de correções próximo à demonstração | 4 — Alta | 4 — Alto | 16 | Realizar testes e integração continuamente, corrigindo defeitos assim que forem identificados. | Priorizar defeitos bloqueadores e críticos e concentrar a estabilização nos fluxos essenciais para a demonstração. |
| 6 | Relatórios ultrapassarem o limite de 3 segundos | 3 — Média | 5 — Muito alto | 15 | Monitorar o desempenho durante o desenvolvimento e testar com dados representativos. | Otimizar os processamentos e priorizar as correções nos pontos que mais impactam o tempo de carregamento. |
| 7 | Perda ou inconsistência dos dados armazenados localmente | 3 — Média | 5 — Muito alto | 15 | Centralizar a persistência, validar os dados antes da gravação e testar criação, edição e exclusão. | Interromper operações afetadas, recuperar dados quando possível e corrigir o mecanismo de persistência antes da retomada. |
| 8 | Dependência excessiva de conhecimento concentrado em determinados membros | 3 — Média | 4 — Alto | 12 | Fazer revisões de código, documentar decisões técnicas e compartilhar conhecimento sobre componentes críticos. | Redistribuir tarefas e realizar transferência de conhecimento sobre o componente afetado. |
| 9 | Problemas de compatibilidade entre Chrome, Firefox e Safari | 3 — Média | 4 — Alto | 12 | Executar testes de compatibilidade durante o desenvolvimento, e não apenas na etapa final. | Priorizar correções das incompatibilidades que afetam os fluxos principais. |
| 10 | Falhas de comunicação e integração entre as tarefas da equipe | 3 — Média | 4 — Alto | 12 | Manter comunicação frequente, definir responsáveis e alinhar dependências entre as tarefas. | Reorganizar responsabilidades, resolver dependências críticas e realizar uma sincronização de alinhamento. |
| 11 | Complexidade maior que a prevista nas funcionalidades de metas | 3 — Média | 4 — Alto | 12 | Detalhar as regras de negócio e validar previamente os estados e transições da meta. | Dividir a funcionalidade em partes menores e priorizar o fluxo mínimo necessário para a entrega. |
| 12 | Fluxo de registro de transação não atingir a meta de 30 segundos | 3 — Média | 3 — Médio | 9 | Validar a usabilidade desde o desenvolvimento inicial e medir o tempo do fluxo com usuários de teste. | Simplificar o fluxo, reduzir etapas e corrigir pontos que aumentem o tempo de registro. |

## Matriz Probabilidade × Impacto

A exposição de cada risco é calculada pela fórmula:

**Exposição = Probabilidade × Impacto**

| Probabilidade \ Impacto | 1 — Muito baixo | 2 — Baixo | 3 — Médio | 4 — Alto | 5 — Muito alto |
|---|---:|---:|---:|---:|---:|
| **5 — Muito alta** | 5 | 10 | 15 | 20 | 25 |
| **4 — Alta** | 4 | 8 | 12 | 16 | 20 |
| **3 — Média** | 3 | 6 | 9 | 12 | 15 |
| **2 — Baixa** | 2 | 4 | 6 | 8 | 10 |
| **1 — Muito baixa** | 1 | 2 | 3 | 4 | 5 |

### Classificação da Exposição

| Faixa de Exposição | Classificação |
|---|---|
| 1–5 | Baixo |
| 6–10 | Moderado |
| 12–15 | Alto |
| 16–25 | Crítico |
