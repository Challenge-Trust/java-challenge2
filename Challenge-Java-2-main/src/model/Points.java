package model;

import java.time.LocalDateTime;

public class Points {
    private Long id;
    private Long usuarioId;
    private int earnedPoints;
    private LocalDateTime dataAtualizacao;

    public Points(int earnedPoints, Long usuarioId) {
        this.earnedPoints = earnedPoints;
        this.usuarioId = usuarioId;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Points(){

    }

    public int getPoints() {
        return earnedPoints;
    }

    public void setPoints(int points) {
        this.earnedPoints = points;
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
