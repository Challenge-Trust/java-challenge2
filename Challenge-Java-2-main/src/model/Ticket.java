package model;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private int tickets;
    private Long usuarioId;
    private LocalDateTime dataEmissao;

    public Ticket(int tickets, Long usuarioId) {
        this.usuarioId = usuarioId;
        this.tickets = tickets;
        this.dataEmissao = LocalDateTime.now();
    }

    public Ticket() {
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
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

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
}

