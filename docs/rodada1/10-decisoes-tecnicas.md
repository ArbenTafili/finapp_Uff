Filipe (GP) — Papéis, cronograma, orçamento, monitoramento

Por que a distribuição de papéis que escolhemos? Argumentar que os papéis foram atribuídos considerando afinidade/experiência de cada um com a parte equivalente no projeto de ES anterior — isso reduz risco de curva de aprendizado e é uma decisão de alocação de recursos, não aleatória.
Por que o cronograma tem esse formato (Gantt) e não outro? Gantt permite visualizar dependências entre pacotes de trabalho e sobreposição de iterações — essencial pra um GP acompanhar se o projeto está "on track".
Por que já começamos o EVM/burndown na Rodada 1, mesmo com pouco código pronto? Porque monitoramento e controle não é algo que se faz só no fim — quanto antes se estabelece a baseline (PV), mais cedo desvios (SPI/CPI baixos) são detectados e corrigidos

Decisão técnica: Validação de data em transações (RF01/UC01)

O UC01 especifica que a regra de negócio deve impedir "datas anteriores ao cadastro". Na implementação, identificamos uma ambiguidade: o escopo do projeto (RF01-RF05) não inclui uma entidade Usuario com autenticação/data de cadastro, o que tornaria essa regra, lida literalmente, inviável de implementar sem expandir o escopo.

Avaliamos três interpretações possíveis:

Impedir apenas datas futuras (transação não pode ser posterior a hoje);
Impedir datas anteriores ao instante em que o registro foi criado no banco (sem "backdating");
Criar uma entidade Usuario simplificada apenas para guardar dataCadastro e validar contra ela.

Decisão tomada: optamos pela interpretação (1) — impedir apenas datas futuras.

Justificativa: as opções (2) e (3) comprometeriam a usabilidade esperada (RNF01 — registro rápido) e o caso de uso real das personas levantadas em ES (ex: usuário registrar hoje um gasto de ontem). A opção (3) também introduziria escopo não previsto no Planning Poker, indo contra o princípio de escopo controlado do projeto. A decisão prioriza a experiência de uso real sobre a leitura mais restritiva do texto do UC01.
