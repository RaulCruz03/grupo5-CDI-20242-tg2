package rjm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "senha";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Conexão com MariaDB bem-sucedida!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao MariaDB.");
            e.printStackTrace();
        }
    }
}
