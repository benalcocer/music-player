package com.example.util;

public class Constants {
    public final String DATABASE_HOST = Environment.getInstance().get("DATABASE_HOST").orElse("");
    public final double MIN_WINDOW_WIDTH = 426.0;
    public final double MIN_WINDOW_HEIGHT = 240.0;

    private Constants() {

    }

    private static class ConstantsSingleton {
        private static final Constants INSTANCE = new Constants();
    }

    public static Constants getInstance() {
        return Constants.ConstantsSingleton.INSTANCE;
    }
}
