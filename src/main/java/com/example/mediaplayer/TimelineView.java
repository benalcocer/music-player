package com.example.mediaplayer;

import com.example.fxutil.CustomSlider;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TimelineView extends HBox {

    /**
     * A Slider that allows control of the timeline.
     */
    private final CustomSlider timelineSlider = new CustomSlider(0, 100, 0);

    /**
     * The start time label.
     */
    private final Label startTimeLabel = new Label("0:00");

    /**
     * The end time label.
     */
    private final Label endTimeLabel = new Label("0:00");

    public TimelineView() {
        getStyleClass().add("timeline-view");
        getChildren().addAll(
            startTimeLabel,
            timelineSlider,
            endTimeLabel
        );
        HBox.setHgrow(timelineSlider, Priority.ALWAYS);
    }

    public void setTimeline(double timelineValue) {
        setSliderValue(timelineSlider, timelineValue);
    }

    private static void setSliderValue(Slider slider, double value) {
        slider.setValue(
            Math.max(
                slider.getMin(),
                Math.min(slider.getMax(), value)
            )
        );
    }

    public CustomSlider getTimelineSlider() {
        return timelineSlider;
    }

    public Label getStartTimeLabel() {
        return startTimeLabel;
    }

    public Label getEndTimeLabel() {
        return endTimeLabel;
    }
}
