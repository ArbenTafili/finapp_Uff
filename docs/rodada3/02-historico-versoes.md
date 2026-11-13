# Histórico de versões — FinApp (Rodada 3)

Fonte: `git log origin/main --first-parent` (atualizado em 2026-11-13, depois da integração dos PRs #58 a #65 da Rodada 3). Os hashes e as tags permitem reproduzir cada versão com `git checkout <hash>` ou `git checkout <tag>`.

## Marcos do produto

| Versão | Tag | Marco | Commit na `main` | Conteúdo |
|---|---|---|---|---|
| Demo v1 | `v0.1.0` | Rodada 1 (início de set/2026) | `db71473` (merge do PR #9, `develop` → `main`, 07/09) | CRUD de transações e categorias, interface web simples, Docker |
| Demo v2 | `v0.2.0` | Rodada 2 (início de out/2026) | `7c96a25` (merge #55, 06/10) | RF01 e RF02 concluídos, Flyway/PostgreSQL, relatório mensal na API (RF03 parcial) |
| Demo v3 / final | `v1.0.0` | Rodada 3 (nov/2026) | Merge do PR #66 (este histórico) na `main` (13/11) | RF03 interface, RF04 metas, RF05 exportação CSV, gestão de categorias, deploy com conta de teste, tela de login (#62), testes ampliados (#61, suíte com 130 testes) e EAP revisada (#59) |

O código da Demo v1 entrou na `develop` pelo PR #2 (`d9e37a2`, 05/09) e chegou à `main` no merge do PR #9 (`db71473`). O código-fonte dos dois commits é idêntico; a tag `v0.1.0` fica no commit da `main`.

## Tags de versão

Tags anotadas criadas pelo Gerente de Configuração em 13/11/2026, depois de todos os PRs da Rodada 3 integrados à `main`:

```bash
git tag -a v0.1.0 db71473 -m "Demo v1 - Rodada 1"
git tag -a v0.2.0 7c96a25 -m "Demo v2 - Rodada 2"
git tag -a v1.0.0 origin/main -m "Entrega final - Demo v3 - Rodada 3"   # merge do PR #66
git push origin v0.1.0 v0.2.0 v1.0.0
```

Conferência: `git tag -n` lista as tags com a mensagem; `git show v1.0.0` mostra o commit da entrega final.

## Branches da Rodada 3

| Branch | PR | Entrega | Requisito / pacote EAP |
|---|---|---|---|
| `feature/rf03-interface-relatorio` | #58 (via `release/rodada3`) | Interface do relatório mensal: totais, rosca e barras por categoria, navegação entre meses | RF03 · 3.3.4–3.3.7 |
| `feature/rf05-exportacao-csv` | #58 (via `release/rodada3`) | `GET /api/transacoes/exportar?de=&ate=`, CSV pt-BR com proteção contra injeção de fórmulas, seleção de período na interface | RF05 · 3.5.1–3.5.5 · 4.4 |
| `feature/rf04-metas` | #58 (via `release/rodada3`) | Entidade `Meta`, migração V3, CRUD, cancelamento, progresso, status e página `metas.html` | RF04 · 3.4.1–3.4.9 · 4.1 |
| `feature/rf02-interface-categorias` | #58 (via `release/rodada3`) | Página `categorias.html` para categorias personalizadas | RF02 (acabamento) |
| `infra/deploy-conta-teste` | #58 (via `release/rodada3`) | Perfil `prod`, conta de teste (HTTP Basic), seed de demonstração, `render.yaml`, guia de deploy | 4.9 · Demo v3 |
| `docs/rodada3-entrega-final` | #58 (via `release/rodada3`) | Documentos consolidados da Rodada 3 | Entrega final |
| `release/rodada3` | #58 | Integração das branches acima (um conflito resolvido em `TransacaoRepository.kt`) | 1.1 |
| `test/60-testes-relatorio-demo` | #61 | 17 testes novos, sem alterar código de produção: regras do relatório mensal sem banco (`RelatorioServiceUnitTest`, 8), massa do perfil `demo` (`DemoDataSeederTest`, 5) e migrações no perfil `h2` do plano B (`PerfilH2Test`, 4) | 4.1–4.3 · issue #60 |
| `feature/#24-correcao-eap` | #59 | EAP em árvore única (diagrama Mermaid), mantendo os 54 pacotes de trabalho; ajustes no roteiro da Demo v3 e no registro de participação | 1.2 · 1.6 · issues #24 e #25 |
| `feature/tela-login` | #62 | Tela de login (`login.html`) no lugar do pop-up do HTTP Basic: `POST /api/login` cria sessão (cookie HttpOnly, SameSite=Lax), páginas sem sessão redirecionam ao login, API responde 401, botão Sair nas páginas; Basic segue aceito para clientes de API | 4.9 · Demo v3 |
| `docs/consolidacao-final-gp` | #63 | EVM acumulado final, burndowns por rodada e acumulado, Gantt, orçamento detalhado e PDF consolidado | 1.6 |
| `docs/rodada3-conferencia` | #64 | README e documento de dificuldades e decisões atualizados com o deploy e a autenticação | 4.9 · documentação |
| `docs/roteiro-demo-v3-login` | #65 | Roteiro da Demo v3 com acesso pela tela de login; suíte com 130 testes, 0 falhas (13/11) | 1.2 · 4.9 |
| `docs/historico-versoes-horas` | #66 | Histórico de versões, tags `v0.1.0`/`v0.2.0`/`v1.0.0` e horas da Rodada 3 | 1.1 · 1.6 |

Ordem de integração na `main` em 13/11: #61 → #59 → #62 → #63 → #64 → #65 → #66.

Convenção de commits mantida: `tipo(escopo): descrição #issue`. Nas Rodadas 1 e 2 o fluxo foi branch → PR → `develop` → `main`; na Rodada 3 as branches foram integradas direto na `main` por PR, com a `release/rodada3` concentrando as funcionalidades (ver `docs/adr/estrategia-branches.md`).

## Histórico da `main` (primeiro pai, do mais recente ao mais antigo)

| Commit | Data | Autor | Mensagem |
|---|---|---|---|
| `68b32ef` | 2026-11-13 | Sara | docs(demo): atualiza roteiro da Demo v3 (#65) |
| `5587a38` | 2026-11-13 | Giovana | docs: update README and difficulties document with deployment details and authentication changes (#64) |
| `0b77b4d` | 2026-11-13 | Filipe | docs(gestao): consolidacao final da Rodada 3 (EVM, burndown acumulado, graficos e PDF master) (#63) |
| `4b6ffde` | 2026-11-13 | Giovana | Feature/tela login (#62) |
| `defddbd` | 2026-11-13 | Sara | Feature/#24 correcao eap (#59) |
| `e463bea` | 2026-11-13 | Enzo | test: amplia testes do relatório mensal, do perfil demo e do perfil h2 #60 (#61) |
| `cab0d34` | 2026-11-09 | Giovana | feat: entrega final da Rodada 3 (RF03 interface, RF04 metas, RF05 CSV, deploy com conta de teste) (#58) |
| `7c96a25` · **v0.2.0** | 2026-10-06 | Enzo | Merge develop para a Main (#55) |
| `c86694f` | 2026-10-06 | Giovana | merge(config): integra develop na main (#17, #18, #16, #21) (#43) |
| `04494c4` | 2026-10-05 | Giovana | feat(transacao): valida tipo da transação contra a categoria #18 (#40) |
| `14fc19a` | 2026-10-05 | Enzo | docs(demo): preparação técnica e plano B da Demo v2 #29 (#38) |
| `8a7758b` | 2026-10-05 | Enzo | feat(relatorio): endpoint GET /api/relatorios?mes=AAAA-MM #20 (#37) |
| `4004db9` | 2026-10-05 | Enzo | feat(relatorio): consultas agregadas do relatório mensal #19 (#36) |
| `9f65346` | 2026-10-05 | Enzo | feat(db): schema versionado com Flyway, NUMERIC(19,2) e índices #15 (#35) |
| `f64e267` | 2026-10-05 | Enzo | chore(docker): revisa docker-compose e perfis do Spring #14 (#34) |
| `4fa4993` | 2026-10-05 | Arben | docs(adr): registra decisao PostgreSQL local #13 (#33) |
| `17ddea6` | 2026-10-05 | Arben | config(issues): adiciona templates da rodada 2 #12 (#32) |
| `7057346` | 2026-10-05 | Arben | docs(repositorio): atualiza estrutura e controle de versoes #11 (#31) |
| `725165a` | 2026-10-05 | Arben | docs(config): documenta estratégia de branches #10 (#30) |
| `e61386c` | 2026-09-08 | Giovana | Corrigindo slides |
| `8ac20b9` | 2026-09-08 | Giovana | adicionando slide da apresentacao |
| `db71473` · **v0.1.0** | 2026-09-07 | Enzo | Merge pull request #9 from ArbenTafili/develop |
| `48e13f5` | 2026-09-07 | Enzo | Merge pull request #8 from ArbenTafili/Filipe_Moreira_Docs |
| `c6c6685` | 2026-09-07 | Enzo | Merge pull request #7 from ArbenTafili/emanuel-scrum-master |
| `6af30b3` | 2026-09-07 | Enzo | Merge pull request #6 from ArbenTafili/Enzo-Sara-Escopo-Eap |
| `a54e6dc` | 2026-09-07 | Enzo | Update database information and team member name |
| `e4661f1` | 2026-09-07 | Enzo | Update developer role from 'Você' to 'Giovana' |
| `02514ce` | 2026-09-07 | Enzo | Merge pull request #5 from ArbenTafili/sara-documentacao-po |
| `27c210d` | 2026-09-07 | Sara | Revert |
| `6946458` | 2026-09-07 | Sara | docs: atualiza Escopo do Produto e EAP |
| `b93d3fd` | 2026-09-07 | Sara | docs: atualiza Documento de Visão do Produto |
| `9f00604` | 2026-09-06 | Filipe | Add governance and planning document for FinApp |
| `7d4fcdc` | 2026-09-06 | Filipe | Atualiza registro de decisões técnicas e de processo |
| `2d2b89f` | 2026-09-06 | Filipe | Add technical decisions documentation for Round 1 |
| `d201f88` | 2026-09-06 | Filipe | Delete docs/rodada1/docs directory |
| `2c4f717` | 2026-09-06 | Filipe | Add technical decisions and justifications in documentation |
| `e4a1360` | 2026-09-06 | Filipe | Create decisoes-tecnicas.md |
| `a3c4135` | 2026-09-01 | Arben | feat: estrutura inicial do projeto FinApp |

## Contribuições por autor (todos os commits da `main` até o PR #65)

| Integrante | Commits |
|---|---|
| Enzo | 20 |
| Filipe | 14 |
| Giovana | 11 |
| Sara | 7 |
| Arben | 5 |
| Emanuel | 2 |

O detalhamento de horas, inclusive as horas por branch e PR da Rodada 3, está no [registro de participação](05-registro-participacao-consolidado.md).
