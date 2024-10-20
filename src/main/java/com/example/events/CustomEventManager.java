package com.example.events;

import java.util.function.Consumer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;

public class CustomEventManager {

    private Node node;

    public CustomEventManager() {
        this.node = new AnchorPane();
    }

    public CustomEventManager(Node node) {
        this.node = node;
    }

    public <T extends Event> Subscription subscribe(EventType<T> eventType, Consumer<? super Event> consumer) {
        EventHandler eventHandler = event -> {
            consumer.accept(event);
        };
        node.addEventHandler(eventType, eventHandler);
        return () -> {
            node.removeEventHandler(eventType, eventHandler);
        };
    }

    public void fireEvent(Event event) {
        node.fireEvent(event);
    }

    public <T> EventStream<CustomEvent<T>> eventsOf(CustomEventType<T> customEventType) {
        return EventStreams.eventsOf(node, customEventType.getEventType());
    }
}
