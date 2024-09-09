package br.com.rsdconsultoria.rdcoin.infra.storage.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteQuery {
    public static void selectAll() {
        String sql = "SELECT id, nome, email FROM usuarios";

        try (Connection conn = SQLiteConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre os resultados
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("nome") + "\t" +
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        selectAll();
    }
}
