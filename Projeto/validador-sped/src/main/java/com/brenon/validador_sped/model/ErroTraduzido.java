package com.brenon.validador_sped.model;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa um erro traduzido do SPED Fiscal.
 * Mapeia a tabela 'erros_traduzidos' no banco H2, contendo o código do erro,
 * a mensagem original do PVA, sua tradução humanizada e a orientação de correção.
 *
 * @author Brenon
 */
@Entity
@Table(name = "erros_traduzidos")
public class ErroTraduzido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_erro", length = 50)
    private String codigoErro;

    @Column(name = "campo", length = 100)
    private String campo;

    @Column(name = "mensagem_pva", length = 1000)
    private String mensagemPva;

    @Column(name = "mensagem_traduzida", length = 2000)
    private String mensagemTraduzida;

    @Column(name = "orientacao_correcao", length = 3000)
    private String orientacaoCorrecao;

    /** Construtor padrão exigido pelo JPA. */
    public ErroTraduzido() {
    }

    public ErroTraduzido(String codigoErro, String campo, String mensagemPva,
                         String mensagemTraduzida, String orientacaoCorrecao) {
        this.codigoErro = codigoErro;
        this.campo = campo;
        this.mensagemPva = mensagemPva;
        this.mensagemTraduzida = mensagemTraduzida;
        this.orientacaoCorrecao = orientacaoCorrecao;
    }

    // ======================== Getters ========================

    public Long getId() {
        return id;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensagemPva() {
        return mensagemPva;
    }

    public String getMensagemTraduzida() {
        return mensagemTraduzida;
    }

    public String getOrientacaoCorrecao() {
        return orientacaoCorrecao;
    }

    // ======================== Setters ========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigoErro(String codigoErro) {
        this.codigoErro = codigoErro;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public void setMensagemPva(String mensagemPva) {
        this.mensagemPva = mensagemPva;
    }

    public void setMensagemTraduzida(String mensagemTraduzida) {
        this.mensagemTraduzida = mensagemTraduzida;
    }

    public void setOrientacaoCorrecao(String orientacaoCorrecao) {
        this.orientacaoCorrecao = orientacaoCorrecao;
    }
}
