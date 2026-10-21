package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConnection {
    private Logger log = LogManager.getLogger(DatabaseConnection.class);

    public Object initConnectionToBb(DatabaseType databaseType, String databaseName, String username, String password) throws SQLException {
        Object result;
        log.info("Connecting to DB: {}", databaseName);
        switch (databaseType) {
            case MYSQL:
                try {
                    return DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/" + databaseName,
                            username,
                            password);
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            default:
                throw new RuntimeException("Note support connertion to " + databaseType);
        }

    }
}
