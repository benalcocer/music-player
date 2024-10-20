package com.example.fxutil;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class CustomSlider extends Slider {

    /**
     * Boolean property that indicates if the Slider is seeking.
     */
    private final ReadOnlyBooleanWrapper isSeeking = new ReadOnlyBooleanWrapper(false);

    private final EventHandler<MouseEvent> mousePressedEventHandler = event -> {
        isSeeking.setValue(true);
    };

    private final EventHandler<MouseEvent> mouseReleasedEventHandler = event -> {
        isSeeking.setValue(false);
    };

    public CustomSlider(double min, double max, double value) {
        super(min, max, value);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, new WeakEventHandler<>(mousePressedEventHandler));
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, new WeakEventHandler<>(mouseReleasedEventHandler));
    }

    public ReadOnlyBooleanProperty getIsSeekingProperty() {
        return isSeeking.getReadOnlyProperty();
    }

    public boolean isSeeking() {
        return isSeeking.getValue();
    }
}
