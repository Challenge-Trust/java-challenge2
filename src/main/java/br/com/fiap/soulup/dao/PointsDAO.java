package br.com.fiap.soulup.dao;

import br.com.fiap.soulup.exception.BancoDeDadosException;
import br.com.fiap.soulup.factory.ConnectionFactory;
import br.com.fiap.soulup.model.Points;
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
 * Acesso à tabela T_SU_PONTOS.
 */
@Repository
public class PointsDAO {

    private static final String COLUNAS = "id, usuarioId, earnedPoints, dataAtualizacao";

    private final ConnectionFactory connectionFactory;

    public PointsDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Points insert(Points pontos) {
        String sql = "INSERT INTO T_SU_PONTOS (usuarioId, earnedPoints, dataAtualizacao) VALUES (?, ?, ?)";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id"})) {
            stmt.setLong(1, pontos.getUsuarioId());
            stmt.setInt(2, pontos.getEarnedPoints());
            stmt.setTimestamp(3, Timestamp.valueOf(pontos.getDataAtualizacao()));
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    pontos.setId(chaves.getLong(1));
                }
            }
            return pontos;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir pontos", e);
        }
    }

    public List<Points> findAll() {
        String sql = "SELECT " + COLUNAS + " FROM T_SU_PONTOS ORDER BY id";
        List<Points> lista = new ArrayList<>();
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar pontos", e);
        }
    }

    public Optional<Points> findById(Long id) {
        return buscarUm("SELECT " + COLUNAS + " FROM T_SU_PONTOS WHERE id = ?", id);
    }

    public Optional<Points> findByUsuarioId(Long usuarioId) {
        return buscarUm("SELECT " + COLUNAS + " FROM T_SU_PONTOS WHERE usuarioId = ?", usuarioId);
    }

    public boolean update(Points pontos) {
        String sql = "UPDATE T_SU_PONTOS SET usuarioId = ?, earnedPoints = ?, dataAtualizacao = ? WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, pontos.getUsuarioId());
            stmt.setInt(2, pontos.getEarnedPoints());
            stmt.setTimestamp(3, Timestamp.valueOf(pontos.getDataAtualizacao()));
            stmt.setLong(4, pontos.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao atualizar pontos", e);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM T_SU_PONTOS WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir pontos", e);
        }
    }

    private Optional<Points> buscarUm(String sql, Long parametro) {
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, parametro);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar pontos", e);
        }
    }

    private Points mapear(ResultSet rs) throws SQLException {
        Points pontos = new Points();
        pontos.setId(rs.getLong("id"));
        pontos.setUsuarioId(rs.getLong("usuarioId"));
        pontos.setEarnedPoints(rs.getInt("earnedPoints"));
        pontos.setDataAtualizacao(rs.getTimestamp("dataAtualizacao").toLocalDateTime());
        return pontos;
    }
}
