# Histórico de versões — FinApp (Rodada 3)

Fonte: `git log origin/main --first-parent` (gerado em 2026-11-09, antes da integração das branches da Rodada 3). Os hashes permitem reproduzir cada versão com `git checkout <hash>`.

## Marcos do produto

| Versão | Marco | Referência no histórico | Conteúdo |
|---|---|---|---|
| Demo v1 | Rodada 1 (início de set/2026) | `d9e37a2` (merge do PR #2, 05/09) | CRUD de transações e categorias, interface web simples, Docker |
| Demo v2 | Rodada 2 (início de out/2026) | `7c96a25` (merge #55, 06/10) | RF01 e RF02 concluídos, Flyway/PostgreSQL, relatório mensal na API (RF03 parcial) |
| Demo v3 / final | Rodada 3 (nov/2026) | Merge das branches abaixo na `main` | RF03 interface, RF04 metas, RF05 exportação CSV, gestão de categorias, deploy com conta de teste |

Para registrar os marcos como versões no repositório (feito pelo Gerente de Configuração):

```bash
git tag -a v0.1.0 d9e37a2 -m "Demo v1"
git tag -a v0.2.0 7c96a25 -m "Demo v2"
git tag -a v1.0.0 <hash-do-merge-final-na-main> -m "Entrega final - Demo v3"
git push origin --tags
```

## Branches da Rodada 3

| Branch | Entrega | Requisito / pacote EAP |
|---|---|---|
| `feature/rf03-interface-relatorio` | Interface do relatório mensal: totais, rosca e barras por categoria, navegação entre meses | RF03 · 3.3.4–3.3.7 |
| `feature/rf05-exportacao-csv` | `GET /api/transacoes/exportar?de=&ate=`, CSV pt-BR com proteção contra injeção de fórmulas, seleção de período na interface | RF05 · 3.5.1–3.5.5 · 4.4 |
| `feature/rf04-metas` | Entidade `Meta`, migração V3, CRUD, cancelamento, progresso, status e página `metas.html` | RF04 · 3.4.1–3.4.9 · 4.1 |
| `feature/rf02-interface-categorias` | Página `categorias.html` para categorias personalizadas | RF02 (acabamento) |
| `infra/deploy-conta-teste` | Perfil `prod`, conta de teste (HTTP Basic), seed de demonstração, `render.yaml`, guia de deploy | 4.9 · Demo v3 |
| `release/rodada3` | Integração das quatro primeiras (um conflito resolvido em `TransacaoRepository.kt`) | — |
| `docs/rodada3-entrega-final` | Documentos consolidados da Rodada 3 | Entrega final |

Convenção de commits mantida: `tipo(escopo): descrição #issue`, fluxo branch → PR → `develop` → `main` (ver `docs/adr/estrategia-branches.md`).

## Histórico da `main` (primeiro pai, do mais recente ao mais antigo)

| Commit | Data | Autor | Mensagem |
|---|---|---|---|
| `7c96a25` | 2026-10-06 | Enzo | Merge develop para a Main (#55) |
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
| `db71473` | 2026-09-07 | Enzo | Merge pull request #9 from ArbenTafili/develop |
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

## Contribuições por autor (todos os commits da `main`)

| Integrante | Commits |
|---|---|
| Enzo | 19 |
| Filipe | 13 |
| Giovana | 8 |
| Arben | 5 |
| Sara | 5 |
| Emanuel | 2 |

O detalhamento de horas está no [registro de participação](05-registro-participacao-consolidado.md).
