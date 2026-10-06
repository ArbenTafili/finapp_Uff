# Textos de Pull Request — Iteração 2

Ordem de merge: **#17 → #18 → #16 → #21**, com Squash and Merge. Abra cada PR só depois do merge do anterior, rebaseando a branch na `main` antes (ver guia no chat).
Branches sugeridas: `feature/17-seed-categorias`, `feature/18-integracao-categoria-transacao`, `feature/16-concluir-rf01`, `test/21-regras-calculo`.

---

## Issue #17 — RF02: categorias padrão e CRUD de personalizadas

Closes #17

### O que mudou
- `DataSeeder` idempotente: cria cada categoria padrão só se (nome + tipo) não existir; reiniciar a API não duplica nada.
- Nome de categoria único por tipo, sem diferenciar maiúsculas (`409 Conflito`).
- Categorias padrão não podem ser **editadas** nem **excluídas** (`422`).
- Categoria personalizada com transações: não pode ser **excluída** nem ter o **tipo alterado** (`422`, mensagem com a quantidade de transações).
- Nova `ConflitoException` + handler no `GlobalExceptionHandler`.
- ADR 002 em `docs/rodada1/10-decisoes-tecnicas.md`.
- Testes: `DataSeederTest`, `CategoriaServiceTest` (unitários, MockK) e `CategoriaControllerTest` (integração).

### Como testar
1. `./gradlew test` (ou `docker compose up` e `GET /api/categorias`: 8 categorias com `ehPadrao: true`).
2. `POST /api/categorias {"nome":"Pets","tipo":"DESPESA"}` → 201; repetir com `"pets"` → 409.
3. `DELETE /api/categorias/{id de uma padrão}` → 422.
4. Criar transação na categoria "Pets" e tentar excluí-la → 422 com "1 transação(ões)".

---

## Issue #18 — Integração Categoria × Transação (PT12)

Closes #18

### O que mudou
- `TransacaoService` valida que o **tipo da transação = tipo da categoria** na criação e na edição (`422`, mensagem cita a categoria).
- Categoria inexistente continua retornando `404`.
- Testes de integração atualizados: o setup antigo pegava a primeira categoria qualquer; agora escolhe por tipo.

### Como testar
1. `POST /api/transacoes` com `tipo: RECEITA` e `categoriaId` de "Alimentação" → 422.
2. Mesmo corpo com categoria "Salário" → 201.
3. `PUT` mudando só o `tipo` para outro incompatível → 422 e a transação permanece inalterada.

---

## Issue #16 — Concluir RF01 (PT03–PT08)

Closes #16

### O que mudou
- **PT08 – Saldo:** `GET /api/transacoes/saldo` retorna `totalReceitas`, `totalDespesas` e `saldo`, calculados em `CalculoFinanceiro` só com `BigDecimal` (escala 2, sem arredondamento implícito).
- **Valor:** `@Digits(17,2)`; valores com mais de 2 casas retornam `400` em vez de serem arredondados em silêncio. Valor salvo sempre com 2 casas.
- **Descrição:** aparada; em branco vira `null`.
- **Erros padronizados** (`GlobalExceptionHandler`): JSON malformado ou `tipo`/`data` inválidos → `400`; id não numérico na URL → `400`.
- `Clock` injetável, para testar "data ≤ hoje" (ADR 001) sem depender do dia.
- Testes: `TransacaoServiceTest` (MockK) e `TransacaoControllerTest` (CRUD completo, validações, erros, saldo).

### Como testar
1. `./gradlew test`.
2. `POST /api/transacoes` com `"valor": 10.999` → 400; com `10.5` → 201 e resposta `10.50`.
3. Criar receita 0.10 + receita 0.20 + despesa 0.05 → `GET /api/transacoes/saldo` soma 0.25 exatos.
4. `GET /api/transacoes/abc` → 400; `POST` com `"tipo":"X"` → 400.

---

## Issue #21 — Testes unitários das regras de cálculo (PT30–PT34)

Closes #21

### O que mudou
- `CalculoFinanceiroTest`: lista vazia, saldo/total por tipo, saldo negativo, 1000 × R$ 0,01 = R$ 10,00 (prova contra erro de ponto flutuante), valores grandes, consistência `saldo = receitas − despesas`.
- `TransacaoTest`: sinal de `impactoNoSaldo()`.

### Como testar
`./gradlew test --tests 'com.finapp.service.CalculoFinanceiroTest' --tests 'com.finapp.model.TransacaoTest'`
