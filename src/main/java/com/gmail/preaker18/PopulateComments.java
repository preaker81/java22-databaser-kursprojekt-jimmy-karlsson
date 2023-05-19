package com.gmail.preaker18;

import models.DatabaseConnector;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PopulateComments extends DatabaseConnector {
    public PopulateComments(String url, int port, String database, String username, String password) {
        super(url, port, database, username, password);
    }

    public void populateComments(int commentAmount) {
        String[] comments = {
                "Great quote, very inspiring!",
                "This quote has made my day.",
                "Words to live by.",
                "This is so true!",
                "Thanks for sharing this.",
                "Wow, this really resonates with me.",
                "Such a powerful quote.",
                "I needed to hear this today.",
                "I've always loved this quote.",
                "This is why I love quotes, they're so insightful."
        };

        Random random = new Random();

        // Query posts and users to get all ids and creation dates
        List<Map<String, Object>> posts = getPosts();
        List<Map<String, Object>> users = getUsers();

        for (int i = 0; i < commentAmount; i++) {
            Map<String, Object> comment = new HashMap<>();

            // Randomly select a post and get its ID and post_date
            Map<String, Object> post = posts.get(random.nextInt(posts.size()));
            int postId = (int) post.get("id");
            LocalDate postDate = (LocalDate) post.get("post_date");

            // Randomly select a user and get its ID
            Map<String, Object> user = users.get(random.nextInt(users.size()));
            int userId = (int) user.get("id");

            comment.put("message", comments[random.nextInt(comments.length)]);  // Generate random comment
            comment.put("visible", random.nextInt(2));  // Generate random 0 or 1 for visible

            // Generate a random date that is not earlier than the post's post_date
            int day = random.nextInt(28) + 1;
            int month = random.nextInt(5) + 1;
            LocalDate commentDate = LocalDate.of(2023, month, day);
            while (commentDate.isBefore(postDate)) {
                day = random.nextInt(28) + 1;
                month = random.nextInt(5) + 1;
                commentDate = LocalDate.of(2023, month, day);
            }
            comment.put("post_date", commentDate);
            comment.put("post_id", postId);  // Associate comment with post ID
            comment.put("user_id", userId);  // Associate comment with user ID

            populateDatabaseWithComments(comment);
        }
    }

    public void populateDatabaseWithComments(Map<String, Object> comment) {
        String query = "INSERT INTO comments (message, post_date, post_id, user_id, visible) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, (String) comment.get("message"));  // message
            ps.setDate(2, Date.valueOf((LocalDate) comment.get("post_date")));  // post_date
            ps.setInt(3, (int) comment.get("post_id"));  // post_id
            ps.setInt(4, (int) comment.get("user_id"));  // user_id
            ps.setBoolean(5, (int) comment.get("visible") == 1);  // visible

            int result = ps.executeUpdate();
            System.out.println("Result: " + result);

        } catch (SQLException e) {
            System.out.println("Failed to populate comments!");
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getPosts() {
        String query = "SELECT id, post_date FROM posts";
        List<Map<String, Object>> posts = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> post = new HashMap<>();
                post.put("id", rs.getInt("id"));
                post.put("post_date", rs.getDate("post_date").toLocalDate());
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get posts!");
            e.printStackTrace();
        }

        return posts;
    }

    public List<Map<String, Object>> getUsers() {
        String query = "SELECT id, created FROM users";
        List<Map<String, Object>> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("created", rs.getDate("created").toLocalDate());
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get users!");
            e.printStackTrace();
        }

        return users;
    }


}
