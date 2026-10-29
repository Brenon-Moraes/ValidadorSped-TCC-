package com.brenon.validador_sped.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

/**
 * Manipulador global de exceções da aplicação.
 *
 * <p>Centraliza o tratamento de erros via {@code @ControllerAdvice},
 * eliminando a necessidade de blocos try-catch repetidos nos controllers.
 * Retorna respostas JSON padronizadas com campo {@code "message"}.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata erros de validação do arquivo SPED (formato, tamanho, integridade).
     */
    @ExceptionHandler(ArquivoInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleArquivoInvalido(ArquivoInvalidoException ex) {
        logger.warn("Validação de arquivo falhou: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

    /**
     * Trata tentativas de upload acima do limite configurado (20MB).
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleTamanhoExcedido(MaxUploadSizeExceededException ex) {
        logger.warn("Upload excedeu o tamanho máximo permitido: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("message", "O arquivo excede o tamanho máximo permitido (20MB)."));
    }

    /**
     * Trata qualquer exceção não prevista, evitando exposição de stack traces ao cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleErroGenerico(Exception ex) {
        logger.error("Erro interno inesperado na aplicação", ex);
        return ResponseEntity
                .internalServerError()
                .body(Map.of("message", "Ocorreu um erro interno inesperado. Tente novamente."));
    }
}
