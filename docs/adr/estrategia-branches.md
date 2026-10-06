# Estratégia de Branches do FinApp

## Contexto

O FinApp utiliza Git e GitHub para controle de versão e colaboração entre os integrantes da equipe.

Durante a Rodada 1 foi utilizada uma branch `develop`. Para a Rodada 2, considerando que a equipe possui seis integrantes, o projeto possui apenas três iterações e o prazo restante é curto, foi adotado o **GitHub Flow** como estratégia principal.

A branch `develop` da Rodada 1 será mantida apenas como histórico e não será utilizada para novos desenvolvimentos.

## Estratégia adotada: GitHub Flow

A branch `main` representa a versão integrada e estável do projeto.

Toda nova alteração deve partir da `main` através de uma branch de curta duração associada a uma Issue do GitHub.

O fluxo padrão será:

`Issue → Branch → Commits → Pull Request → Review → Squash and Merge → main`

## Convenção de branches

As branches deverão seguir o padrão:

`tipo/numero-da-issue-descricao`

Tipos utilizados:

- `feature/` — implementação de funcionalidades;
- `fix/` — correção de defeitos;
- `infra/` — infraestrutura e configuração;
- `test/` — testes;
- `docs/` — documentação;
- `config/` — configuração do projeto e do repositório.

Exemplos:

- `infra/14-docker-compose`
- `infra/15-schema-numeric`
- `feature/17-seed-categorias`
- `feature/19-rf03-agregacoes`
- `test/21-regras-calculo`
- `docs/28-slides-rodada2`

O número utilizado deve ser sempre o número real da Issue no GitHub.

## Convenção de commits

Os commits deverão seguir o padrão:

`tipo(escopo): descrição #issue`

Exemplos:

- `feat(categoria): adiciona categorias padrão #17`
- `fix(database): ajusta campos monetários para NUMERIC(19,2) #15`
- `test(relatorio): adiciona testes das regras de cálculo #21`
- `docs(config): documenta estratégia de branches #10`

## Pull Requests

Alterações na branch `main` deverão ocorrer através de Pull Request.

Cada Pull Request deverá:

- estar associado a uma Issue sempre que aplicável;
- possuir pelo menos uma aprovação de integrante diferente do autor;
- utilizar `Closes #N` na descrição para vincular e encerrar a Issue correspondente;
- possuir discussões resolvidas antes da integração;
- ter como destino a branch `main`.

Exemplo:

`Closes #17`

## Política de merge

A integração dos Pull Requests será realizada através de **Squash and Merge**.

Essa estratégia mantém a `main` com um histórico mais simples, consolidando os commits intermediários de uma branch em um único commit lógico.

Após o merge, a branch de trabalho deverá ser removida.

## Proteção da main

A branch `main` deverá permanecer protegida com as seguintes regras:

- Pull Request obrigatório;
- pelo menos uma aprovação;
- resolução das conversas antes do merge;
- force push desabilitado;
- exclusão da branch desabilitada.

## Branch develop

A branch `develop`, utilizada durante a Rodada 1, já teve seu conteúdo integrado à `main`.

A partir da Rodada 2, ela não será utilizada no fluxo de desenvolvimento, permanecendo no repositório apenas como histórico.