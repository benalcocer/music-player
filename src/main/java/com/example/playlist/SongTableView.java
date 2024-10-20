package com.example.playlist;

import com.example.models.Song;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SongTableView extends TableView<Song> {
    private final TableColumn<Song, String> indexCol = createColumn(0.05);
    private final TableColumn<Song, String> titleCol = createColumn(0.45);
    private final TableColumn<Song, String> artistCol = createColumn(0.45);

    private TableColumn<Song, String> createColumn(double widthPercentage) {
        TableColumn<Song, String> column = new TableColumn<>();
        column.setReorderable(false);
        column.setResizable(false);
        column.prefWidthProperty().bind(this.widthProperty().multiply(widthPercentage));
        return column;
    }

    public SongTableView() {
        getStyleClass().add("song-table-view");
        setPlaceholder(new Label(""));
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        indexCol.setText("#");
        indexCol.setCellFactory(col -> {
            TableCell<Song, String> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.when(cell.emptyProperty())
                .then("")
                .otherwise(cell.indexProperty().asString()));
            return cell;
        });
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
        artistCol.setText("Artist");
        artistCol.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        artistCol.setCellFactory(tableColumn -> new TableCell<>() {
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
            indexCol,
            titleCol,
            artistCol
        );
    }
}
