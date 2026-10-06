-- Relatório mensal (RF03): filtra por intervalo de datas e agrupa por tipo/categoria.
CREATE INDEX idx_transacoes_data ON transacoes (data);

-- Consultas por categoria dentro de um período; também cobre a FK categoria_id,
-- usada na checagem de exclusão de categoria (existsByCategoriaId).
CREATE INDEX idx_transacoes_categoria_data ON transacoes (categoria_id, data);
