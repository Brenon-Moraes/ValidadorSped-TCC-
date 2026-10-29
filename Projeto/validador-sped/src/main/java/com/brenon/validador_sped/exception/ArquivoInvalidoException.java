package com.brenon.validador_sped.exception;

/**
 * Exceção personalizada lançada quando o arquivo SPED enviado
 * falha nas validações de formato, tamanho ou integridade.
 *
 * <p>Estende {@link RuntimeException} para ser tratada automaticamente
 * pelo {@link GlobalExceptionHandler} via {@code @ControllerAdvice}.</p>
 */
public class ArquivoInvalidoException extends RuntimeException {

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public ArquivoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
