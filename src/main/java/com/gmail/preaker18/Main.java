package com.gmail.preaker18; // Specifies the location of the class in the project's package hierarchy

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<String> valuesList = new ArrayList<>();

    public static void main(String[] args) {

        DatabaseUtils dbUtils = new DatabaseUtils("localhost", 3306, "testdblaragon", "root", "");

        // Use the database connection to add a table with column structure
//        dbUtils.createTable("test1", "id INT PRIMARY KEY AUTO_INCREMENT, message VARCHAR(400), post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, post_id INT");

        // Use the database connection to add a user
//         List<String> userData = Arrays.asList("John Doe", "johndoe@example.com", "2023-05-10", "false", "1234567890", "123 Main St");
//         dbUtils.addUser(userData);

        // Use the database connection to add a post
//         List<String> postData = Arrays.asList("This is a post", "2023-05-10");
//         dbUtils.addPost(postData);

        // Use the database connection to add a comment
//        List<String> commentData = Arrays.asList("This is a comment too", "2023-05-10");
//        dbUtils.addComment(commentData);

        // Use the database connection to get name and email from user
//        dbUtils.getUserEmail();

        // Use the database connection to get messages by post date from comments
//        dbUtils.getCommentsDesc();

        // Use the database connection to get messages from comments table based on online in users table
//        dbUtils.getCommentsByOnlineUsers();

        // Hämta antalet inlägg per användare efter ett visst datum med SQL. Returnera raderna med alias. Sortera datan efter antalet inlägg i fallande ordning.
//        dbUtils.getPostsByUserAfterDateDesc("2023-05-04 00:00:00");

        dbUtils.updateAddressByEmail("Detta är en uppdaterad adress 1", "björn@bergström.com");
    }

    public static void getInput() {
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

}





