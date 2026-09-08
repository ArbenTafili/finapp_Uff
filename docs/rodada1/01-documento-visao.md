# Documento de Visão — FinApp

**Responsável:** Sara (PO)

## 1. Problema

Pessoas físicas têm dificuldade em manter um acompanhamento claro de para onde seu dinheiro está indo. Essa falta de visibilidade leva a gastos excessivos, baixo índice de poupança e dificuldade em alcançar objetivos financeiros. As alternativas existentes no mercado costumam ser complexas demais ou não oferecem um método simples e rápido de registro — o que faz muitos usuários abandonarem o hábito de controlar as finanças antes mesmo de criar consistência.


## 2. Objetivo e Proposta de Valor

O FinApp propõe uma solução web, simples e centralizada para registro de receitas e despesas, com foco em velocidade de uso (registrar uma transação em menos de 30 segundos) e em clareza visual dos padrões de consumo. A proposta de valor central é:
Dar ao usuário controle sobre sua vida financeira sem exigir esforço, tempo ou conhecimento técnico — o oposto das planilhas complexas e dos apps bancários sobrecarregados de funcionalidades que ele não usa.

## 3. Público-alvo / Personas

Definido a partir das personas levantadas na fase de Engenharia de Requisitos:
Persona	Perfil	Necessidade principal
Henrique Gonçalves - Estudante universitário, 23 anos, renda limitada. Controlar pequenos gastos diários com registro rápido.
Laura Varges - Jovem profissional, 27 anos, salário estável. Economizar para viagens e metas maiores; análises mais profundas.
O produto atende dois perfis complementares: o usuário que precisa de simplicidade no dia a dia e o usuário que demanda planejamento de médio prazo.

## 4. Principais Funcionalidades (visão de alto nível)

Registro de transações: Lançar receitas e despesas de forma rápida.
Organização por categorias: Categorias padrão e personalizadas.
Relatórios visuais: Entendimento dos padrões de consumo por mês e categoria.
Metas de economia: Definição e acompanhamento de objetivos financeiros.
Exportação de dados: Extração das transações (CSV) para uso em ferramentas externas.

## 5. Restrições Conhecidas

Restrição de equipe: Projeto executado por 6 pessoas com níveis de experiência distintos. A distribuição de tarefas considerará a curva de aprendizado, não apenas a divisão igualitária de esforço.
Restrição de prazo: Desenvolvimento limitado à duração de um semestre letivo; não há margem para escopo inflado ou retrabalho extenso.
Restrição tecnológica: Solução web compatível com Chrome, Firefox e Safari (RNF04). Sem sincronização bancária automática ou apps nativos nesta versão.
Restrição de dados: Armazenamento local por questões de privacidade (RNF03), limitando funcionalidades dependentes de back-ends centralizados robustos.

## 6. Critérios de Sucesso do Projeto

Entregar um MVP funcional com o RF01 (Gerenciar Transações) operando de ponta a ponta antes do fim do semestre.
Cumprir os requisitos não funcionais de usabilidade (RNF01) e desempenho (RNF02).
Validar com pelo menos uma das personas (ou perfil equivalente) que o fluxo de registro é percebido como rápido e simples.
Concluir o ciclo de modelagem, implementação e entrega dentro do prazo, sem reduzir o escopo essencial por atraso.

