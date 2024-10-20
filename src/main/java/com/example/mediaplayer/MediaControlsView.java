package com.example.mediaplayer;

import com.example.fxutil.Subscriptions;
import com.example.util.Cleaner;
import com.example.views.IconButton;
import javafx.scene.layout.HBox;

public class MediaControlsView extends HBox implements Cleaner {

    private final IconButton playPauseButton = new IconButton(null);

    private final Subscriptions subscriptions = new Subscriptions();

    public MediaControlsView() {
        getStyleClass().add("media-controls-view");
        playPauseButton.getIconView().setFitWidth(30);
        playPauseButton.getIconView().setFitHeight(30);
        getChildren().addAll(
            playPauseButton
        );
    }

    public IconButton getPlayPauseButton() {
        return playPauseButton;
    }

    @Override
    public void initialize() {
        playPauseButton.initialize();
    }

    @Override
    public void cleanUp() {
        subscriptions.unsubscribeFromSubs();
        playPauseButton.cleanUp();
    }
}
