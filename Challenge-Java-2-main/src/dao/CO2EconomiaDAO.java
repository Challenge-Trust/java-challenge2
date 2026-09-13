package dao;

import connection.ConnectionFactory;
import model.CO2Economia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CO2EconomiaDAO {
    private Connection conexao;

    public CO2EconomiaDAO() {
        conexao = new ConnectionFactory().getConnection();
    }

    public void adicionarCO2(CO2Economia co2Economia) {
        String sql = "INSERT INTO co2 (usuarioId, distanciaKm, co2Economizado, passageiroOnibus, dataCalculo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, co2Economia.getUsuarioId());
            stmt.setDouble(2, co2Economia.getDistanciaKm());
            stmt.setDouble(3, co2Economia.getCo2Economizado());
            stmt.setInt(4, co2Economia.getPassageiroOnibus());
            stmt.setTimestamp(5, Timestamp.valueOf(co2Economia.getDataCalculo()));
            stmt.executeUpdate();

            ResultSet chavesGeradas = stmt.getGeneratedKeys();
            if (chavesGeradas.next()) {
                co2Economia.setId(chavesGeradas.getLong(1));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public CO2Economia buscarPorId(Long id) {
            String sql = "SELECT * FROM co2 WHERE id = ?";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    CO2Economia co2Economia = new CO2Economia();
                    co2Economia.setId(rs.getLong("id"));
                    co2Economia.setUsuarioId(rs.getLong("usuarioId"));
                    co2Economia.setDistanciaKm(rs.getDouble("distanciaKm"));
                    co2Economia.setCo2Economizado(rs.getDouble("co2Economizado"));
                    co2Economia.setPassageiroOnibus(rs.getInt("PassageiroOnibus"));
                    co2Economia.setDataCalculo(rs.getTimestamp("dataCalculo").toLocalDateTime());
                    return co2Economia;
                }
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
            return null;
        }

    public List<CO2Economia> listarTodos() {
        String sql = "SELECT * FROM co2";
        List<CO2Economia> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CO2Economia co2Economia = new CO2Economia();
                co2Economia.setId(rs.getLong("id"));
                co2Economia.setUsuarioId(rs.getLong("usuarioId"));
                co2Economia.setDistanciaKm(rs.getDouble("distanciaKm"));
                co2Economia.setCo2Economizado(rs.getDouble("co2Economizado"));
                co2Economia.setPassageiroOnibus(rs.getInt("PassageiroOnibus"));
                co2Economia.setDataCalculo(rs.getTimestamp("dataCalculo").toLocalDateTime());
                lista.add(co2Economia);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return lista;
    }

    public void atualizarCO2(CO2Economia co2Economia) {
        String sql = "UPDATE co2 SET usuarioId = ?, distanciaKm = ?, co2Economizado = ?, passageiroOnibus = ?, dataCalculo = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, co2Economia.getUsuarioId());
            stmt.setDouble(2, co2Economia.getDistanciaKm());
            stmt.setDouble(3, co2Economia.getCo2Economizado());
            stmt.setInt(4, co2Economia.getPassageiroOnibus());
            stmt.setTimestamp(5, Timestamp.valueOf(co2Economia.getDataCalculo()));
            stmt.setLong(6, co2Economia.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void excluirCO2(CO2Economia co2Economia) {
        String sql = "DELETE FROM co2 WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, co2Economia.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}



