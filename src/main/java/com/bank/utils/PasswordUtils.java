package com.bank.utils;

public class PasswordUtils {

    public static void validatePassword(String password) {

        boolean valid =
                password != null &&
                        password.length() >= 8 &&
                        password.matches(".*[A-Z].*") &&
                        password.matches(".*[a-z].*") &&
                        password.matches(".*\\d.*");

        if (!valid) {
            throw new RuntimeException(
                    "Password must be at least 8 characters long and contain uppercase, lowercase and a digit");
        }
    }
}