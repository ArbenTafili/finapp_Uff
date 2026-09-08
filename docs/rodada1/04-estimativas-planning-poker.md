# Estimativas de Esforço (Planning Poker) — FinApp

**Responsável:** Emanuel (Scrum Master)

Escala usada: Fibonacci (1, 2, 3, 5, 8, 13, 21)

| Pacote de Trabalho | Estimativa (pontos) | Observações |
|---|---|---|
| PT01 — Estrutura base do projeto | 3 | Configuração inicial, organização e convenções de desenvolvimento. |
| PT02 — Persistência local | 5 | Implementação do armazenamento local e operações básicas de leitura e gravação. |
| PT03 — Modelo de Transação | 3 | Entidade Transacao com atributos e validações básicas. |
| PT04 — Cadastro de Transações | 5 | Fluxo de inclusão de receitas e despesas com validações. |
| PT05 — Listagem de Transações | 3 | Visualização das transações cadastradas. |
| PT06 — Edição de Transações | 3 | Alteração dos dados de uma transação existente. |
| PT07 — Exclusão de Transações | 2 | Remoção de transações com manutenção da consistência. |
| PT08 — Cálculo de Saldo | 5 | Cálculo de saldo com atenção à precisão financeira. |
| PT09 — Modelo de Categoria | 2 | Entidade Categoria e identificação de categorias padrão/personalizadas. |
| PT10 — Categorias Padrão | 2 | Criação e carga das categorias iniciais. |
| PT11 — CRUD de Categorias Personalizadas | 5 | Criação, listagem, edição e exclusão de categorias. |
| PT12 — Integração Categoria x Transação | 5 | Integração das categorias válidas aos fluxos de transação. |
| PT13 — Agregação Mensal | 5 | Filtragem por mês e cálculo dos totais mensais. |
| PT14 — Agrupamento por Categoria | 5 | Agrupamento de despesas para os relatórios. |
| PT15 — Tela de Relatórios | 5 | Interface de visualização dos relatórios mensais. |
| PT16 — Gráfico de Gastos por Categoria | 5 | Representação visual da distribuição dos gastos. |
| PT17 — Navegação entre Meses | 3 | Consulta de relatórios de meses anteriores e posteriores. |
| PT18 — Otimização dos Relatórios | 8 | Ajustes para atender ao limite de carregamento definido. |
| PT19 — Modelo de Meta | 5 | Entidade Meta com valores, prazo e status. |
| PT20 — Cadastro de Metas | 3 | Criação de novas metas de economia. |
| PT21 — Listagem de Metas | 3 | Visualização das metas e informações principais. |
| PT22 — Edição e Exclusão de Metas | 5 | Alteração e exclusão/cancelamento de metas. |
| PT23 — Progresso Visual da Meta | 3 | Indicador visual baseado no progresso da meta. |
| PT24 — Controle de Status da Meta | 8 | Regras dos estados Em Andamento, Concluída, Vencida e Cancelada. |
| PT25 — Integração Receita x Meta | 8 | Atualização do acompanhamento de metas a partir das receitas. |
| PT26 — Filtro de Período para Exportação | 3 | Seleção do intervalo de datas para exportação. |
| PT27 — Geração de CSV | 5 | Geração do arquivo CSV com colunas padronizadas. |
| PT28 — Download do CSV | 2 | Disponibilização do arquivo gerado para o usuário. |
| PT29 — Validação do CSV | 5 | Validação de estrutura, conteúdo e compatibilidade. |
| PT30 — Testes de Cálculos Financeiros | 8 | Testes de saldo, totais, agrupamentos e demais cálculos. |
| PT31 — Testes de Desempenho | 5 | Verificação do limite de carregamento dos relatórios. |
| PT32 — Testes de Compatibilidade | 5 | Validação em Chrome, Firefox e Safari. |
| PT33 — Validação de Usabilidade | 3 | Verificação do fluxo de registro em menos de 30 segundos. |
| PT34 — Estabilização para Demo | 8 | Correções críticas, dados de demonstração e validação integrada. |

**Total estimado: 153 Story Points**

## Custo estimado

Para fins de estimativa financeira, foi adotada a seguinte premissa hipotética:

- **Conversão estimada:** 1 Story Point = 4 horas de trabalho
- **Custo/hora hipotético da equipe:** R$ 50,00

Assim:

- **Esforço estimado:** 153 SP × 4 h/SP = **612 horas**
- **Custo estimado:** 612 h × R$ 50,00/h = **R$ 30,600.00**