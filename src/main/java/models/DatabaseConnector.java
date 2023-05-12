package models;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {
    private MysqlDataSource dataSource;

    public DatabaseConnector(String url, int port, String database, String username, String password) {
        try {
            System.out.printf("Configuring data source...");
            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database +
                    "?serverTimezone=UTC");
            dataSource.setUseSSL(false);
            System.out.printf("done!\n");
        } catch (SQLException e) {
            System.out.printf("failed!\n");
            System.exit(0);
        }
    }

    public Connection getConnection() {
        try {
            System.out.printf("Fetching connection to database...");
            Connection connection = dataSource.getConnection();
            System.out.printf("done!\n");
            return connection;
        } catch (SQLException e) {
            System.out.printf("failed!\n");
            System.exit(0);
            return null;
        }
    }
}
