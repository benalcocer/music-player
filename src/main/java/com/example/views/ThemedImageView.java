package com.example.views;

import com.example.Main;
import com.example.events.CustomEventTypes;
import com.example.fxutil.CustomStyleSheet;
import com.example.fxutil.StyleSheetManager;
import com.example.fxutil.Subscriptions;
import com.example.util.Cleaner;
import javafx.scene.image.ImageView;

public abstract class ThemedImageView extends ImageView implements Cleaner {

    private final Subscriptions subscriptions = new Subscriptions();

    public ThemedImageView() {

    }

    public abstract void updateTheme(CustomStyleSheet currentTheme);

    @Override
    public void initialize() {
        updateTheme(StyleSheetManager.getInstance().getTheme());
        subscriptions.addSubscription(
            Main.globalEventManager.eventsOf(CustomEventTypes.STYLESHEET_CHANGE).subscribe(event -> {
                updateTheme(StyleSheetManager.getInstance().getTheme());
            })
        );
    }

    @Override
    public void cleanUp() {
        subscriptions.unsubscribeFromSubs();
        setImage(null);
    }
}
