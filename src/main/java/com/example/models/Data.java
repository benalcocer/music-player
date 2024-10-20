package com.example.models;

import com.example.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {

    private User user = null;
    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    private final ObservableList<Playlist> unmodifiablePlaylists = FXCollections.unmodifiableObservableList(playlists);

    public Data() {

    }

    public ObservableList<Playlist> getPlaylists() {
        return playlists;
    }

    public ObservableList<Playlist> getUnmodifiablePlaylists() {
        return unmodifiablePlaylists;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void refreshData(Database database) {
        playlists.clear();
        if (user == null) {
            return;
        }
        playlists.addAll(database.getFromUser(user));
    }
}
