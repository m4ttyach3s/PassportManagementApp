package com.progetto.packController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/PassaportiCompleto";
    private final String username = "postgres";
    private final String password = "root";
    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, username, password);
        }
        return connection;
        //return DriverManager.getConnection(DB_URL, username, password);
    }
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
