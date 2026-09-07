## Escopo do Produto (requisitos)

### RF01 — Gerenciar Transações
Permitir que o usuário registre receitas e despesas informando valor, tipo (receita/despesa), categoria, data e descrição opcional. O sistema deve permitir também edição e exclusão das transações cadastradas.

Para a validação da data, será adotada a regra de que a transação pode possuir data passada ou a data atual, mas **não pode possuir data futura** (`data <= hoje`). Essa interpretação preserva a possibilidade de registrar despesas retroativas sem introduzir autenticação ou entidade de usuário fora do escopo atual.

### RF02 — Gerenciar Categorias
Disponibilizar categorias padrão pré-carregadas e permitir que o usuário crie, edite e exclua categorias personalizadas.

### RF03 — Visualizar Relatórios
Permitir a visualização de relatórios mensais contendo total de receitas, total de despesas, distribuição de gastos por categoria e navegação entre meses.

### RF04 — Gerenciar Metas de Economia
Permitir que o usuário crie metas de economia com valor alvo e prazo, acompanhe visualmente seu progresso e possa editar ou excluir/cancelar metas existentes.

### RF05 — Exportar Dados
Permitir a exportação das transações para arquivo CSV, com seleção de intervalo de datas e colunas padronizadas.

### Requisitos não funcionais relacionados

- **RNF01 — Usabilidade:** o registro de uma transação deve poder ser realizado em menos de 30 segundos.
- **RNF02 — Desempenho:** o carregamento dos relatórios deve ocorrer em menos de 3 segundos.
- **RNF03 — Segurança/Privacidade:** os dados do FinApp devem permanecer no ambiente da aplicação, sem sincronização automática com serviços bancários externos.
- **RNF04 — Compatibilidade:** a interface web deve ser compatível com Chrome, Firefox e Safari.
- **RNF05 — Confiabilidade:** os cálculos financeiros devem preservar precisão e consistência.

## Limites de escopo

O desenvolvimento desta versão está limitado aos requisitos RF01 a RF05 e RNF01 a RNF05.

Não fazem parte desta EAP:

- autenticação e gerenciamento de contas de usuário;
- sincronização automática com contas bancárias;
- controle de investimentos;
- gestão avançada de cartões de crédito;
- planejamento orçamentário futuro detalhado;
- gestão de empréstimos ou dívidas;
- aplicativos móveis nativos.

A ausência de autenticação é intencional nesta fase. Não será criada uma entidade `Usuario` apenas para suportar regras que podem ser atendidas sem expansão do escopo.
