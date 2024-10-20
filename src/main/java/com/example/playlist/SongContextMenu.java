package com.example.playlist;

import com.example.Main;
import com.example.events.CustomEvent;
import com.example.events.CustomEventTypes;
import com.example.models.Playlist;
import com.example.models.Song;
import com.example.views.CustomContextMenu;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.util.Pair;

public class SongContextMenu extends CustomContextMenu {
    private final Menu addPlaylistMenu = new Menu("Add to playlist");

    public SongContextMenu() {
        super();
        getItems().addAll(
            addPlaylistMenu
        );
    }

    @Override
    protected void onHidden() {
        subscriptions.unsubscribeFromSubs();
        clearPlaylistMenuItems();
    }

    public void addPlaylistMenuItems(Song song, ObservableList<Playlist> playlists) {
        addPlaylistMenu.getItems().clear();
        ArrayList<MenuItem> playlistMenuItems = new ArrayList<>();
        playlists.forEach(playlist -> {
            MenuItem playlistMenuItem = new MenuItem(playlist.getTitle());
            playlistMenuItem.setOnAction(event -> {
                Main.globalEventManager.fireEvent(new CustomEvent<>(CustomEventTypes.SONG_ADDED_TO_PLAYLIST, new Pair<>(song, playlist)));
            });
            subscriptions.addSubscription(() -> playlistMenuItem.setOnAction(null));
            playlistMenuItems.add(playlistMenuItem);
        });
        addPlaylistMenu.getItems().addAll(playlistMenuItems);
    }

    public void clearPlaylistMenuItems() {
        addPlaylistMenu.getItems().clear();
    }
}
