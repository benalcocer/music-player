package com.example.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.UUID;

public class BaseMongoModel {
    private String id;

    public BaseMongoModel() {
        id = UUID.randomUUID().toString();
    }

    public BaseMongoModel(String id) {
        this.id = id;
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(String id) {
        this.id = id;
    }
}
