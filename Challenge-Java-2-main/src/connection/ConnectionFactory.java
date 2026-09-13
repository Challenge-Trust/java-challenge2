package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    String urlConexao = "jdbc:h2:./banco/bancoDeDados;AUTO_SERVER=TRUE";
    String login = "sa";
    String senha = "";
    public Connection getConnection() {
        try {
            Connection conexao =
                    DriverManager.getConnection(urlConexao,login,senha);
            System.out.println("Conectado");
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

}
