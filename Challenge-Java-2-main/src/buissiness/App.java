package buissiness;

import model.Points;
import model.Ticket;
import model.Usuario;

public class App {
    private Usuario usuario;
    private Points points;
    private Ticket ticket;
    public App(Usuario usuario, Points points, Ticket ticket) {
        this.usuario = usuario;
        this.points = points;
        this.ticket = ticket;
    }
    public App() {
    }
    public int conversionViewsForPoints() {
        if (usuario.getViewMinutes() >= 180) {
            points.setPoints(points.getPoints() + usuario.getViewMinutes() / 180);
            usuario.setViewMinutes(usuario.getViewMinutes() % 180);
        }
        return points.getPoints();
    }
    public int conversionPointsToTickets() {
        if (points.getPoints() >= 10) {
            ticket.setTickets(ticket.getTickets() + points.getPoints() / 10);
            points.setPoints(points.getPoints() % 10);
        }
        return ticket.getTickets();
    }

    public String calcularSeloSustentavel(double totalCo2Economizado) {
        if (totalCo2Economizado >= 100) {
            return "Ouro";
        } else if (totalCo2Economizado >= 50) {
            return "Prata";
        } else {
            return "Bronze";
        }
    }
}
