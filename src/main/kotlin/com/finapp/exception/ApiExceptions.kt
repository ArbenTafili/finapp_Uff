package com.finapp.exception

class RecursoNaoEncontradoException(mensagem: String) : RuntimeException(mensagem)

class RegraDeNegocioException(mensagem: String) : RuntimeException(mensagem)

class ParametroInvalidoException(val parametro: String, mensagem: String) : RuntimeException(mensagem)

/** Conflito com um recurso já existente (ex.: categoria duplicada). */
class ConflitoException(mensagem: String) : RuntimeException(mensagem)
