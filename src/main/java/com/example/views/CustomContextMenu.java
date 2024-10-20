package com.example.views;

import com.example.fxutil.Subscriptions;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.control.ContextMenu;
import javafx.stage.WindowEvent;

public abstract class CustomContextMenu extends ContextMenu {

    protected final Subscriptions subscriptions = new Subscriptions();

    private final EventHandler<WindowEvent> hiddenEventHandler = event -> {
        onHidden();
    };

    public CustomContextMenu() {
        addEventHandler(WindowEvent.WINDOW_HIDDEN, new WeakEventHandler<>(hiddenEventHandler));
    }

    /**
     * Handle when the ContextMenu window is hidden.
     */
    protected abstract void onHidden();
}
