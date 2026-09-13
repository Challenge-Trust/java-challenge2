package model;

import java.time.LocalDateTime;

public class CO2Economia {
    private Long id;
    private Long usuarioId;
    private double distanciaKm, co2Economizado;
    private int passageiroOnibus;
    private LocalDateTime dataCalculo;

    public CO2Economia(Long usuarioId, double distanciaKm,
                       double co2Economizado, int passageiroOnibus) {
        this.usuarioId = usuarioId;
        this.distanciaKm = distanciaKm;
        this.co2Economizado = co2Economizado;
        this.passageiroOnibus = passageiroOnibus;
        this.dataCalculo = LocalDateTime.now();
    }

    public CO2Economia(){

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

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public double getCo2Economizado() {
        return co2Economizado;
    }

    public void setCo2Economizado(double co2Economizado) {
        this.co2Economizado = co2Economizado;
    }

    public int getPassageiroOnibus() {
        return passageiroOnibus;
    }

    public void setPassageiroOnibus(int passageiroOnibus) {
        this.passageiroOnibus = passageiroOnibus;
    }

    public LocalDateTime getDataCalculo() {
        return dataCalculo;
    }

    public void setDataCalculo(LocalDateTime dataCalculo) {
        this.dataCalculo = dataCalculo;
    }
}
