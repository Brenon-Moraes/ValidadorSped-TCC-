package com.brenon.validador_sped.repository;

import com.brenon.validador_sped.model.ErroTraduzido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade {@link ErroTraduzido}.
 * Contém consultas derivadas para busca por código de erro, campo e mensagem PVA.
 */
@Repository
public interface ErroTraduzidoRepository extends JpaRepository<ErroTraduzido, Long> {

    /**
     * Busca erros cujo código contenha o termo informado (case-insensitive).
     */
    List<ErroTraduzido> findByCodigoErroContainingIgnoreCase(String codigoErro);

    /**
     * Busca abrangente: pesquisa o termo em código de erro, campo OU mensagem PVA.
     */
    List<ErroTraduzido> findByCodigoErroContainingIgnoreCaseOrCampoContainingIgnoreCaseOrMensagemPvaContainingIgnoreCase(
        String codigoErro, String campo, String mensagemPva
    );
}
