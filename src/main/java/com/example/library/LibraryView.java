package com.example.library;

import com.example.fxutil.View;
import com.example.views.CustomButton;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LibraryView extends View {
    private final PlaylistTableView playlistTableView = new PlaylistTableView();
    private final CustomButton settingsButton = new CustomButton("Settings");
    private final CustomButton searchButton = new CustomButton("Search");
    private final CustomButton addPlaylistButton = new CustomButton("Add new Playlist");

    public LibraryView() {
        getStyleClass().add("library-view");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("Your Library"));

        getChildren().addAll(
            hBox,
            settingsButton,
            searchButton,
            addPlaylistButton,
            playlistTableView
        );
        VBox.setVgrow(playlistTableView, Priority.ALWAYS);
    }

    public PlaylistTableView getPlaylistTableView() {
        return playlistTableView;
    }

    public CustomButton getSettingsButton() {
        return settingsButton;
    }

    public CustomButton getSearchButton() {
        return searchButton;
    }

    public CustomButton getAddPlaylistButton() {
        return addPlaylistButton;
    }
}
