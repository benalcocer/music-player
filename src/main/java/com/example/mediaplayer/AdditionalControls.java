package com.example.mediaplayer;

import com.example.fxutil.CustomSlider;
import com.example.fxutil.Icon;
import com.example.util.Cleaner;
import com.example.views.IconButton;
import javafx.scene.layout.HBox;

/**
 * Additional Controls such as the volume controls.
 */
public class AdditionalControls extends HBox implements Cleaner {

    /**
     * The Volume button.
     */
    private final IconButton volumeButton = new IconButton(Icon.VOLUME);

    /**
     * A Slider that allows control of the volume.
     */
    private final CustomSlider volumeSlider = new CustomSlider(0, 100, 100);

    public AdditionalControls() {
        getStyleClass().add("additional-controls");
        volumeSlider.getStyleClass().add("volume-slider");
        getChildren().addAll(
            volumeButton,
            volumeSlider
        );
    }

    public IconButton getVolumeButton() {
        return volumeButton;
    }

    public CustomSlider getVolumeSlider() {
        return volumeSlider;
    }

    @Override
    public void initialize() {
        volumeButton.initialize();
    }

    @Override
    public void cleanUp() {
        volumeButton.cleanUp();
    }
}
