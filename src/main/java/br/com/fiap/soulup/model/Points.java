package br.com.fiap.soulup.model;

import java.time.LocalDateTime;

/**
 * Saldo de pontos do usuário - tabela T_SU_PONTOS (1 registro por usuário).
 */
public class Points {

    private Long id;
    private Long usuarioId;
    private int earnedPoints;
    private LocalDateTime dataAtualizacao;

    public Points() {
    }

    public Points(Long usuarioId, int earnedPoints) {
        this.usuarioId = usuarioId;
        this.earnedPoints = earnedPoints;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
