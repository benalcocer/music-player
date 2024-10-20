package com.example.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.ArrayList;
import java.util.List;

public class Playlist extends BaseMongoModel {

    private String title = "";

    private final ArrayList<String> songIds = new ArrayList<>();

    public Playlist() {
        super();
    }

    public Playlist(String title, List<String> songIds) {
        this();
        this.title = title;
        this.songIds.addAll(songIds);
    }

    public Playlist(String id, String title, List<String> songIds) {
        super(id);
        this.title = title;
        this.songIds.addAll(songIds);
    }

    public Playlist(String title) {
        this.title = title;
    }

    @JsonGetter("title")
    public String getTitle() {
        return title;
    }

    @JsonSetter("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonGetter("songIds")
    public String[] getSongIds() {
        return songIds.toArray(String[]::new);
    }

    @JsonSetter("songIds")
    public void setSongIds(String[] songIds) {
        this.songIds.addAll(List.of(songIds));
    }

    @JsonIgnore
    public ArrayList<String> getSongIdList() {
        return songIds;
    }
}
