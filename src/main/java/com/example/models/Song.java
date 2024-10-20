package com.example.models;

public class Song extends BaseMongoModel {

    private String title = "";
    private String artist = "";
    private String link = "";

    public Song() {

    }

    public Song(String id, String title, String artist, String link) {
        this(title, artist, link);
        setId(id);
    }

    public Song(String title, String artist, String link) {
        this.title = title;
        this.artist = artist;
        this.link = link;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
