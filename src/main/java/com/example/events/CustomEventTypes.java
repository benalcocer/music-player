package com.example.events;

import com.example.models.Playlist;
import com.example.models.Song;
import com.example.models.User;
import javafx.util.Pair;

public class CustomEventTypes {
    public static final CustomEventType<Void> STYLESHEET_CHANGE = new CustomEventType<>("STYLESHEET_CHANGE");
    public static final CustomEventType<Pair<Song, Playlist>> SONG_ADDED_TO_PLAYLIST = new CustomEventType<>("SONG_ADDED_TO_PLAYLIST");
    public static final CustomEventType<User> LOGIN_SUCCESS = new CustomEventType<>("LOGIN_SUCCESS");
}
