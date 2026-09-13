package dao;

import connection.ConnectionFactory;
import model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private Connection conexao;

    public TicketDAO() {
        conexao = new ConnectionFactory().getConnection();
    }

    public void adicionarTicket(Ticket ticket) {
        String sql = "INSERT INTO passagem (usuarioId, tickets, dataEmissao) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, ticket.getUsuarioId());
            stmt.setInt(2, ticket.getTickets());
            stmt.setTimestamp(3, Timestamp.valueOf(ticket.getDataEmissao()));
            stmt.executeUpdate();

            ResultSet chavesGeradas = stmt.getGeneratedKeys();
            if (chavesGeradas.next()) {
                ticket.setId(chavesGeradas.getLong(1));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public Ticket buscarPorId(Long id) {
        String sql = "SELECT * FROM passagem WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getLong("id"));
                ticket.setUsuarioId(rs.getLong("usuarioId"));
                ticket.setTickets(rs.getInt("tickets"));
                ticket.setDataEmissao(rs.getTimestamp("dataEmissao").toLocalDateTime());
                return ticket ;
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }

    public List<Ticket> listarTodos() {
        String sql = "SELECT * FROM passagem";
        List<Ticket> listaTicket = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getLong("id"));
                ticket.setUsuarioId(rs.getLong("usuarioId"));
                ticket.setTickets(rs.getInt("tickets"));
                ticket.setDataEmissao(rs.getTimestamp("dataEmissao").toLocalDateTime());
                listaTicket.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return listaTicket;
    }

    public void atualizarTicket(Ticket ticket) {
        String sql = "UPDATE passagem SET usuarioId = ?, tickets = ?, dataEmissao = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, ticket.getUsuarioId());
            stmt.setInt(2, ticket.getTickets());
            stmt.setTimestamp(3, Timestamp.valueOf(ticket.getDataEmissao()));
            stmt.setLong(4, ticket.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void excluirTicket(Ticket ticket) {
        String sql = "DELETE FROM passagem WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, ticket.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}