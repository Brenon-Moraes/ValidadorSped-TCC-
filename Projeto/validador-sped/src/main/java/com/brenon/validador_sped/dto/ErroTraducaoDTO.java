package com.brenon.validador_sped.dto;

/**
 * DTO (Data Transfer Object) de resposta para o endpoint de pesquisa manual de erros.
 * Desacopla a camada de apresentação (API REST) da entidade JPA.
 */
public record ErroTraducaoDTO(
    Long id,
    String codigoErro,
    String campo,
    String mensagemPva,
    String mensagemTraduzida,
    String orientacaoCorrecao
) {}
