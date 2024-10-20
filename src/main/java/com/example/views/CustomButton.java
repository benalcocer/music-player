package com.example.views;

import com.example.events.EventObservables;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class CustomButton extends Button {

    private final EventHandler<MouseEvent> mouseEnteredHandler = event -> {
        EventObservables.getInstance().currentCursor.setValue(Cursor.HAND);
    };
    private final EventHandler<MouseEvent> mouseExitedHandler = event -> {
        EventObservables.getInstance().currentCursor.setValue(Cursor.DEFAULT);
    };

    public CustomButton() {
        super();
        initialize();
    }

    public CustomButton(String text) {
        super(text);
        initialize();
    }

    private void initialize() {
        addEventHandler(MouseEvent.MOUSE_ENTERED, new WeakEventHandler<>(mouseEnteredHandler));
        addEventHandler(MouseEvent.MOUSE_EXITED, new WeakEventHandler<>(mouseExitedHandler));
    }
}
