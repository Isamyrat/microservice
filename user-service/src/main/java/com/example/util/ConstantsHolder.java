package com.example.util;

import org.springframework.stereotype.Component;

@Component
public class ConstantsHolder {

    public static final String SECRET = loadEnv("SECRET");

    private static String loadEnv(String name) {
        String var = System.getenv(name);
        if (var == null) {
            throw new RuntimeException(String.format("Environment variable %s not found.", name));
        } else if (var.isBlank()) {
            throw new RuntimeException(String.format("Environment variable %s is blank.", name));
        }
        return var;
    }
}
