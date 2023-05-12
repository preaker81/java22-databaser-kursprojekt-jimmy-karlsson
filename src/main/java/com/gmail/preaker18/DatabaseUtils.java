package com.gmail.preaker18;

import models.DatabaseConnector;

import java.sql.*;
import java.util.List;

public class DatabaseUtils extends DatabaseConnector {

    public DatabaseUtils(String url, int port, String database, String username, String password) {
        super(url, port, database, username, password);
    }

    // tableName: the name of the table to create.
    // columnChoices format example: id INT PRIMARY KEY AUTO_INCREMENT, message VARCHAR(400), post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, post_id INT
    public void createTable(String tableName, String columnChoices) {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnChoices + ");";

        // try-with-resources, auto-close when block is finished
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            int result = statement.executeUpdate(query);
            System.out.println("Result: " + result);

        } catch (SQLException e) {
            System.out.println("failed!");
            e.printStackTrace();
        }
    }


    //    Takes in a String ArrayList containing (name, email, created, online, phone, address), order matters.
    public void addUser(List<String> valuesList) {
        String query = "INSERT INTO users (name, email, created, online, phone, address) VALUES (?, ?, ?, ?, ?, ?)";

        // try-with-resources, auto-close when block is finished
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, valuesList.get(0));  // name
            ps.setString(2, valuesList.get(1));  // email
            ps.setDate(3, Date.valueOf(valuesList.get(2)));  // created (Assuming that date is in "yyyy-[m]m-[d]d" format)
            ps.setBoolean(4, Boolean.parseBoolean(valuesList.get(3)));  // online
            ps.setString(5, valuesList.get(4));  // phone
            ps.setString(6, valuesList.get(5));  // address

            int result = ps.executeUpdate();
            System.out.println("Result: " + result);

        } catch (SQLException e) {
            System.out.println("failed!");
            e.printStackTrace();
        }
    }


    public void addPost(List<String> valuesList) {
        String query = "INSERT INTO posts (post, post_date) VALUES (?, ?)";

        // try-with-resources, auto-close when block is finished
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, valuesList.get(0));  // post
            ps.setString(2, valuesList.get(1));  // post_date

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Insert successful!");
            } else {
                System.out.println("Insert failed!");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }


    public void addComment(List<String> valuesList) {
        String query = "INSERT INTO comments (message, post_date) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, valuesList.get(0));  // message
            ps.setString(2, valuesList.get(1));  // post_date

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Insert successful!");
            } else {
                System.out.println("Insert failed!");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getUserEmail() {
        String query = "SELECT name, email FROM users WHERE online=1";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                String name = result.getString("name");
                String email = result.getString("email");

                System.out.println("Name: " + name + ", " + "Email: " + email);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getCommentsDesc() {
        String query = "SELECT message, post_date FROM comments ORDER BY post_date DESC ";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                String message = result.getString("message");
                Date postDate = result.getDate("post_date");

                System.out.println("Message: " + message + ", " + "Post date: " + postDate);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getCommentsByOnlineUsers() {
        String query = "SELECT comments.message FROM comments INNER JOIN users ON comments.user_id = users.id WHERE users.online =0";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                String message = result.getString("message");

                System.out.println("Message: " + message);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getPostsByUserAfterDateDesc(String inputDate) {
        String query = "SELECT u.name AS username, COUNT(p.id) AS postCount FROM users u JOIN posts p ON u.id = p.user_id WHERE p.post_date > ? GROUP BY u.name ORDER BY postCount DESC";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, inputDate);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    String username = result.getString("username");
                    int postCount = result.getInt("postCount");

                    System.out.println("User: " + username + " Posts: " + postCount + ".");
                }
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }


    public boolean updateAddressByEmail(String address, String email) {
        String query = "UPDATE users SET address = ? WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, address);
            preparedStatement.setString(2, email);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0; // return true if at least one row has been updated

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            return false;
        }
    }




}



