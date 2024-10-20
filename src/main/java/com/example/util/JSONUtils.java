package com.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JSONUtils() {

    }

    /**
     * Convert a JSON String into an Object of class tClass.
     */
    public static <T> T convertToObject(Class<T> tClass, String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, tClass);
    }

    /**
     * Convert an Object into a JSON String.
     */
    public static String convertToJSON(Object object) {
        try {
            return mapper.writer().writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }
}
