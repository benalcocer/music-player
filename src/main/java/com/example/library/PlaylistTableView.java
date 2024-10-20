package com.example.library;

import com.example.models.Playlist;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlaylistTableView extends TableView<Playlist> {
    private final TableColumn<Playlist, String> titleCol = new TableColumn<>();

    public PlaylistTableView() {
        getStyleClass().add("playlist-table-view");
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setPlaceholder(new Label(""));
        titleCol.setText("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        titleCol.setCellFactory(tableColumn -> new TableCell<>() {
            private final Label label = new Label();
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(label);
                }
            }
        });
        getColumns().addAll(
            titleCol
        );
    }
}
