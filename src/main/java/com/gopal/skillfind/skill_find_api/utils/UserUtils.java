package com.gopal.skillfind.skill_find_api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtils {
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-=_+[]{}|;:'\"<>,.?/";

    public static String hashWithSalt(String input, String salt) {
        String crypt = "";
        try {
            String inputWithSalt = input + salt;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(inputWithSalt.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            crypt = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return crypt;
    }

    public static String generateToken(String value) {
        System.out.println(value);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (1000 * 60 * 60 * 24));

        return Jwts.builder()
                .setSubject(value)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, "A7b#D3o9Q-L6s*F2tW5yZ1xG8h@K4u0")
                .compact();
    }

    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomDigit = random.nextInt(10);
            sb.append(randomDigit);
        }
        return sb.toString();
    }

    public static String generateUniqueGuestEmail() {
        // Base part of the email address
        String baseEmail = "guestLogin";

        // Generate a random number to make the email unique
        int randomSuffix1 = new Random().nextInt(100000);
        String randomCharSuffix = generateRandomString(5);
        int randomSuffix2 = new Random().nextInt(100000);

        // Combine the base email and random number to create the guest email
        String guestEmail = baseEmail + randomSuffix1 + randomCharSuffix + "@skillfind.app";

        // Check if the generated email is unique; if not, generate a new one
        return guestEmail;
    }

    private static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('a' + random.nextInt(26));
            randomString.append(randomChar);
        }
        return randomString.toString();
    }

    public static String generateRandomPassword() {
        // Combine all character sets
        String allChars = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;

        // Create a StringBuilder to build the password
        StringBuilder password = new StringBuilder();

        // Use a secure random number generator
        SecureRandom random = new SecureRandom();

        // Generate the password
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(allChars.length());
            char randomChar = allChars.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Define a regular expression for a simple phone number pattern
        // This example assumes a 10-digit US phone number format
        String phoneRegex = "^[2-9]\\d{2}[2-9]\\d{2}\\d{4}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(phoneRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(phoneNumber);

        // Return true if the phone number matches the pattern, otherwise false
        return matcher.matches();
    }
}
