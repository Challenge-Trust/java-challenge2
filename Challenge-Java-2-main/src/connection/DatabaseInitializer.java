package connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public void criarTabelas() {
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100), username VARCHAR(30),senha VARCHAR(30), cpf VARCHAR(11), viewMinutes INT)";
        String sqlPontos = "CREATE TABLE  IF NOT EXISTS pontos(id BIGINT AUTO_INCREMENT PRIMARY KEY, usuarioId BIGINT REFERENCES usuario(id), " +
                "earnedPoints INT,dataAtualizacao TIMESTAMP)";
        String sqlPassagem = "CREATE TABLE IF NOT EXISTS passagem(id BIGINT AUTO_INCREMENT PRIMARY KEY, usuarioId BIGINT REFERENCES usuario(id), " +
                "tickets INT, dataEmissao TIMESTAMP)";
        String sqlCo2 = "CREATE TABLE IF NOT EXISTS co2(id BIGINT AUTO_INCREMENT PRIMARY KEY,usuarioId BIGINT REFERENCES usuario(id), " +
                "distanciaKm DOUBLE, co2Economizado DOUBLE, passageiroOnibus INT, dataCalculo TIMESTAMP)";

        try (Connection conexao = new ConnectionFactory().getConnection();
             Statement statement = conexao.createStatement()) {

            statement.execute(sqlUsuario);
            statement.execute(sqlPontos);
            statement.execute(sqlPassagem);
            statement.execute(sqlCo2);


        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

}
