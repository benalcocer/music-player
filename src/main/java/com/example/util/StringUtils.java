package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String CODE_REGEX = "[0-9]{6}";
    private static final Pattern CODE_PATTERN = Pattern.compile(CODE_REGEX);

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 3 && password.length() <= 80;
    }

    public static boolean isValidVerificationCode(String code) {
        return code != null && CODE_PATTERN.matcher(code).matches();
    }

    public static String[] splitFileNameExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return new String[]{fileName};
        }
        return new String[]{fileName.substring(0, index), fileName.substring(index + 1)};
    }

    public static String getFileNameExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    public static boolean isSVGFileExtension(String fileName) {
        return "svg".equalsIgnoreCase(getFileNameExtension(fileName));
    }
}
