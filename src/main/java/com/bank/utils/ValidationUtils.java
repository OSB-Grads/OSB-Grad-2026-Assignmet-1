package com.bank.utils;

import com.bank.exception.UserCreationException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validatePassword(String password) {
        boolean valid =
                password != null &&
                        password.length() >= 8 &&
                        password.matches(".*[A-Z].*") &&
                        password.matches(".*[a-z].*") &&
                        password.matches(".*\\d.*");

        if (!valid) {
            throw new UserCreationException(
                    "Password must be at least 8 characters long and contain uppercase, lowercase and a digit");
        }
    }

    public static void validateName(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserCreationException(fieldName + " must not be blank");
        }

        if (!value.trim().matches("[a-zA-Z\\s'-]+")) {
            throw new UserCreationException(
                    fieldName + " must contain only letters, spaces, hyphens or apostrophes");
        }
    }

    public static void validateDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            throw new UserCreationException("Date of birth must not be blank");
        }

        try {
            LocalDate dob = LocalDate.parse(dateOfBirth.trim());

            int age = Period.between(dob, LocalDate.now()).getYears();

            if (age < 18) {
                throw new UserCreationException(
                        "Customer must be 18 or older. Calculated age: " + age);
            }

        } catch (DateTimeParseException e) {
            throw new UserCreationException(
                    "Invalid date of birth format. Expected YYYY-MM-DD");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new UserCreationException("Email must not be blank");
        }

        if (!email.trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new UserCreationException(
                    "Email format is invalid. Expected format: user@example.com");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new UserCreationException("Phone number must not be blank");
        }

        String cleaned = phone.replaceAll("[\\s()+-]", "");

        if (!cleaned.matches("\\d{7,15}")) {
            throw new UserCreationException(
                    "Phone number must contain 7 to 15 digits");
        }
    }

    public static void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new UserCreationException("Address must not be blank");
        }
    }

    public static void validateNationalId(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new UserCreationException("National ID must not be blank");
        }

        if (!nationalId.trim().matches("\\d{12}")) {
            throw new UserCreationException(
                    "National ID must be exactly 12 digits");
        }
    }

    public static void validateCustomer(
            String firstName,
            String lastName,
            String dateOfBirth,
            String email,
            String phone,
            String address,
            String nationalId) {

        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateDateOfBirth(dateOfBirth);
        validateEmail(email);
        validatePhone(phone);
        validateAddress(address);
        validateNationalId(nationalId);
    }
}