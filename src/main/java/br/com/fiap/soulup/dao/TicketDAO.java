package br.com.fiap.soulup.dao;

import br.com.fiap.soulup.exception.BancoDeDadosException;
import br.com.fiap.soulup.factory.ConnectionFactory;
import br.com.fiap.soulup.model.Ticket;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acesso à tabela T_SU_PASSAGEM.
 */
@Repository
public class TicketDAO {

    private static final String COLUNAS = "id, usuarioId, tickets, dataEmissao";

    private final ConnectionFactory connectionFactory;

    public TicketDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Ticket insert(Ticket ticket) {
        String sql = "INSERT INTO T_SU_PASSAGEM (usuarioId, tickets, dataEmissao) VALUES (?, ?, ?)";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id"})) {
            stmt.setLong(1, ticket.getUsuarioId());
            stmt.setInt(2, ticket.getTickets());
            stmt.setTimestamp(3, Timestamp.valueOf(ticket.getDataEmissao()));
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    ticket.setId(chaves.getLong(1));
                }
            }
            return ticket;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir passagem", e);
        }
    }

    public List<Ticket> findAll() {
        return listar("SELECT " + COLUNAS + " FROM T_SU_PASSAGEM ORDER BY id", null);
    }

    public List<Ticket> findByUsuarioId(Long usuarioId) {
        return listar("SELECT " + COLUNAS + " FROM T_SU_PASSAGEM WHERE usuarioId = ? ORDER BY dataEmissao DESC", usuarioId);
    }

    public Optional<Ticket> findById(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM T_SU_PASSAGEM WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar passagem", e);
        }
    }

    public boolean update(Ticket ticket) {
        String sql = "UPDATE T_SU_PASSAGEM SET usuarioId = ?, tickets = ?, dataEmissao = ? WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, ticket.getUsuarioId());
            stmt.setInt(2, ticket.getTickets());
            stmt.setTimestamp(3, Timestamp.valueOf(ticket.getDataEmissao()));
            stmt.setLong(4, ticket.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao atualizar passagem", e);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM T_SU_PASSAGEM WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir passagem", e);
        }
    }

    private List<Ticket> listar(String sql, Long usuarioId) {
        List<Ticket> lista = new ArrayList<>();
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            if (usuarioId != null) {
                stmt.setLong(1, usuarioId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar passagens", e);
        }
    }

    private Ticket mapear(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getLong("id"));
        ticket.setUsuarioId(rs.getLong("usuarioId"));
        ticket.setTickets(rs.getInt("tickets"));
        ticket.setDataEmissao(rs.getTimestamp("dataEmissao").toLocalDateTime());
        return ticket;
    }
}
