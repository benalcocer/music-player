package com.example.playlist;

import com.example.events.CenterView;
import com.example.events.EventObservables;
import com.example.fxutil.AbstractController;
import com.example.fxutil.Subscriptions;
import com.example.Main;
import com.example.models.Playlist;
import com.example.models.Song;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;
import org.reactfx.EventStreams;

public class PlaylistController extends AbstractController {

    private final PlaylistView playlistView = new PlaylistView();

    private final Subscriptions subscriptions = new Subscriptions();

    private Task<Void> selectPlaylistTask = null;

    public PlaylistController() {

    }

    @Override
    public void initialize() {
        subscriptions.addSubscription(EventStreams.valuesOf(EventObservables.getInstance().selectedPlaylist).subscribe(playlist -> {
            selectPlaylist(playlist);
        }));
        playlistView.getSongTableView().setRowFactory(tableView -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && event.getClickCount() % 2 == 0 && row.getItem() != null) {
                    EventObservables.getInstance().selectedSong.setValue(row.getItem());
                }
            });
            row.setOnMouseEntered(event -> {
                if (row.getItem() != null) {
                    row.pseudoClassStateChanged(PseudoClass.getPseudoClass("populated-hover"), true);
                }
            });
            row.setOnMouseExited(event -> {
                row.pseudoClassStateChanged(PseudoClass.getPseudoClass("populated-hover"), false);
            });
            subscriptions.addSubscription(() -> {
                row.setOnMousePressed(null);
                row.setOnMouseEntered(null);
                row.setOnMouseExited(null);
            });
            return row;
        });
        subscriptions.addSubscription(() -> playlistView.getSongTableView().setRowFactory(null));
    }

    @Override
    public void cleanUp() {
        subscriptions.unsubscribeFromSubs();
    }

    public void selectPlaylist(Playlist playlist) {
        if (selectPlaylistTask != null) {
            selectPlaylistTask.cancel();
        }
        playlistView.getSongTableView().getItems().clear();
        playlistView.getPlaylistNameLabel().setText(playlist != null ? playlist.getTitle() : "");
        selectPlaylistTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (isCancelled()) {
                    return null;
                }
                List<Song> songs = playlist == null ? List.of() : Main.getDatabase().getPlaylistSongs(playlist.getId());
                Platform.runLater(() -> {
                    if (isCancelled()) {
                        return;
                    }
                    playlistView.getSongTableView().getItems().addAll(songs);
                    EventObservables.getInstance().centerViewProperty.setValue(CenterView.PLAYLISTS);
                });
                return null;
            }
        };
        Main.executor.execute(selectPlaylistTask);
    }

    public final PlaylistView getPlaylistView() {
        return playlistView;
    }
}
