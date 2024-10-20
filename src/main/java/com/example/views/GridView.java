package com.example.views;

import com.example.fxutil.View;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridView extends View {

    private final GridPane gridPane = new GridPane();
    private final ArrayList<ArrayList<Node>> nodesGrid = new ArrayList<>();

    public GridView() {
        getStyleClass().add("grid-view");
        gridPane.getStyleClass().add("grid-pane");
        getChildren().add(gridPane);
    }

    public void clear() {
        gridPane.getChildren().clear();
        nodesGrid.clear();
    }

    public int rowCount() {
        return nodesGrid.size();
    }

    public void addRow(Node... nodes) {
        nodesGrid.add(new ArrayList<>(List.of(nodes)));
        gridPane.addRow(rowCount(), nodes);
    }

    public void setRow(int index, Node... nodes) {
        List<Node> nodesList = List.of(nodes);

        gridPane.getChildren().removeAll(getRow(index));
        gridPane.addRow(index, nodes);
        getRow(index).clear();
        getRow(index).addAll(List.of(nodes));
    }

    public ArrayList<Node> getRow(int index) {
        return nodesGrid.get(index);
    }
}
