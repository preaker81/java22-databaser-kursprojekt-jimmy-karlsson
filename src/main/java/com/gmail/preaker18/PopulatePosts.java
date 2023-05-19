package com.gmail.preaker18;

import models.DatabaseConnector;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PopulatePosts extends DatabaseConnector {

    public PopulatePosts(String url, int port, String database, String username, String password) {
        super(url, port, database, username, password);
    }

    public void populatePosts(int postAmount) {
        String[] posts = {
                "The greatest glory in living lies not in never falling, but in rising every time we fall. - Nelson Mandela",
                "The way to get started is to quit talking and begin doing. - Walt Disney",
                "Don't judge each day by the harvest you reap but by the seeds that you plant. - Robert Louis Stevenson",
                "The only impossible journey is the one you never begin. - Tony Robbins",
                "In three words I can sum up everything I've learned about life: it goes on. - Robert Frost",
                "Life is what happens when you're busy making other plans. - John Lennon",
                "The purpose of life is not to be happy. It is to be useful, to be honorable, to be compassionate, to have it make some difference that you have lived and lived well. - Ralph Waldo Emerson",
                "Love the life you live. Live the life you love. - Bob Marley",
                "I have learned that people will forget what you said, people will forget what you did, but people will never forget how you made them feel. - Maya Angelou",
                "The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart. - Helen Keller",
                "In the end, it's not the years in your life that count. It's the life in your years. - Abraham Lincoln",
                "Many of life's failures are people who did not realize how close they were to success when they gave up. - Thomas A. Edison",
                "You miss 100% of the shots you don't take. - Wayne Gretzky",
                "I've failed over and over and over again in my life and that is why I succeed. - Michael Jordan",
                "Always forgive your enemies; nothing annoys them so much. - Oscar Wilde",
                "To live is the rarest thing in the world. Most people exist, that is all. - Oscar Wilde",
                "You only live once, but if you do it right, once is enough. - Mae West",
                "It is never too late to be what you might have been. - George Eliot",
                "I can't change the direction of the wind, but I can adjust my sails to always reach my destination. - Jimmy Dean",
                "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt"
        };

        Random random = new Random();

        // Query users to get all ids and creation dates
        List<Map<String, Object>> users = getUsers();

        for (int i = 0; i < postAmount; i++) {
            Map<String, Object> post = new HashMap<>();

            // Randomly select a user and get their ID and creation date
            Map<String, Object> user = users.get(random.nextInt(users.size()));
            int userId = (int) user.get("id");
            LocalDate createdDate = (LocalDate) user.get("created");

            post.put("post", posts[random.nextInt(posts.length)]);  // Generate random post
            post.put("visible", random.nextInt(2));  // Generate random 0 or 1 for visible

            // Generate a random date that is not earlier than the user's creation date
            int day = random.nextInt(28) + 1;
            int month = random.nextInt(5) + 1;
            LocalDate postDate = LocalDate.of(2023, month, day);
            while (postDate.isBefore(createdDate)) {
                day = random.nextInt(28) + 1;
                month = random.nextInt(5) + 1;
                postDate = LocalDate.of(2023, month, day);
            }
            post.put("post_date", postDate);
            post.put("user_id", userId);  // Associate post with user ID

            populateDatabaseWithPosts(post);
        }
    }

    /*
     * Method used in the populatePosts method
     * */
    public void populateDatabaseWithPosts(Map<String, Object> post) {
        String query = "INSERT INTO posts (post, post_date, user_id, visible) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, (String) post.get("post"));  // post
            ps.setDate(2, Date.valueOf((LocalDate) post.get("post_date")));  // post_date
            ps.setInt(3, (int) post.get("user_id"));  // user_id
            ps.setBoolean(4, (int) post.get("visible") == 1);  // visible

            int result = ps.executeUpdate();
            System.out.println("Result: " + result);

        } catch (SQLException e) {
            System.out.println("Failed to populate posts!");
            e.printStackTrace();
        }
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
