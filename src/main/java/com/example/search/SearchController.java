package com.example.search;

import com.example.Main;
import com.example.events.CenterView;
import com.example.events.EventObservables;
import com.example.fxutil.AbstractController;
import com.example.fxutil.Subscriptions;
import com.example.models.Song;
import com.example.playlist.SongContextMenu;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.control.TableRow;
import org.reactfx.EventStreams;

public class SearchController extends AbstractController {
    private final SearchModel searchModel;
    private final SearchView searchView;
    private final SongContextMenu songContextMenu = new SongContextMenu();

    private final Subscriptions subscriptions = new Subscriptions();

    public SearchController() {
        searchModel = new SearchModel();
        searchView = new SearchView();
        initialize();
    }

    @Override
    public void initialize() {
        subscriptions.addSubscription(EventStreams.changesOf(EventObservables.getInstance().centerViewProperty).subscribe(centerViewChange -> {
            if (centerViewChange.getOldValue() == CenterView.SEARCH) {
                searchModel.handleClose();
            }
        }));
        subscriptions.addSubscription(EventStreams.changesOf(searchView.getTextField().textProperty()).subscribe(textChange -> {
            searchModel.handleTextChange(textChange.getOldValue(), textChange.getNewValue());
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(searchView.getTextField(), ActionEvent.ACTION).subscribe(event -> {
            searchModel.searchImmediately(searchView.getTextField().getText());
        }));
        searchView.getSongTableView().setItems(searchModel.getSongsList());
        searchView.getSongTableView().setRowFactory(tableView -> {
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
            row.setContextMenu(songContextMenu);
            row.setOnContextMenuRequested(event -> {
                songContextMenu.addPlaylistMenuItems(row.getItem(), Main.getData().getUnmodifiablePlaylists());
                songContextMenu.show(Main.getMainApplicationWindow().getStage());
            });
            subscriptions.addSubscription(() -> {
                row.setOnMousePressed(null);
                row.setOnMouseEntered(null);
                row.setOnMouseExited(null);
                row.setContextMenu(null);
                row.setOnContextMenuRequested(null);
            });
            return row;
        });
        subscriptions.addSubscription(() -> searchView.getSongTableView().setRowFactory(null));
    }

    @Override
    public void cleanUp() {
        subscriptions.unsubscribeFromSubs();
    }

    public SearchView getSearchView() {
        return searchView;
    }
}
