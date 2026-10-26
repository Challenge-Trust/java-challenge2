package br.com.fiap.soulup.model;

import java.time.LocalDateTime;

/**
 * Passagens de transporte emitidas para o usuário - tabela T_SU_PASSAGEM.
 */
public class Ticket {

    private Long id;
    private Long usuarioId;
    private int tickets;
    private LocalDateTime dataEmissao;

    public Ticket() {
    }

    public Ticket(Long usuarioId, int tickets) {
        this.usuarioId = usuarioId;
        this.tickets = tickets;
        this.dataEmissao = LocalDateTime.now();
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

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
}
