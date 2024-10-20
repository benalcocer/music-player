package com.example.events;

import javafx.event.Event;

public class CustomEvent<T> extends Event {

    private final T value;
    private final CustomEventType<T> customEventType;

    public CustomEvent(CustomEventType<T> customEventType, T value) {
        super(customEventType.getEventType());
        this.customEventType = customEventType;
        this.value = value;
    }

    public final T getValue() {
        return value;
    }

    public CustomEventType<T> getCustomEventType() {
        return customEventType;
    }
}
