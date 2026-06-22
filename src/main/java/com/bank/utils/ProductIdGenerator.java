package com.bank.utils;

import java.security.SecureRandom;

public class ProductIdGenerator {

    private static final String CHARS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String productIdGenerate() {
        SecureRandom secureRandom = new SecureRandom();

        StringBuilder sb = new StringBuilder("OSBP");

        for (int i = 0; i <=6; i++) {
            sb.append(CHARS.charAt(secureRandom.nextInt(CHARS.length())));
        }

        sb.append("2026");

        return sb.toString();
    }
}