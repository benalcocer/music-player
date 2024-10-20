package com.example.views;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InputView extends HBox {

    private final Label prefix = new Label();

    public InputView() {
        super();
        prefix.getStyleClass().add("prompt");
        getStyleClass().add("input-view");

        prefix.visibleProperty().bind(prefix.textProperty().isNotEmpty());
        prefix.managedProperty().bind(prefix.visibleProperty());
    }

    public Label getPrefixLabel() {
        return prefix;
    }
}
