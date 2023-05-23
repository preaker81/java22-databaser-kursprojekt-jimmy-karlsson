package com.gmail.preaker18; // Specifies the location of the class in the project's package hierarchy

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<String> valuesList = new ArrayList<>();

    public static void main(String[] args) {

        DatabaseUtils dbUtils = new DatabaseUtils("localhost", 3306, "testdblaragon", "root", "");
        PopulateUsers populateUsers = new PopulateUsers("localhost", 3306, "testdblaragon", "root", "");
        PopulatePosts populatePosts = new PopulatePosts("localhost", 3306, "testdblaragon", "root", "");
        PopulateComments populateComments = new PopulateComments("localhost", 3306, "testdblaragon", "root", "");

//        Use the database connection to add a table with column structure
//        dbUtils.createTable("test1", "id INT PRIMARY KEY AUTO_INCREMENT, message VARCHAR(400), post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, post_id INT");


//        Method for populating the database with specified amount of users.
//        Use this if you need more users in the database.
//        populateUsers.populateUsers(10);

//        Method for populating the database with specific amount of posts.
//        The created posts will not have a post_date that is before the connected users created date.
//        The user_id will only be user id´s that actually exist in users.
//        populatePosts.populatePosts(100);

//        Method for populating the database with specific amount of comments.
//        The created comments will not have a post_date that is before the connected posts post_date.
//        The user_id will only be user id´s that actually exist in users.
//        The post_id will only be user id´s that actually exist in posts.
//        populateComments.populateComments(100);






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

//        dbUtils.updateAddressByEmail("Detta är en uppdaterad adress 1", "björn@bergström.com");

//        Update a comment that is has id and user_id
//        dbUtils.updateComment(2, 2, "updated comment");

//        Delete user in users based on email
//        dbUtils.deleteUserByEmail("johndoe@example.com");

//        Delete post based on id
//        dbUtils.deletePostByID(12);

//        Delete comment based on id
//        dbUtils.deleteCommentByID(12);

//        Get users that have posted atleast once
//        dbUtils.getUserThatPosted();

//        Get all posts that someone have commented on.
//        dbUtils.getPostsThatHaveComments();

//        Get posts that have more than 10 comments
//        dbUtils.getPostsThatHaveTenOrMoreComments();

//        Get all users that have not posted and is online
//        dbUtils.getAllUsersThatHaveNotPosted();

//        Get all posts that have comments from more than one user
//        dbUtils.getAllPostsThatHaveCommentsFromMoreThanOneUser();

//        Get all users that have posted on or after a given date
//        dbUtils.getAllUsersThatHaveMadePostAfterGivenDate("2023-05-10");

//        Update password for user with given ID
//        dbUtils.updateUserPassword(1, "updated password");

//        Delete deletes all posts by user
//        dbUtils.deletePostByUserID(4);

//        Update switches visible boolean in posts table based on given user id
//        dbUtils.updatePostVisibilityByUser(1);

//        Update switches the visibility of the comments to the same value of its posts owner
//        dbUtils.updateCommentVisibilityByPostVisibility();


//        Gets all users that have posted and added a media to the post
//        dbUtils.getAllUsersThatPostedAndAddedMediatoPost();

//        Gets all media that have 10 or more likes and the name of the uploader.
//        dbUtils.getMediaURLWithTenOrMoreLikes();


//        Get all users name that have posted and added an image.
//        dbUtils.getAllUsernamesThatPostedWithImage();


//        Get all posts that have atleast one visible comment or is a post the given userID have posted.
//        dbUtils.getAllVisiblePostsAndAllPostsUsersOwnPosts(12);


        dbUtils.updateHashedPassword();
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





