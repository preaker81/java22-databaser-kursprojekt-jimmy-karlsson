package org.example; // Specifies the location of the class in the project's package hierarchy

import com.mysql.cj.jdbc.MysqlDataSource; // Importing the MysqlDataSource class for establishing a connection to the MySQL database

import java.sql.*; // Importing the java.sql package which contains classes for accessing and processing data stored in a data source
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static MysqlDataSource dataSource; // MySQL data source object
    static String url = "localhost"; // The hostname or IP where the MySQL server is running
    static int port = 3306; // The port where the MySQL server is listening
    static String database = "testdblaragon"; // The name of the database to connect to
    static String username = "root"; // The username for the MySQL server
    static String password = ""; // The password for the MySQL server

    static List<String> valuesList = new ArrayList<>();

    public static void main(String[] args) {


        getInput();


        InitializeDatabase(); // Calls the method to initialize the database connection
//        createTable();
        insertToTable();
    }

    public static void getInput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Give name: ");
        String name = sc.nextLine();
        System.out.print("Give mail: ");
        String mail = sc.nextLine();
        System.out.print("Was created (Date): ");
        String created = sc.nextLine();
        System.out.print("Is online: ");
        String online = sc.nextLine();
        System.out.print("Give phonenumber: ");
        String phone = sc.nextLine();
        System.out.print("Give Adress:");
        String address = sc.nextLine();

        valuesList.add(name);
        valuesList.add(mail);
        valuesList.add(created);
        valuesList.add(online);
        valuesList.add(phone);
        valuesList.add(address);
    }

    // This method configures the connections to the database
    public static void InitializeDatabase() {
        try {
            System.out.printf("Configuring data source..."); // Printing a message to indicate the start of data source configuration
            dataSource = new MysqlDataSource(); // Creating a new MySQL data source object
            dataSource.setUser(username); // Setting the username for the data source
            dataSource.setPassword(password); // Setting the password for the data source
            dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database +
                    "?serverTimezone=UTC"); // Setting the JDBC connection URL for the data source
            dataSource.setUseSSL(false); // Disabling the use of SSL for the connection
            System.out.printf("done!\n"); // Printing a message to indicate the successful configuration of the data source
        } catch (SQLException e) {
            System.out.printf("failed!\n"); // Printing a message to indicate a failure in the configuration of the data source
            System.exit(0); // Terminate the JVM
        }
    }

    // This method creates a temporary connection to the database
    public static Connection GetConnection() {
        try {
            System.out.printf("Fetching connection to database..."); // Printing a message to indicate the start of fetching a connection
            Connection connection = dataSource.getConnection(); // Getting a connection to the database
            System.out.printf("done!\n"); // Printing a message to indicate the successful fetching of the connection
            return connection; // Returning the established connection
        } catch (SQLException e) {
            System.out.printf("failed!\n"); // Printing a message to indicate a failure in fetching the connection
            System.exit(0); // Terminate the JVM
            return null; // Required by the compiler as all paths in the method must return a value
        }
    }

    public static void createTable() {
        try {
            //Koppla upp mot databasen
            Connection connection = GetConnection();

            //Skapa ett statement object för att köra SQL-querys genom databaskopplingen
            Statement statement = connection.createStatement();
            //Skriv din query som du vill köra mot databasen.
            String query = "CREATE TABLE IF NOT EXISTS users3 (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(75), email VARCHAR(50), created DATE, online BOOLEAN, phone VARCHAR(20), address VARCHAR(100));";
            //Kör SQL-query och returnera resultatet (Antalet påverkade rader returneras)
            int result = statement.executeUpdate(query);
            System.out.println("Result: " + result);
            //Stäng databaskoppling och returnera den till databaspoolen
            connection.close();
        } catch (SQLException e) {
            System.out.printf("failed!\n"); // Printing a message to indicate a failure in fetching the connection
        }
    }

    public static void insertToTable() {
        try {
            //Koppla upp mot databasen
            Connection connection = GetConnection();

            // Prepare SQL statement with placeholders
            String query = "INSERT INTO users3 (name, email, created, online, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            // Set values for placeholders
            ps.setString(1, valuesList.get(0));  // name
            ps.setString(2, valuesList.get(1));  // email
            ps.setDate(3, Date.valueOf(valuesList.get(2)));  // created (Assuming that date is in "yyyy-[m]m-[d]d" format)
            ps.setBoolean(4, Boolean.parseBoolean(valuesList.get(3)));  // online
            ps.setString(5, valuesList.get(4));  // phone
            ps.setString(6, valuesList.get(5));  // address

            // Execute SQL query
            int result = ps.executeUpdate();
            System.out.println("Result: " + result);

            // Close database connection
            connection.close();
        } catch (SQLException e) {
            System.out.printf("failed!\n"); // Printing a message to indicate a failure in fetching the connection
        }
    }
}
