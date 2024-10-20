package com.example.search;

import com.example.playlist.SongTableView;
import com.example.fxutil.View;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchView extends View {

    private final TextField textField = new TextField();

    private final SongTableView songTableView = new SongTableView();

    public SearchView() {
        getStyleClass().add("search-view");
        textField.setPromptText("What do you want to listen to?");
        getChildren().addAll(
            textField,
            songTableView
        );
        VBox.setVgrow(songTableView, Priority.ALWAYS);
    }

    public SongTableView getSongTableView() {
        return songTableView;
    }

    public final TextField getTextField() {
        return textField;
    }
}
