package dao;

import connection.ConnectionFactory;
import model.Usuario;
import org.h2.command.dml.Delete;
import org.h2.command.query.Select;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO(){
      conexao = new ConnectionFactory().getConnection();
    }

    public void adicionarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, username, senha, cpf, viewMinutes) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getUserName());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getCpf());
            stmt.setInt(5, usuario.getViewMinutes());

            stmt.executeUpdate();
            ResultSet chavesGeradas = stmt.getGeneratedKeys();
            if (chavesGeradas.next()) {
                Long idGerado = chavesGeradas.getLong(1);
                usuario.setId(idGerado);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {                           // 3. "anda" pra primeira linha — achou algo?
                Usuario usuario = new Usuario();        // 4. cria o objeto vazio
                usuario.setId(rs.getLong("id"));        //    e preenche campo por campo, lendo do rs
                usuario.setName(rs.getString("nome"));
                usuario.setUserName(rs.getString("username"));
                usuario.setPassword(rs.getString("senha"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setViewMinutes(rs.getInt("viewMinutes"));
                return usuario;                         // 5. devolve o objeto montado
            }

        }catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();

                usuario.setId(rs.getLong("id"));
                usuario.setName(rs.getString("nome"));
                usuario.setUserName(rs.getString("username"));
                usuario.setPassword(rs.getString("senha"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setViewMinutes(rs.getInt("viewMinutes"));

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return usuarios;
    }

    public void deleteUsuario(Usuario usuario){
        String sql = "DELETE FROM usuario WHERE id = ?";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void atualizarUsuario(Usuario usuario){
        String sql = "UPDATE usuario SET coluna1 = ?, coluna2 = ?, coluna3 = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getUserName());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getCpf());
            stmt.setInt(5, usuario.getViewMinutes());

            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }


    }
}
