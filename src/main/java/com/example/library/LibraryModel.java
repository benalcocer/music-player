package com.example.library;

import com.example.Main;
import com.example.models.Data;
import com.example.models.Playlist;
import com.example.models.Song;
import com.example.models.User;
import com.example.util.Database;
import java.util.HashSet;
import java.util.logging.Logger;
import javafx.application.Platform;

public class LibraryModel {
    private final Data data;
    private final Database database;

    public LibraryModel(Data data, Database database) {
        this.data = data;
        this.database = database;
    }

    public Playlist savePlaylist(Playlist playlist) {
        try {
            return database.savePlaylist(playlist);
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
            return null;
        }
    }

    public void addNewPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setTitle(getUniquePlaylistTitle("New Playlist"));
        Main.getMainApplicationWindow().awaitRoutine(() -> {
            try {
                Playlist savedPlaylist = database.savePlaylist(playlist);
                if (savedPlaylist != null) {
                    data.getUser().addPlaylistId(playlist.getId());
                    User savedUser = database.updateUser(data.getUser());
                    if (savedUser != null) {
                        Platform.runLater(() -> {
                            data.getPlaylists().add(savedPlaylist);
                        });
                    }
                }
            } catch (Exception e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        });
    }

    public void addSongToPlaylist(Song song, Playlist playlist) {
        if (song == null || playlist == null) {
            Logger.getGlobal().warning("Song or Playlist was not found.");
            return;
        }
        if (!playlist.getSongIdList().contains(song.getId())) {
            playlist.getSongIdList().add(song.getId());
            Playlist savedPlaylist = savePlaylist(playlist);
            if (savedPlaylist == null) {
                playlist.getSongIdList().remove(song.getId());
            }
        }
    }

    public String getUniquePlaylistTitle(String requestedTitle) {
        HashSet<String> usedTitles = new HashSet<>(data.getUnmodifiablePlaylists().stream().map(Playlist::getTitle).toList());
        String uniqueTitle = requestedTitle;
        int count = 2;
        while (usedTitles.contains(uniqueTitle)) {
            uniqueTitle = requestedTitle + " " + count;
            count++;
        }
        return uniqueTitle;
    }

    public void deletePlaylist(Playlist playlist) {
        if (database.deletePlaylist(playlist.getId())) {
            data.getPlaylists().remove(playlist);
        }
    }
}
