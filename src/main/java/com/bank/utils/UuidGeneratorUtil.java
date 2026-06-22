
package com.bank.utils;
import java.util.UUID;

public final class UuidGeneratorUtil {

    private UuidGeneratorUtil() {
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}