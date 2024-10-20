package com.example.views;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class InputTextField extends InputView {

    private final TextField textField;

    public InputTextField() {
        this(new TextField());
    }

    public InputTextField(TextField textField) {
        super();
        this.textField = textField;
        getChildren().add(textField);
        HBox.setHgrow(textField, Priority.ALWAYS);
    }


    public TextField getTextField() {
        return textField;
    }
}
