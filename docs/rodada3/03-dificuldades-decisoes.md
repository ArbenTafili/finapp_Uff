# Dificuldades encontradas e decisões técnicas e de processo

Consolida os registros das rodadas anteriores ([ADRs](../rodada1/10-decisoes-tecnicas.md), [RDT-01](../adr/RDT-01.md), [estratégia de branches](../adr/estrategia-branches.md)) e acrescenta as decisões da Rodada 3. Itens marcados **(equipe)** pedem a confirmação de quem viveu a situação antes da entrega.

## Linha do tempo

| Rodada | Dificuldade | Decisão / resposta | Registro |
|---|---|---|---|
| 1 | Requisito UC01 pedia bloquear “datas anteriores ao cadastro”, mas não há entidade `Usuario` no escopo | Bloquear apenas datas futuras (`data <= hoje`); evita scope creep e preserva o registro retroativo | ADR 001 |
| 1 | Nem todo ambiente de desenvolvimento tem Docker **(equipe: confirmar)** | H2 em memória como perfil padrão de desenvolvimento; PostgreSQL via Docker na execução integrada | README |
| 2 | RNF03 (dados locais) e persistência: H2 não representa o ambiente real | PostgreSQL local via Docker Compose, perfis `h2`, `dev`, `docker`, `test` | RDT-01 |
| 2 | Valores monetários com risco de erro de arredondamento (risco nº 1) | `NUMERIC(19,2)` + `BigDecimal` em todo o fluxo; schema versionado com Flyway (baseline para bancos da Demo v1) | Issue #15 |
| 2 | Regras de exclusão/edição de categoria não especificadas | Bloquear exclusão com transações, categorias padrão imutáveis, nome único por tipo, tipo da transação igual ao da categoria | ADR 002 |
| 2 | Setup Docker/PostgreSQL consumiu ~8 h não planejadas (CPI 0,98) | Ambiente dockerizado e documentado; esperado não se repetir | Monitoramento, Seção 7 |
| 2 | Muitas branches e PRs paralelos | Fluxo branch → PR → `develop` → `main`, templates de issue, auditoria do repositório | docs/adr/estrategia-branches.md |
| 3 | RF04 não define de onde vem o “valor economizado” | **Progresso = receitas − despesas desde a criação da meta até o prazo**, calculado (não gravado). Reaproveita as transações (3.4.9), evita aportes manuais e não duplica dados | abaixo |
| 3 | Percentual arredondado para cima mostraria “100%” antes de atingir o alvo | Percentual **truncado** a 2 casas e limitado a 100 | `CalculoMetaTest` |
| 3 | Estado “Vencida” e “Concluída” podem coexistir | Ordem de precedência: Cancelada > Concluída > Vencida > Em andamento; só o cancelamento é persistido | `CalculoMeta` |
| 3 | CSV precisa abrir corretamente no Excel em pt-BR | Separador `;`, UTF-8 com BOM, datas `dd/MM/aaaa`, vírgula decimal, CRLF; campos escapados (RFC 4180) | `ExportacaoService` |
| 3 | Descrições digitadas pelo usuário podem executar fórmulas ao abrir o CSV (`=`, `+`, `-`, `@`) | Prefixar com apóstrofo e testar | `ExportacaoServiceTest` |
| 3 | Nomes de categoria/descrição entravam no `innerHTML` da interface (XSS) | Escape de texto em todas as páginas | `index.html`, `metas.html`, `categorias.html` |
| 3 | Safari não suporta `input type="month"` (RNF04) | Navegação mensal própria (‹ mês ›), sem o componente nativo | `index.html` |
| 3 | “Conta de teste” para a versão publicada, sem entidade `Usuario` no escopo | Credencial única com tela de login (sessão por cookie; Basic mantido para API), configurada por variáveis de ambiente, ligada só no perfil `prod`; **não** é cadastro de usuários | `AcessoTesteFilter` |
| 3 | Branches paralelas editando os mesmos arquivos | Páginas novas separadas (`metas.html`, `categorias.html`) reduziram conflitos; o único conflito (consultas em `TransacaoRepository`) foi resolvido numa branch de integração (`release/rodada3`) | histórico de versões |

## Decisões da Rodada 3 em detalhe

### D3-01 — Progresso de metas derivado das transações

- **Contexto:** RF04 exige “acompanhar visualmente o progresso” e a EAP 3.4.9 pede integração Transações × Metas.
- **Opções:** (a) campo `valorAtual` atualizado manualmente; (b) vincular transações a uma meta; (c) calcular o saldo do período.
- **Decisão:** (c). É a opção sem novo fluxo de cadastro, sempre consistente com o saldo e testável com dados puros.
- **Consequências:** o progresso é do usuário inteiro, não de uma conta separada por meta; se o usuário gastar, o progresso cai (e uma meta concluída pode ser reaberta enquanto o prazo não terminou — após o prazo, só entram transações até a data limite). Metas concluídas não podem ser editadas ou canceladas.

### D3-02 — Exportação gerada no servidor

- **Contexto:** a Demo v2 exportava no navegador, só do que estava filtrado na tela.
- **Decisão:** endpoint `GET /api/transacoes/exportar?de=&ate=` com período explícito (3.5.1–3.5.4) e testes de conteúdo (3.5.5, 4.4).

### D3-03 — Conta de teste sem sistema de usuários

- **Decisão:** tela de login (`/login.html`) com um par usuário/senha por ambiente, sessão por cookie HttpOnly/SameSite=Lax e botão Sair; HTTP Basic segue aceito só para clientes de API. Mantém RNF03 e o escopo; a senha é gerada na hospedagem e nunca versionada.
- **Limitação assumida:** todos que têm a credencial compartilham os dados; sem proteção contra tentativas repetidas de senha (força bruta) nem token CSRF além do SameSite=Lax do cookie. Adequado à demonstração, não a produção real.

## Para completar (equipe)

- Dificuldades de processo da Rodada 3 vividas por vocês (comunicação, prazos, revisão de PR, divisão do trabalho). **(equipe)**
- Impacto real das decisões acima no esforço da I3, com as horas do [monitoramento](04-monitoramento-consolidado.md). **(equipe)**
