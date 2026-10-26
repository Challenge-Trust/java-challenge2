package br.com.fiap.soulup.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões JDBC com o Oracle.
 * URL, usuário e senha vêm das variáveis de ambiente DB_URL, DB_USER e DB_PASSWORD
 * (mapeadas no application.properties), nunca fixas no código.
 * Quem chama é responsável por fechar a conexão (try-with-resources nos DAOs).
 */
@Component
public class ConnectionFactory {

    private final String url;
    private final String user;
    private final String password;

    public ConnectionFactory(@Value("${app.db.url}") String url,
                             @Value("${app.db.user}") String user,
                             @Value("${app.db.password}") String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
