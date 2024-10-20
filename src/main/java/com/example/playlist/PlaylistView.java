package com.example.playlist;

import com.example.fxutil.View;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PlaylistView extends View {
    private final SongTableView songTableView = new SongTableView();
    private final Label playlistLabel = new Label("Playlist");
    private final Label playlistNameLabel = new Label("");

    public PlaylistView() {
        getStyleClass().add("playlist-view");
        playlistNameLabel.getStyleClass().add("playlist-name-label");
        getChildren().addAll(
            playlistLabel,
            playlistNameLabel,
            songTableView
        );
        VBox.setVgrow(songTableView, Priority.ALWAYS);
    }

    public SongTableView getSongTableView() {
        return songTableView;
    }

    public Label getPlaylistNameLabel() {
        return playlistNameLabel;
    }
}
