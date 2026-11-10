# Demo v3 — roteiro e critérios de aceite

**Produto:** FinApp com RF01–RF05 integrados. **Duração sugerida:** 4 a 5 minutos dentro da gravação (limite total do vídeo: 10 min).

Acesso: versão publicada com a conta de teste (ver [deploy-conta-teste.md](deploy-conta-teste.md)) ou, se local, `docker compose up --build`.

## Roteiro

| # | Passo | Resultado esperado | Requisito |
|---|---|---|---|
| 1 | Abrir o endereço e informar a conta de teste | Tela principal no mês corrente, com saldo, rosca e barras de despesas por categoria | RF03 |
| 2 | Clicar em ‹ para voltar três meses e em “Mês atual” | Totais e gráfico mudam por mês; ‹ › funcionam mesmo em meses sem dados | RF03 (3.3.6) |
| 3 | Cadastrar uma receita e uma despesa de hoje | Mensagem de sucesso; totais e gráfico atualizam sem recarregar | RF01 |
| 4 | Tentar data futura ou valor com 3 casas decimais | Erro claro, nada é gravado | RF01 / RNF05 |
| 5 | Em “gerenciar”, criar a categoria “Pets” (despesa) e usá-la em uma despesa | Categoria aparece no formulário e no gráfico; ao tentar excluí-la com transações, a API recusa | RF02 |
| 6 | Abrir “Metas de economia”, criar “Viagem” (R$ 2.000, prazo futuro) | Meta em andamento; progresso vem do saldo desde a criação | RF04 |
| 7 | Registrar uma receita grande e voltar às metas | Barra sobe; ao atingir o alvo, status “Concluída” | RF04 / 3.4.9 |
| 8 | Cancelar uma meta | Status “Cancelada”, histórico preservado | RF04 |
| 9 | Em “Exportar CSV”, escolher o período e baixar; abrir no Excel/LibreOffice | Colunas Data, Tipo, Categoria, Descrição, Valor; acentos corretos; só o período escolhido | RF05 |

## Critérios de aceite

- RF01–RF05 executáveis de ponta a ponta no passo a passo acima.
- Relatório carrega em menos de 3 s (RNF02): o tempo de cada carregamento aparece no console do navegador (`Relatório AAAA-MM carregado em N ms`).
- Valores sempre com 2 casas e sem arredondamento implícito (RNF05): coberto por `CalculoFinanceiroTest`, `CalculoMetaTest` e `ExportacaoServiceTest`.
- Funciona em Chrome, Firefox e Safari (RNF04): a interface não usa `input type="month"` nem recursos sem suporte no Safari. **Conferir manualmente nos três navegadores antes da gravação.**
- Suíte automatizada verde: `./gradlew test` (exige Docker para o PostgreSQL dos testes; 109 testes na integração das branches).

## Plano B

Se a hospedagem estiver fora do ar na hora da gravação: `docker compose up --build`, depois `FINAPP_SEED_DEMO=true` no serviço `finapp` para carregar dados de exemplo (guia de deploy, seção “Demonstração local”).
