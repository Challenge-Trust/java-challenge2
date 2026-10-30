package br.com.fiap.soulup.bo;

import br.com.fiap.soulup.dao.PointsDAO;
import br.com.fiap.soulup.dao.TicketDAO;
import br.com.fiap.soulup.dao.UsuarioDAO;
import br.com.fiap.soulup.dto.ConversaoResponse;
import br.com.fiap.soulup.model.Points;
import br.com.fiap.soulup.model.Ticket;
import br.com.fiap.soulup.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Regras de conversão da SoulUp:
 * a cada 180 minutos assistidos o usuário ganha 1 ponto e
 * a cada 10 pontos o saldo vira 1 passagem de transporte público.
 */
@Component
public class ConversaoBO {

    public static final int MINUTOS_POR_PONTO = 180;
    public static final int PONTOS_POR_PASSAGEM = 10;

    private final UsuarioDAO usuarioDAO;
    private final PointsDAO pointsDAO;
    private final TicketDAO ticketDAO;

    public ConversaoBO(UsuarioDAO usuarioDAO, PointsDAO pointsDAO, TicketDAO ticketDAO) {
        this.usuarioDAO = usuarioDAO;
        this.pointsDAO = pointsDAO;
        this.ticketDAO = ticketDAO;
    }

    /**
     * Converte os minutos do usuário em pontos e os pontos em passagens, gravando o resultado.
     * Os minutos e pontos que sobram ficam guardados para a próxima conversão.
     */
    public ConversaoResponse converter(Usuario usuario) {
        Points saldo = pointsDAO.findByUsuarioId(usuario.getId())
                .orElseGet(() -> pointsDAO.insert(new Points(usuario.getId(), 0)));

        int pontosGanhos = usuario.getViewMinutes() / MINUTOS_POR_PONTO;
        if (pontosGanhos > 0) {
            usuario.setViewMinutes(usuario.getViewMinutes() % MINUTOS_POR_PONTO);
            usuarioDAO.update(usuario);
        }

        int totalPontos = saldo.getEarnedPoints() + pontosGanhos;
        int passagens = totalPontos / PONTOS_POR_PASSAGEM;
        if (passagens > 0) {
            ticketDAO.insert(new Ticket(usuario.getId(), passagens));
            totalPontos = totalPontos % PONTOS_POR_PASSAGEM;
        }

        if (pontosGanhos > 0 || passagens > 0) {
            saldo.setEarnedPoints(totalPontos);
            saldo.setDataAtualizacao(LocalDateTime.now());
            pointsDAO.update(saldo);
        }
        return new ConversaoResponse(usuario.getId(), pontosGanhos, passagens,
                usuario.getViewMinutes(), saldo.getEarnedPoints());
    }
}
