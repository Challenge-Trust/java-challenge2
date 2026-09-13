package test;

import business.App;
import business.CO2Emission;
import connection.DatabaseInitializer;
import dao.CO2EconomiaDAO;
import dao.PontosDAO;
import dao.TicketDAO;
import dao.UsuarioDAO;
import model.CO2Economia;
import model.Points;
import model.Ticket;
import model.Usuario;

import java.util.List;

public class ConectionTest {
    public static void main(String[] args) {

        // 1. Garante que as tabelas existem
        new DatabaseInitializer().criarTabelas();

        // 2. Cria e salva um usuário
        Usuario usuario = new Usuario("Ana Silva", "ana123", "senha123", "12345678900", 200);
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.adicionarUsuario(usuario);
        System.out.println("Usuário criado, id: " + usuario.getId());

        // 3. Aplica a regra de negócio: minutos assistidos -> pontos
        Points points = new Points(usuario.getId(), 0);
        Ticket ticket = new Ticket(0, usuario.getId());
        App app = new App(usuario, points, ticket);

        app.conversionViewsForPoints();
        PontosDAO pontosDAO = new PontosDAO();
        pontosDAO.adicionarPontos(points);
        System.out.println("Pontos gerados: " + points.getPoints());

        // 4. Aplica a regra: pontos -> passagens
        app.conversionPointsToTickets();
        TicketDAO ticketDAO = new TicketDAO();
        ticketDAO.adicionarTicket(ticket);
        System.out.println("Passagens geradas: " + ticket.getTickets());

        // 5. Calcula economia de CO2 e salva
        CO2Emission calculadora = new CO2Emission();
        double economia = calculadora.calculateSavings(15.0, 40); // 15km, 40 passageiros no ônibus
        CO2Economia registroCo2 = new CO2Economia(usuario.getId(), 15.0, economia, 40);
        CO2EconomiaDAO co2DAO = new CO2EconomiaDAO();
        co2DAO.adicionarCO2(registroCo2);
        System.out.println("CO2 economizado: " + economia);

        // 6. Regra de negócio 4: selo de sustentabilidade
        String selo =   app.calcularSeloSustentavel(economia);
        System.out.println("Selo do usuário: " + selo);

        // 7. Demonstra leitura (READ)
        Usuario usuarioLido = usuarioDAO.buscarPorId(usuario.getId());
        System.out.println("Usuário lido do banco: " + usuarioLido.getName());

        List<Usuario> todosUsuarios = usuarioDAO.listarTodos();
        System.out.println("Total de usuários no banco: " + todosUsuarios.size());

        // 8. Demonstra atualização (UPDATE)
        usuario.setViewMinutes(500);
        usuarioDAO.atualizarUsuario(usuario);
        System.out.println("Minutos atualizados: " + usuarioDAO.buscarPorId(usuario.getId()).getViewMinutes());

        // 9. Demonstra exclusão (DELETE) —