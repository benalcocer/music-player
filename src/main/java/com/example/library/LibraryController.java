package com.example.library;

import com.example.events.CenterView;
import com.example.events.CustomEventTypes;
import com.example.events.EventObservables;
import com.example.fxutil.AbstractController;
import com.example.Main;
import com.example.models.Playlist;
import com.example.views.CustomContextMenu;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import org.reactfx.EventStreams;

public class LibraryController extends AbstractController {

    private final LibraryModel libraryModel;
    private final LibraryView libraryView;

    private final PlaylistContextMenu playlistContextMenu = new PlaylistContextMenu();

    public LibraryController() {
        this.libraryModel = new LibraryModel(Main.getData(), Main.getDatabase());
        this.libraryView = new LibraryView();
    }

    @Override
    public void initialize() {
        libraryView.getPlaylistTableView().setItems(Main.getData().getUnmodifiablePlaylists());
        libraryView.getPlaylistTableView().setRowFactory(tableView -> {
            TableRow<Playlist> row = new TableRow<>();
            row.setContextMenu(playlistContextMenu);
            row.setOnContextMenuRequested(event -> {
                playlistContextMenu.addMenuItems(libraryModel, row.getItem());
                playlistContextMenu.show(Main.getMainApplicationWindow().getStage(), event.getScreenX(), event.getScreenY());
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
                row.setContextMenu(null);
                row.setOnContextMenuRequested(null);
                row.setOnMouseEntered(null);
                row.setOnMouseExited(null);
            });
            return row;
        });
        subscriptions.addSubscription(() -> libraryView.getPlaylistTableView().setRowFactory(null));

        subscriptions.addSubscription(EventStreams.valuesOf(libraryView.getPlaylistTableView().getSelectionModel().selectedItemProperty()).subscribe(playlist -> {
            EventObservables.getInstance().selectedPlaylist.setValue(playlist);
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(libraryView.getAddPlaylistButton(), ActionEvent.ACTION).subscribe(event -> {
            libraryModel.addNewPlaylist();
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(libraryView.getSearchButton(), ActionEvent.ACTION).subscribe(event -> {
            EventObservables.getInstance().centerViewProperty.setValue(CenterView.SEARCH);
        }));
        subscriptions.addSubscription(Main.globalEventManager.eventsOf(CustomEventTypes.SONG_ADDED_TO_PLAYLIST).subscribe(event -> {
            libraryModel.addSongToPlaylist(event.getValue().getKey(), event.getValue().getValue());
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(libraryView.getSettingsButton(), ActionEvent.ACTION).subscribe(event -> {
            EventObservables.getInstance().centerViewProperty.setValue(CenterView.SETTINGS);
        }));
    }

    @Override
    public void cleanUp() {
        subscriptions.unsubscribeFromSubs();
        libraryView.getPlaylistTableView().setItems(FXCollections.emptyObservableList());
    }

    public final LibraryView getLibraryView() {
        return libraryView;
    }

    /**
     * The ContextMenu to show for a Playlist.
     */
    private static class PlaylistContextMenu extends CustomContextMenu {
        public PlaylistContextMenu() {
            super();
        }

        @Override
        protected void onHidden() {
            subscriptions.unsubscribeFromSubs();
            getItems().clear();
        }

        public void addMenuItems(LibraryModel libraryModel, Playlist playlist) {
            MenuItem deletePlaylistItem = new MenuItem("Delete Playlist");
            subscriptions.addSubscription(EventStreams.eventsOf(deletePlaylistItem, ActionEvent.ACTION).subscribe(event -> {
                libraryModel.deletePlaylist(playlist);
            }));
            getItems().addAll(
                deletePlaylistItem
            );
        }
    }

}
