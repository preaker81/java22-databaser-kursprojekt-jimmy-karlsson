package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHandler {
    private static final int SALT_LENGTH = 16;

    public static String hashPassword(String password) {
        // Create a SecureRandom object. This will be used to generate a random salt.
        SecureRandom random = new SecureRandom();

        // Create a byte array of a certain length to hold the salt. The value of SALT_LENGTH is not shown in this snippet.
        byte[] salt = new byte[SALT_LENGTH];

        // Generate random bytes and place them into the salt array.
        random.nextBytes(salt);

        try {
            // Get an instance of the SHA-256 MessageDigest object. This will be used to hash the password.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the MessageDigest object with the salt.
            digest.update(salt);

            // Hash the password. The password string is first converted to bytes.
            byte[] hashedPassword = digest.digest(password.getBytes());

            // Return the hashed password and salt, both converted to hexadecimal strings.
            // The "+" operator concatenates the two strings together.
            return byteArrayToHexString(hashedPassword) + byteArrayToHexString(salt);
        }catch(NoSuchAlgorithmException e){
            // If the SHA-256 algorithm is not available (which should never happen in a Java environment), return null.
            return null;
        }
    }


    public static boolean verifyPassword(String password, String hashedPassword) {
        // Extract the hashed password and salt from the input string. The hashed password is the first 64 characters,
        // and the salt (stored as a hexadecimal string) is everything after.
        String passwordHash = hashedPassword.substring(0, 64);
        String saltHex = hashedPassword.substring(64);

        // Convert the salt from a hexadecimal string to a byte array.
        byte[] salt = hexStringToByteArray(saltHex);

        try {
            // Get an instance of the SHA-256 MessageDigest object. This will be used to hash the input password.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the MessageDigest with the salt.
            digest.update(salt);

            // Hash the input password using the updated MessageDigest. The password string is first converted to bytes.
            byte[] hashedInputPassword = digest.digest(password.getBytes());

            // Convert the hashed password from a byte array to a hexadecimal string.
            String hashedInputPasswordHex = byteArrayToHexString(hashedInputPassword);

            // If the calculated hash matches the stored hash, the input password is correct. Return true.
            // Otherwise, return false.
            return passwordHash.equals(hashedInputPasswordHex);
        }
        catch(NoSuchAlgorithmException e){
            // If the SHA-256 algorithm is not available (which should never happen in a Java environment), return false.
            return false;
        }
    }


    private static String byteArrayToHexString(byte[] array) {
        StringBuilder sb = new StringBuilder(); // Instantiate a StringBuilder object to build the hex string.
        for (byte b : array) { // Iterate through each byte in the input array.
            sb.append(String.format("%02x", b)); // Convert each byte to a 2-digit hexadecimal string and append it to the StringBuilder.
        }
        return sb.toString(); // Convert the StringBuilder to a String and return it.
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length(); // Get the length of the input string.
        byte[] data = new byte[len / 2]; // Instantiate a byte array half the length of the input string. Each pair of hex characters will be turned into a single byte.
        for (int i = 0; i < len; i += 2) { // Iterate through the input string, 2 characters at a time.
            // Convert each pair of hex characters to a byte, where the first character represents the high nibble (4 bits),
            // and the second character represents the low nibble. Combine them to form a byte and store it in the byte array.
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data; // Return the byte array.
    }

}
