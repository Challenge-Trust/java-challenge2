package test;

import connection.DatabaseInitializer;

public class DatabaseInitializerTest {
    public static void main(String[] args){

    DatabaseInitializer inicializador = new DatabaseInitializer();
    inicializador.criarTabelas();

    }
}