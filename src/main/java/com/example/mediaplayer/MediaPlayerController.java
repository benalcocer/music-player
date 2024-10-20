package com.example.mediaplayer;

import com.example.events.EventObservables;
import com.example.fxutil.AbstractController;
import com.example.Main;
import com.example.fxutil.Icon;
import com.example.util.BigDecimals;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.media.MediaPlayer.Status;
import org.reactfx.EventStreams;

public class MediaPlayerController extends AbstractController {

    // Models
    private final MediaPlayerModel mediaPlayerModel;

    // Views
    private final ControlsView controlsView;

    public MediaPlayerController() {
        mediaPlayerModel = new MediaPlayerModel();
        controlsView = new ControlsView();
    }

    @Override
    public void initialize() {
        controlsView.initialize();
        subscriptions.addSubscription(EventStreams.valuesOf(EventObservables.getInstance().selectedSong).subscribe(song -> {
            if (song == null) {
                controlsView.getCurrentlyPlayingLabel().setText("");
                return;
            }
            Main.getMainApplicationWindow().awaitRoutine(() -> {
                String signedURI = Main.getDatabase().getSignedURI(song.getId());
                if (signedURI == null) {
                    Logger.getGlobal().warning("Failed to get signedURI for " + song.getId());
                    return;
                }
                Platform.runLater(() -> {
                    mediaPlayerModel.stop();
                    try {
                        mediaPlayerModel.setSource(signedURI);
                        mediaPlayerModel.play();
                        controlsView.getCurrentlyPlayingLabel().setText(song.getTitle());
                    } catch (Exception e) {
                        Logger.getGlobal().severe(e.getMessage());
                    }
                });
            });
        }));
        // Volume
        subscriptions.addSubscription(EventStreams.valuesOf(controlsView.getAdditionalControls().getVolumeSlider().valueProperty()).subscribe(volume -> {
            mediaPlayerModel.setVolume(volume.doubleValue() / 100);
        }));
        // Volume mute
        subscriptions.addSubscription(EventStreams.valuesOf(mediaPlayerModel.getIsMutedProperty()).subscribe(isMuted -> {
            controlsView.getAdditionalControls().getVolumeButton().getIconView().setIcon(isMuted ? Icon.VOLUME_MUTE : Icon.VOLUME);
        }));
        controlsView.getAdditionalControls().getVolumeButton().setOnMousePressed(event -> {
            mediaPlayerModel.toggleMute();
        });
        subscriptions.addSubscription(() -> {
            controlsView.getAdditionalControls().getVolumeButton().setOnMousePressed(null);
        });
        // Time
        subscriptions.addSubscription(
            EventStreams.valuesOf(mediaPlayerModel.getStopTimeProperty()).subscribe(time -> {
                controlsView.getTimelineView().getEndTimeLabel().setText(secondsToMinutes(time.doubleValue()));
            })
        );
        subscriptions.addSubscription(
            EventStreams.valuesOf(mediaPlayerModel.getCurrentTimeProperty()).subscribe(time -> {
                if (!controlsView.getTimelineView().getTimelineSlider().isSeeking() && time != null) {
                    if (!controlsView.getTimelineView().getTimelineSlider().isSeeking()) {
                        Main.executor.execute(() -> {
                            double songLength = mediaPlayerModel.getStopTime() - mediaPlayerModel.getStartTime();
                            double currentTime = songLength > 0.0 ? time.doubleValue() / songLength * 100 : 0.0;
                            String currentTimeString = secondsToMinutes(time.doubleValue());
                            Platform.runLater(() -> {
                                controlsView.getTimelineView().setTimeline(currentTime);
                                controlsView.getTimelineView().getStartTimeLabel().setText(currentTimeString);
                            });
                        });
                    }
                }
            })
        );
        subscriptions.addSubscription(
            EventStreams.valuesOf(controlsView.getTimelineView().getTimelineSlider().valueProperty()).subscribe(timelineValue -> {
                if (controlsView.getTimelineView().getTimelineSlider().isSeeking()) {
                    double songLength = mediaPlayerModel.getStopTime() - mediaPlayerModel.getStartTime();
                    double currentTime = controlsView.getTimelineView().getTimelineSlider().getValue() / 100 * songLength;
                    String currentTimeString = secondsToMinutes(currentTime);
                    controlsView.getTimelineView().getStartTimeLabel().setText(currentTimeString);
                }
            })
        );
        subscriptions.addSubscription(
            EventStreams.valuesOf(controlsView.getTimelineView().getTimelineSlider().getIsSeekingProperty()).subscribe(isSeeking -> {
                if (!isSeeking) {
                    double songLength = mediaPlayerModel.getStopTime() - mediaPlayerModel.getStartTime();
                    double currentTime = controlsView.getTimelineView().getTimelineSlider().getValue() / 100 * songLength;
                    mediaPlayerModel.seek(currentTime);
                }
            })
        );
        // Media Controls
        subscriptions.addSubscription(
            EventStreams.valuesOf(mediaPlayerModel.getStatusProperty()).subscribe(status -> {
                if (status == Status.PLAYING) {
                    controlsView.getMediaControlsView().getPlayPauseButton().getIconView().setIcon(Icon.PAUSE);
                } else {
                    controlsView.getMediaControlsView().getPlayPauseButton().getIconView().setIcon(Icon.PLAY);
                }
            })
        );
        subscriptions.addSubscription(
            EventStreams.eventsOf(controlsView.getMediaControlsView().getPlayPauseButton(), ActionEvent.ACTION).subscribe(event -> {
                if (mediaPlayerModel.getStatus() == Status.PLAYING) {
                    mediaPlayerModel.pause();
                } else {
                    mediaPlayerModel.play();
                }
            })
        );
    }

    @Override
    public void cleanUp() {
        controlsView.cleanUp();
        subscriptions.unsubscribeFromSubs();
        mediaPlayerModel.cleanUp();
    }

    private static String secondsToMinutes(double seconds) {
        StringBuilder sb = new StringBuilder();

        double minutes = seconds / 60.0;
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(minutes));
        int minuteValue = bigDecimal.intValue();
        int secondsValue = bigDecimal.subtract(new BigDecimal(minuteValue)).multiply(BigDecimals.SIXTY).intValue();

        sb.append(minuteValue);
        sb.append(":");
        if (secondsValue < 10) {
            sb.append("0");
        }
        sb.append(secondsValue);
        return sb.toString();
    }

    public ControlsView getControlsView() {
        return controlsView;
    }
}
