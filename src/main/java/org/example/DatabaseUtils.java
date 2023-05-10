package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.List;

public class DatabaseUtils {
    private MysqlDataSource dataSource;


    public DatabaseUtils(String url, int port, String database, String username, String password) {
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

    private Connection getConnection() {
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

    // tableName: the name of the table to create.
    // columnChoices format example: id INT PRIMARY KEY AUTO_INCREMENT, message VARCHAR(400), post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, post_id INT
    public void createTable(String tableName, String columnChoices) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnChoices + ");";
            int result = statement.executeUpdate(query);
            System.out.println("Result: " + result);
            connection.close();
        } catch (SQLException e) {
            System.out.printf("failed!\n");
        }
    }

    //    Takes in an String ArrayList containing (name, email, created, online, phone, address), order matters.
    public void addUser(List<String> valuesList) {
        try {
            Connection connection = getConnection();

            String query = "INSERT INTO users (name, email, created, online, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, valuesList.get(0));  // name
            ps.setString(2, valuesList.get(1));  // email
            ps.setDate(3, Date.valueOf(valuesList.get(2)));  // created (Assuming that date is in "yyyy-[m]m-[d]d" format)
            ps.setBoolean(4, Boolean.parseBoolean(valuesList.get(3)));  // online
            ps.setString(5, valuesList.get(4));  // phone
            ps.setString(6, valuesList.get(5));  // address

            int result = ps.executeUpdate();
            System.out.println("Result: " + result);

            connection.close();
        } catch (SQLException e) {
            System.out.printf("failed!\n");
        }
    }

    public void addPost(List<String> valuesList) {
        try {
            Connection connection = getConnection();

            String query = "INSERT INTO posts (post, post_date) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, valuesList.get(0));  // post
            ps.setString(2, valuesList.get(1));  // post_date

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Insert successful!");
            } else {
                System.out.println("Insert failed!");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void addComment(List<String> valuesList) {
        try {
            Connection connection = getConnection();

            String query = "INSERT INTO comments (message, post_date) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, valuesList.get(0));  // message
            ps.setString(2, valuesList.get(1));  // post_date

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Insert successful!");
            } else {
                System.out.println("Insert failed!");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

}



