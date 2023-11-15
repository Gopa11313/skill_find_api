package com.gopal.skillfind.skill_find_api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a date-time format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time
        String formattedDateTime = currentDateTime.format(dateTimeFormatter);

        return formattedDateTime;
    }
}
