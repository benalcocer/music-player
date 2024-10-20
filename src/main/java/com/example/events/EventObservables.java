package com.example.events;

import com.example.models.Playlist;
import com.example.models.Song;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;

/**
 * Singleton class containing global Observables that can be modified and observed on the FX Application Thread.
 */
public class EventObservables {

    /**
     * Object property tracking the current CenterView.
     */
    public final SimpleObjectProperty<CenterView> centerViewProperty = new SimpleObjectProperty<>(null);
    /**
     * Object property tracking the selected Playlist.
     */
    public final SimpleObjectProperty<Playlist> selectedPlaylist = new SimpleObjectProperty<>(null);
    /**
     * Object property tracking the selected Song.
     */
    public final SimpleObjectProperty<Song> selectedSong = new SimpleObjectProperty<>(null);
    /**
     * Object property tracking the current mouse cursor.
     */
    public final SimpleObjectProperty<Cursor> currentCursor = new SimpleObjectProperty<>(Cursor.DEFAULT);

    private EventObservables() {

    }

    private static class Singleton {
        public static final EventObservables INSTANCE = new EventObservables();
    }

    public static EventObservables getInstance() {
        return Singleton.INSTANCE;
    }
}
