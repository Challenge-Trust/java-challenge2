package br.com.fiap.soulup.dao;

import br.com.fiap.soulup.exception.BancoDeDadosException;
import br.com.fiap.soulup.factory.ConnectionFactory;
import br.com.fiap.soulup.model.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acesso à tabela T_SU_USUARIO.
 */
@Repository
public class UsuarioDAO {

    private static final String COLUNAS = "id, nome, username, senha, cpf, viewMinutes";

    private final ConnectionFactory connectionFactory;

    public UsuarioDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Usuario insert(Usuario usuario) {
        String sql = "INSERT INTO T_SU_USUARIO (nome, username, senha, cpf, viewMinutes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id"})) {
            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getUserName());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getCpf());
            stmt.setInt(5, usuario.getViewMinutes());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setId(chaves.getLong(1));
                }
            }
            return usuario;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir usuário", e);
        }
    }

    public List<Usuario> findAll() {
        String sql = "SELECT " + COLUNAS + " FROM T_SU_USUARIO ORDER BY id";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar usuários", e);
        }
    }

    public Optional<Usuario> findById(Long id) {
        return buscarUm("SELECT " + COLUNAS + " FROM T_SU_USUARIO WHERE id = ?", id);
    }

    public Optional<Usuario> findByCpf(String cpf) {
        return buscarUm("SELECT " + COLUNAS + " FROM T_SU_USUARIO WHERE cpf = ?", cpf);
    }

    public boolean update(Usuario usuario) {
        String sql = "UPDATE T_SU_USUARIO SET nome = ?, username = ?, senha = ?, cpf = ?, viewMinutes = ? WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getUserName());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getCpf());
            stmt.setInt(5, usuario.getViewMinutes());
            stmt.setLong(6, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao atualizar usuário", e);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM T_SU_USUARIO WHERE id = ?";
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir usuário", e);
        }
    }

    private Optional<Usuario> buscarUm(String sql, Object parametro) {
        try (Connection conexao = connectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setObject(1, parametro);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar usuário", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setName(rs.getString("nome"));
        usuario.setUserName(rs.getString("username"));
        usuario.setPassword(rs.getString("senha"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setViewMinutes(rs.getInt("viewMinutes"));
        return usuario;
    }
}
