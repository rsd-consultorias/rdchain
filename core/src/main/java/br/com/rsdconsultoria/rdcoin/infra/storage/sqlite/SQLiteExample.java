package br.com.rsdconsultoria.rdcoin.infra.storage.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteExample {
    public static void createNewTable(Connection conn) {
        // SQL para criar uma nova tabela
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (\n"
                + " id integer PRIMARY KEY,\n"
                + " nome text NOT NULL,\n"
                + " email text NOT NULL\n"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            // Cria a nova tabela
            stmt.execute(sql);
            System.out.println("Tabela criada com sucesso!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertData(Connection conn) {
        String sql = "INSERT INTO usuarios(id, nome, email) VALUES(1, 'João', 'joao@example.com'),"
                + "(2, 'Maria', 'maria@example.com');";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Dados inseridos com sucesso!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conn = SQLiteConnection.connect();
        createNewTable(conn);
        insertData(conn);
    }
}
