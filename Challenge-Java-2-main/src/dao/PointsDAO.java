package dao;

import connection.ConnectionFactory;
import model.Points;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PointsDAO {
    private Connection conexao;

    public PointsDAO() {
        conexao = new ConnectionFactory().getConnection();
    }

    public void adicionarPontos(Points pontos) {
        String sql = "INSERT INTO pontos (usuarioId, earnedPoints, dataAtualizacao) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, pontos.getUsuarioId());
            stmt.setInt(2, pontos.getPoints());
            stmt.setTimestamp(3, Timestamp.valueOf(pontos.getDataAtualizacao()));
            stmt.executeUpdate();

            ResultSet chavesGeradas = stmt.getGeneratedKeys();
            if (chavesGeradas.next()) {
                pontos.setId(chavesGeradas.getLong(1));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public Points buscarPorId(Long id) {
        String sql = "SELECT * FROM pontos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Points pontos = new Points();
                pontos.setId(rs.getLong("id"));
                pontos.setUsuarioId(rs.getLong("usuarioId"));
                pontos.setPoints(rs.getInt("earnedPoints"));
                pontos.setDataAtualizacao(rs.getTimestamp("dataAtualizacao").toLocalDateTime());
                return pontos;
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }

    public List<Points> listarTodos() {
        String sql = "SELECT * FROM pontos";
        List<Points> listaPontos = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Points pontos = new Points();
                pontos.setId(rs.getLong("id"));
                pontos.setUsuarioId(rs.getLong("usuarioId"));
                pontos.setPoints(rs.getInt("earnedPoints"));
                pontos.setDataAtualizacao(rs.getTimestamp("dataAtualizacao").toLocalDateTime());
                listaPontos.add(pontos);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return listaPontos;
    }

    public void atualizarPontos(Points pontos) {
        String sql = "UPDATE pontos SET usuarioId = ?, earnedPoints = ?, dataAtualizacao = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, pontos.getUsuarioId());
            stmt.setInt(2, pontos.getPoints());
            stmt.setTimestamp(3, Timestamp.valueOf(pontos.getDataAtualizacao()));
            stmt.setLong(4, pontos.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void excluirPontos(Points pontos) {
        String sql = "DELETE FROM pontos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, pontos.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}