package com.brenon.validador_sped.dto;

/**
 * DTO (Data Transfer Object) de resposta para o endpoint de upload/análise do arquivo SPED.
 * Contém informações da linha processada e o erro traduzido correspondente.
 *
 * <p>Utiliza Java {@code record} para garantir imutabilidade e geração automática
 * de getters, equals, hashCode e toString.</p>
 */
public record ResultadoAnaliseDTO(
    int numeroLinha,
    String codigoRegistro,
    String codigoErro,
    String campo,
    String mensagemPva,
    String mensagemTraduzida,
    String orientacaoCorrecao
) {}
