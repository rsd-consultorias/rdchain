package br.com.rsdconsultoria.rdcoin.infra.storage.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            // URL de conexão com o banco de dados
            String url = "jdbc:sqlite:/Users/rafaeldias/repositories/rdchain/rdchain/core/sqlite/banco.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Conexão realizada com sucesso!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        connect();
    }
}
