package com.gmail.preaker18;

import models.DatabaseConnector;
import models.PasswordHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;

import static com.mysql.cj.conf.PropertyKey.logger;

public class DatabaseUtils extends DatabaseConnector {
    private static final Logger logger = LogManager.getLogger(DatabaseUtils.class);

    public DatabaseUtils(String url, int port, String database, String username, String password) {
        super(url, port, database, username, password);
    }

    public class Config {
        public static Properties loadProperties(String propFileName) {
            Properties prop = new Properties();

            try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName)) {
                if (inputStream != null) {
                    prop.load(inputStream);
                } else {
                    throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prop;
        }
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

    public boolean updateComment(int commentID, int userID, String stringMessage) {
        String query = "UPDATE comments SET message = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters
            preparedStatement.setString(1, stringMessage);
            preparedStatement.setInt(2, commentID);
            preparedStatement.setInt(3, userID);

            // Execute update
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0; // returns true if rows were updated, false otherwise

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUserByEmail(String email) {
        String query = "DELETE FROM users WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePostByID(int id) {
        String query = "DELETE FROM posts WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCommentByID(int id) {
        String query = "DELETE FROM comments WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            logger.info("Executing query: " + query + " with id: " + id);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public void getUserThatPosted() {
        String query = "SELECT * FROM users WHERE online=1 AND id IN (SELECT DISTINCT user_id FROM posts)";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                String name = result.getString("name");
                String email = result.getString("email");
                Date created = result.getDate("created");
                String phone = result.getString("phone");
                String address = result.getString("address");

                System.out.println("Name: " + name + ", " + "Email: " + email + ", " + "Created: " + created + ", " + "Phone: " + phone + ", " + "Address: " + address);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getPostsThatHaveComments() {
        String query = "SELECT * FROM posts WHERE id IN (SELECT DISTINCT post_id FROM comments)";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                int postID = result.getInt("id");
                String post = result.getString("post");


                System.out.println("ID: " + postID + ", " + "Post: " + post);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getPostsThatHaveTenOrMoreComments() {
        String query = "SELECT * FROM posts WHERE id IN (SELECT post_id FROM comments GROUP BY post_id HAVING COUNT(*) > 10);";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                int postID = result.getInt("id");
                String post = result.getString("post");


                System.out.println("ID: " + postID + ", " + "Post: " + post);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getAllUsersThatHaveNotPosted() {
        String query = "SELECT * FROM users WHERE online=1 AND id NOT IN (SELECT user_id FROM posts);";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                String name = result.getString("name");
                String email = result.getString("email");
                Date created = result.getDate("created");
                String phone = result.getString("phone");
                String address = result.getString("address");

                System.out.println("Name: " + name + ", " + "Email: " + email + ", " + "Created: " + created + ", " + "Phone: " + phone + ", " + "Address: " + address);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getAllPostsThatHaveCommentsFromMoreThanOneUser() {
        String query = "SELECT p.* FROM posts p WHERE p.id IN (SELECT c.post_id FROM comments c GROUP BY c.post_id HAVING COUNT(DISTINCT c.user_id) > 1);";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                int postID = result.getInt("id");
                String post = result.getString("post");


                System.out.println("ID: " + postID + ", " + "Post: " + post);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getAllUsersThatHaveMadePostAfterGivenDate(String date) {
        String query = "SELECT * FROM users WHERE id IN (SELECT user_id FROM posts WHERE post_date >= ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, date);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    String name = result.getString("name");
                    String email = result.getString("email");
                    Date created = result.getDate("created");
                    String phone = result.getString("phone");
                    String address = result.getString("address");

                    System.out.println("Name: " + name + ", " + "Email: " + email + ", " + "Created: " + created + ", " + "Phone: " + phone + ", " + "Address: " + address);
                }
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public boolean updateUserPassword(int userID, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);


            // Execute update
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0; // returns true if rows were updated, false otherwise

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePostVisibilityByUser(int userID) {
        String query = "UPDATE posts SET visible = CASE WHEN visible = TRUE THEN FALSE ELSE TRUE END WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters
            preparedStatement.setInt(1, userID);


            // Execute update
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0; // returns true if rows were updated, false otherwise

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePostByUserID(int userID) {
        String queryPosts = "DELETE FROM posts WHERE user_id = ?";
        String queryComments = "DELETE FROM comments WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatementPosts = connection.prepareStatement(queryPosts);
             PreparedStatement preparedStatementComments = connection.prepareStatement(queryComments)) {

            preparedStatementPosts.setInt(1, userID);
            preparedStatementComments.setInt(1, userID);

            int rowsAffectedPosts = preparedStatementPosts.executeUpdate();
            int rowsAffectedComments = preparedStatementComments.executeUpdate();

            System.out.println("Post Rows deleted: " + rowsAffectedPosts);
            System.out.println("Comment Rows deleted: " + rowsAffectedComments);
            return rowsAffectedPosts >= 0 && rowsAffectedComments >= 0;
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public void updateCommentVisibilityByPostVisibility() {
        String query = "UPDATE comments INNER JOIN posts ON comments.post_id = posts.id SET comments.visible = posts.visible;";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            int rowsAffected = statement.executeUpdate(query);
            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void addMedia(String URL, String file_type, String description, int user_id, int gallery_id) {
        String query = "INSERT INTO media (URL, file_type, Description, user_id, gallery_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, URL);  // URL
            ps.setString(2, file_type);  // file_type
            ps.setString(3, description); // description
            ps.setInt(4, user_id);  // user_id
            ps.setInt(5, gallery_id); // gallery_id

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

    public void getAllUsersThatPostedAndAddedMediatoPost() {
        String query = "SELECT DISTINCT users.name " +
                "FROM users " +
                "JOIN posts ON users.id = posts.user_id " +
                "WHERE posts.media_id IS NOT NULL";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getMediaURLWithTenOrMoreLikes() {
        String query = "SELECT media.URL, media.likes, users.name AS owner_name " +
                "FROM media " +
                "JOIN users ON media.owner = users.id " +
                "WHERE media.likes >= 10";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String url = resultSet.getString("URL");
                int likes = resultSet.getInt("likes");
                String ownerName = resultSet.getString("owner_name");

                System.out.println("URL: " + url + ", Likes: " + likes + ", Owner Name: " + ownerName);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }


    public void getAllUsernamesThatPostedWithImage() {
        String query = "SELECT DISTINCT users.name " +
                "FROM users " +
                "JOIN posts ON users.id = posts.user_id " +
                "JOIN media ON users.id = media.owner " +
                "WHERE media.file_type = '.jpg'";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void getAllVisiblePostsAndAllPostsUsersOwnPosts(int userID) {
        String query = "SELECT DISTINCT p.* FROM posts p " +
                "LEFT JOIN comments c ON p.id = c.post_id " +
                "WHERE (c.visible = true OR p.user_id = ?) AND p.visible = true";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String post = resultSet.getString("post");
                Timestamp post_date = resultSet.getTimestamp("post_date");
                int user_id = resultSet.getInt("user_id");
                boolean visible = resultSet.getBoolean("visible");
                int media_id = resultSet.getInt("media_id");

                // Here you can print or use your data
                System.out.println("ID: " + id + ", Post: " + post + ", Post Date: " + post_date + ", User ID: " + user_id + ", Visible: " + visible + ", Media ID: " + media_id);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public void updateHashedPassword() {
        // Select query to fetch all rows from the users table
        String selectQuery = "SELECT id, password FROM users";
        // Update query to update the hashed_pwd field for a given user id
        String updateQuery = "UPDATE users SET hashed_pwd = ? WHERE id = ?";

        try (Connection connection = getConnection();
             Statement selectStatement = connection.createStatement();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Execute the select query
            ResultSet resultSet = selectStatement.executeQuery(selectQuery);

            // Iterate over each row in the result set
            while (resultSet.next()) {
                // Get the id and password from the current row
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");

                // Generate the hashed password using the PasswordHandler class
                String hashedPassword = PasswordHandler.hashPassword(password);

                // Set the parameters for the update statement
                updateStatement.setString(1, hashedPassword);
                updateStatement.setInt(2, id);

                // Execute the update statement
                updateStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


}



