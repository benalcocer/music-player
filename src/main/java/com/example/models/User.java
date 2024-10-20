package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class User extends BaseMongoModel {

    @JsonProperty("email")
    private String email;
    @JsonProperty("playlistIds")
    private List<String> playlistIds;

    public User() {

    }

    public void addPlaylistId(String playlistId) {
        if (!playlistIds.contains(playlistId)) {
            playlistIds.add(playlistId);
        }
    }
}
