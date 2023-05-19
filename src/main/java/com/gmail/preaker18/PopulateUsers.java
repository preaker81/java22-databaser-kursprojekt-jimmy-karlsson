package com.gmail.preaker18;

import models.DatabaseConnector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PopulateUsers extends DatabaseConnector {
    public PopulateUsers(String url, int port, String database, String username, String password) {
        super(url, port, database, username, password);
    }

    /*
        Method for generating 100 template users for the database.
        Used in conjunction with the populateDatabaseWithUsers Method.
         */
    public void populateUsers(int userAmount) {
        List<Map<String, Object>> users = new ArrayList<>();

        String[] firstNames = {"John", "Jane", "Mike", "Mary", "James", "Emma", "Robert", "Olivia", "William", "Sophia"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson"};
        String[] streets = {"Main St.", "High St.", "Pearl St.", "Maple St.", "Park St.", "Oak St.", "Pine St.", "Cedar St.", "Elm St.", "Walnut St."};

        Random random = new Random();

        for (int i = 0; i < userAmount; i++) {
            Map<String, Object> user = new HashMap<>();
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            user.put("name", firstName + " " + lastName);
            user.put("email", firstName.toLowerCase() + lastName.toLowerCase() + i + "@example.com");

            int day = random.nextInt(28) + 1; // Keep day between 1-28 to avoid leap year issue
            int month = random.nextInt(5) + 1; // Keep month between 1-5 for the first 5 months
            user.put("created", LocalDate.of(2023, month, day));

            user.put("online", random.nextInt(2)); // Generate random 0 or 1
            user.put("phone", "+1" + String.format("%09d", random.nextInt(1000000000))); // Generate random 10 digit number

            String address = (random.nextInt(9999) + 1) + " " + streets[random.nextInt(streets.length)]; // Generate random street address
            user.put("address", address);

            // Generate a random password with 8 characters including uppercase, lowercase and digits
            String password = random.ints(48, 123)
                    .filter(c -> (c <= 57 || c >= 65) && (c <= 90 || c >= 97))
                    .limit(8)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            user.put("password", password);

            users.add(user);
        }

        for (Map<String, Object> user : users) {
            populateDatabaseWithUsers(user);
        }
    }

    /*
     * Method used in the populateUsers method
     * */
    public void populateDatabaseWithUsers(Map<String, Object> user) {
        String query = "INSERT INTO users (name, email, created, online, phone, address, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, (String) user.get("name"));  // name
            ps.setString(2, (String) user.get("email"));  // email
            ps.setDate(3, Date.valueOf((LocalDate) user.get("created")));  // created
            ps.setBoolean(4, (int) user.get("online") == 1);  // online
            ps.setString(5, (String) user.get("phone"));  // phone
            ps.setString(6, (String) user.get("address"));  // address
            ps.setString(7, (String) user.get("password"));  // password

            int result = ps.executeUpdate();
            System.out.println("Result: " + result);

        } catch (SQLException e) {
            System.out.println("failed!");
            e.printStackTrace();
        }
    }

}
