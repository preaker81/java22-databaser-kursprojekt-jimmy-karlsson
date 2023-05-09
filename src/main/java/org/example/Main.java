package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;

public class Main {
    static MysqlDataSource dataSource;
    static String url = "localhost";
    static int port = 3306;
    static String database = "testdblaragon";
    static String username = "root";
    static String password = "";

    public static void main(String[] args) {
        InitializeDatabase();
        GetConnection();

    }

    //Konfigurerar kopplingar mot databasen
    public static void InitializeDatabase() {
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

    //Skapar en tillfällig koppling till databasen
    public static Connection GetConnection() {
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

