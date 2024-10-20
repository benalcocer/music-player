package com.example.util;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

/**
 * Class that controls getting local Environment variables.
 */
public class Environment {

    private final Dotenv dotenv = Dotenv.load();

    private Environment() {

    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(dotenv.get(key));
    }

    private static class EnvironmentSingleton {
        private static final Environment INSTANCE = new Environment();
    }

    public static Environment getInstance() {
        return EnvironmentSingleton.INSTANCE;
    }
}
