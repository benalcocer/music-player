package com.example.mediaplayer;

import com.example.util.Cleaner;
import com.example.fxutil.Subscriptions;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import org.reactfx.EventStreams;

public class MediaPlayerModel implements Cleaner {

    private final SimpleObjectProperty<MediaPlayer> mediaPlayerProperty = new SimpleObjectProperty<>(null);

    private final Subscriptions mediaPlayerSubs = new Subscriptions();

    private final ReadOnlyBooleanWrapper isEndOfMedia = new ReadOnlyBooleanWrapper(false);

    // Status
    private final ReadOnlyObjectWrapper<Status> currentStatus = new ReadOnlyObjectWrapper(null);

    // Volume
    private final ReadOnlyDoubleWrapper volumeProperty = new ReadOnlyDoubleWrapper(1.0);
    private final ReadOnlyBooleanWrapper muteProperty = new ReadOnlyBooleanWrapper(false);

    // Time
    private final ReadOnlyDoubleWrapper currentTime = new ReadOnlyDoubleWrapper(0.0);

    private final ReadOnlyDoubleWrapper startTime = new ReadOnlyDoubleWrapper(0.0);

    private final ReadOnlyDoubleWrapper stopTime = new ReadOnlyDoubleWrapper(0.0);

    public MediaPlayerModel() {

    }

    public Optional<MediaPlayer> getMediaPlayer() {
        return Optional.ofNullable(mediaPlayerProperty.getValue());
    }

    public void setSource(String url) {
        MediaPlayer newMediaPlayer;
        try {
            newMediaPlayer = new MediaPlayer(new Media(url));
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
            return;
        }
        MediaPlayer previousMediaPlayer = getMediaPlayer().orElse(null);
        mediaPlayerProperty.setValue(newMediaPlayer);
        mediaPlayerSubs.unsubscribeFromSubs();
        if (previousMediaPlayer != null) {
            previousMediaPlayer.stop();
        }
        initialize();
    }

    public void play() {
        MediaPlayer mediaPlayer = mediaPlayerProperty.getValue();
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        MediaPlayer mediaPlayer = mediaPlayerProperty.getValue();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        MediaPlayer mediaPlayer = mediaPlayerProperty.getValue();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void toggleMute() {
        muteProperty.setValue(!muteProperty.getValue());
    }

    public void setVolume(double volume) {
        volumeProperty.setValue(
            Math.max(0.0, Math.min(volume, 1.0))
        );
    }

    public boolean getIsMuted() {
        return muteProperty.getValue();
    }

    public ReadOnlyBooleanProperty getIsMutedProperty() {
        return muteProperty.getReadOnlyProperty();
    }

    public double getVolume() {
        return volumeProperty.getValue();
    }

    public double getStartTime() {
        return mediaPlayerProperty.getValue() != null ? durationToSeconds(mediaPlayerProperty.getValue().getStartTime()) : 0.0;
    }

    public double getStopTime() {
        return mediaPlayerProperty.getValue() != null ? durationToSeconds(mediaPlayerProperty.getValue().getStopTime()) : 0.0;
    }

    public final Status getStatus() {
        return currentStatus.getValue();
    }

    public ReadOnlyDoubleProperty getStartTimeProperty() {
        return startTime.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty getStopTimeProperty() {
        return stopTime.getReadOnlyProperty();
    }

    public final ReadOnlyObjectProperty<Status> getStatusProperty() {
        return currentStatus.getReadOnlyProperty();
    }

    public final ReadOnlyDoubleProperty getCurrentTimeProperty() {
        return currentTime.getReadOnlyProperty();
    }

    /**
     * Seek the MediaPlayer to the seconds given.
     *
     * @param seconds The time in the song to seek to.
     */
    public void seek(double seconds) {
        getMediaPlayer().ifPresent(mediaPlayer -> {
            mediaPlayer.seek(Duration.seconds(seconds));
        });
    }

    public boolean isEndOfMedia() {
        return isEndOfMedia.getValue();
    }

    public ReadOnlyBooleanProperty getIsEndOfMediaProperty() {
        return isEndOfMedia.getReadOnlyProperty();
    }

    @Override
    public void initialize() {
        mediaPlayerSubs.addSubscription(EventStreams.changesOf(volumeProperty).subscribe(volumeChange -> {
            if (volumeChange.getNewValue() == null) {
                return;
            }
            getMediaPlayer().ifPresent(mediaPlayer -> {
                mediaPlayer.setVolume(volumeChange.getNewValue().doubleValue());
            });
        }));
        mediaPlayerSubs.addSubscription(EventStreams.changesOf(muteProperty).subscribe(muteChange -> {
            if (muteChange.getNewValue() == null) {
                return;
            }
            getMediaPlayer().ifPresent(mediaPlayer -> {
                mediaPlayer.setMute(muteChange.getNewValue());
            });
        }));

        currentStatus.unbind();
        startTime.setValue(0.0);
        stopTime.setValue(0.0);
        MediaPlayer mediaPlayer = getMediaPlayer().orElse(null);
        if (mediaPlayer == null) {
            return;
        }
        currentStatus.bind(mediaPlayer.statusProperty());
        mediaPlayer.setVolume(getVolume());
        mediaPlayer.setMute(muteProperty.getValue());
        mediaPlayerSubs.addSubscription(
            EventStreams.valuesOf(mediaPlayer.currentTimeProperty()).subscribe(newValue -> {
                if (newValue == null) {
                    return;
                }
                isEndOfMedia.setValue(false);
                currentTime.setValue(durationToSeconds(newValue));
                startTime.setValue(getStartTime());
                stopTime.setValue(getStopTime());
            })
        );
        mediaPlayer.setOnError(() -> {
            if (mediaPlayer.getError() != null) {
                Logger.getGlobal().warning(mediaPlayer.getError().getMessage());
            }
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            isEndOfMedia.setValue(true);
        });
        mediaPlayerSubs.addSubscription(() -> {
            mediaPlayer.setOnError(null);
            mediaPlayer.setOnEndOfMedia(null);
        });
    }

    @Override
    public void cleanUp() {
        mediaPlayerSubs.unsubscribeFromSubs();
        getMediaPlayer().ifPresent(mediaPlayer -> {
            mediaPlayer.stop();
        });
        mediaPlayerProperty.setValue(null);
    }

    private static double durationToSeconds(Duration duration) {
        double value = duration != null ? duration.toSeconds() : 0.0;
        return Double.isNaN(value) || value < 0.0 ? 0.0 : value;
    }
}
