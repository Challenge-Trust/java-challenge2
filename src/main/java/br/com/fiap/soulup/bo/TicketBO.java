package br.com.fiap.soulup.bo;

import br.com.fiap.soulup.dao.TicketDAO;
import br.com.fiap.soulup.dto.TicketRequest;
import br.com.fiap.soulup.exception.RecursoNaoEncontradoException;
import br.com.fiap.soulup.exception.RegraNegocioException;
import br.com.fiap.soulup.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Regras das passagens emitidas (quantidade sempre positiva, usuário existente).
 */
@Component
public class TicketBO {

    private final TicketDAO ticketDAO;
    private final UsuarioBO usuarioBO;

    public TicketBO(TicketDAO ticketDAO, UsuarioBO usuarioBO) {
        this.ticketDAO = ticketDAO;
        this.usuarioBO = usuarioBO;
    }

    public List<Ticket> listar() {
        return ticketDAO.findAll();
    }

    public List<Ticket> listarPorUsuario(Long usuarioId) {
        usuarioBO.buscarEntidade(usuarioId);
        return ticketDAO.findByUsuarioId(usuarioId);
    }

    public Ticket buscarPorId(Long id) {
        return ticketDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Passagem " + id + " não encontrada."));
    }

    public Ticket cadastrar(TicketRequest dados) {
        validar(dados);
        return ticketDAO.insert(new Ticket(dados.usuarioId(), dados.tickets()));
    }

    public Ticket atualizar(Long id, TicketRequest dados) {
        Ticket ticket = buscarPorId(id);
        validar(dados);
        ticket.setUsuarioId(dados.usuarioId());
        ticket.setTickets(dados.tickets());
        ticketDAO.update(ticket);
        return ticket;
    }

    public void excluir(Long id) {
        if (!ticketDAO.delete(id)) {
            throw new RecursoNaoEncontradoException("Passagem " + id + " não encontrada.");
        }
    }

    private void validar(TicketRequest dados) {
        if (dados == null) {
            throw new RegraNegocioException("Corpo da requisição vazio.");
        }
        if (dados.tickets() == null || dados.tickets() <= 0) {
            throw new RegraNegocioException("Informe a quantidade de tickets (maior que 0).");
        }
        usuarioBO.buscarEntidade(dados.usuarioId());
    }
}
