package com.example.mediaplayer;

import com.example.util.Cleaner;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ControlsView extends HBox implements Cleaner {

    private final VBox leftContainer = new VBox();
    private final Label currentlyPlayingLabel = new Label("");

    private final VBox middleContainer = new VBox();

    private final AdditionalControls additionalControls = new AdditionalControls();

    private final Region leftRegion = new Region();
    private final Region rightRegion = new Region();

    private final MediaControlsView mediaControlsView = new MediaControlsView();
    private final TimelineView timelineView = new TimelineView();

    public ControlsView() {
        getStyleClass().add("controls-view");

        leftContainer.getStyleClass().add("left-container");
        leftContainer.setFillWidth(true);
        leftContainer.getChildren().addAll(
            currentlyPlayingLabel
        );

        middleContainer.getStyleClass().add("middle-container");
        middleContainer.setFillWidth(true);
        middleContainer.getChildren().addAll(
            mediaControlsView,
            timelineView
        );
        getChildren().addAll(
            leftContainer,
            leftRegion,
            middleContainer,
            rightRegion,
            additionalControls
        );
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        HBox.setHgrow(middleContainer, Priority.ALWAYS);
        HBox.setHgrow(rightRegion, Priority.ALWAYS);
    }

    public AdditionalControls getAdditionalControls() {
        return additionalControls;
    }

    public MediaControlsView getMediaControlsView() {
        return mediaControlsView;
    }

    public TimelineView getTimelineView() {
        return timelineView;
    }

    public Label getCurrentlyPlayingLabel() {
        return currentlyPlayingLabel;
    }

    @Override
    public void initialize() {
        additionalControls.initialize();
        mediaControlsView.initialize();
    }

    @Override
    public void cleanUp() {
        additionalControls.cleanUp();
        mediaControlsView.cleanUp();
        currentlyPlayingLabel.setText("");
    }
}
