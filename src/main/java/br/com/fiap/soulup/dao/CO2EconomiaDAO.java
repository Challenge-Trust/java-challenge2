package br.com.fiap.soulup.dao;

import br.com.fiap.soulup.exception.BancoDeDadosException;
import br.com.fiap.soulup.factory.ConnectionFactory;
import br.com.fiap.soulup.model.CO2Economia;
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
 * Acesso à tabela T_SU_CO2.
 */
@Repository
public class CO2EconomiaDAO {

    private static final String COLUNAS =
            "id, usuarioId, distanciaKm, co2Economizado, passageiroOnibus, dataCalculo";

    private final ConnectionFactory connectionFactory;

    public CO2EconomiaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public CO2Economia insert(CO2Economia co2) {
        String sql = "INSERT INTO T_SU_CO2 (usuarioId, distanciaKm, co2Economizado, passageiroOnibus, dataCalculo) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id"})) {
            stmt.setLong(1, co2.getUsuarioId());
            stmt.setDouble(2, co2.getDistanciaKm());
            stmt.setDouble(3, co2.getCo2Economizado());
            stmt.setInt(4, co2.getPassageiroOnibus());
            stmt.setTimestamp(5, Timestamp.valueOf(co2.getDataCalculo()));
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    co2.setId(chaves.getLong(1));
                }
            }
            return co2;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir cálculo de CO2", e);
        }
    }

    public List<CO2Economia> findAll() {
        return listar("SELECT " + COLUNAS + " FROM T_SU_CO2 ORDER BY id", null);
    }

    public List<CO2Economia> findByUsuarioId(Long usuarioId) {
        return listar("SELECT " + COLUNAS + " FROM T_SU_CO2 WHERE usuarioId = ? ORDER BY dataCalculo DESC", usuarioId);
    }

    public Optional<CO2Economia> findById(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM T_SU_CO2 WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar cálculo de CO2", e);
        }
    }

    public boolean update(CO2Economia co2) {
        String sql = "UPDATE T_SU_CO2 SET usuarioId = ?, distanciaKm = ?, co2Economizado = ?, "
                + "passageiroOnibus = ?, dataCalculo = ? WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, co2.getUsuarioId());
            stmt.setDouble(2, co2.getDistanciaKm());
            stmt.setDouble(3, co2.getCo2Economizado());
            stmt.setInt(4, co2.getPassageiroOnibus());
            stmt.setTimestamp(5, Timestamp.valueOf(co2.getDataCalculo()));
            stmt.setLong(6, co2.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao atualizar cálculo de CO2", e);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM T_SU_CO2 WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir cálculo de CO2", e);
        }
    }

    private List<CO2Economia> listar(String sql, Long usuarioId) {
        List<CO2Economia> lista = new ArrayList<>();
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
            throw new BancoDeDadosException("Erro ao listar cálculos de CO2", e);
        }
    }

    private CO2Economia mapear(ResultSet rs) throws SQLException {
        CO2Economia co2 = new CO2Economia();
        co2.setId(rs.getLong("id"));
        co2.setUsuarioId(rs.getLong("usuarioId"));
        co2.setDistanciaKm(rs.getDouble("distanciaKm"));
        co2.setCo2Economizado(rs.getDouble("co2Economizado"));
        co2.setPassageiroOnibus(rs.getInt("passageiroOnibus"));
        co2.setDataCalculo(rs.getTimestamp("dataCalculo").toLocalDateTime());
        return co2;
    }
}
