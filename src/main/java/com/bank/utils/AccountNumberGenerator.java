package com.bank.utils;

import java.util.Random;

public class AccountNumberGenerator {

    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generate() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder("OSBA");

        for (int i = 0; i < 6; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        sb.append("2026");

        return sb.toString();
    }
}