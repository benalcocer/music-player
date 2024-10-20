package com.example.events;

import javafx.event.EventType;

public class CustomEventType<T> {
    private final EventType<CustomEvent<T>> eventType;

    public CustomEventType(String eventTypeName) {
        this.eventType = new EventType<>(eventTypeName);
    }

    public EventType<CustomEvent<T>> getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventType.getName();
    }
}
