package com.example.windows;

import com.example.Main;
import com.example.events.CustomEventTypes;
import com.example.events.EventObservables;
import com.example.settings.SettingsController;
import com.example.util.Cleaner;
import com.example.fxutil.CustomStyleSheet;
import com.example.fxutil.StyleSheetManager;
import com.example.library.LibraryController;
import com.example.login.LoginController;
import com.example.mediaplayer.MediaPlayerController;
import com.example.playlist.PlaylistController;
import com.example.fxutil.CustomWindow;
import com.example.search.SearchController;
import com.example.util.Constants;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.reactfx.EventStreams;

public class MainApplicationWindow extends CustomWindow implements Cleaner {

    // Views
    private final BorderPane borderPane;

    // Controllers
    private final LibraryController libraryController;
    private final PlaylistController playlistController;
    private final MediaPlayerController mediaPlayerController;
    private final SearchController searchController;
    private final LoginController loginController;
    private final SettingsController settingsController;

    public MainApplicationWindow(Stage stage) {
        super(stage);
        getStage().setMinWidth(Constants.getInstance().MIN_WINDOW_WIDTH);
        getStage().setMinHeight(Constants.getInstance().MIN_WINDOW_HEIGHT);

        // Initialize controllers
        libraryController = new LibraryController();
        playlistController = new PlaylistController();
        mediaPlayerController = new MediaPlayerController();
        searchController = new SearchController();
        loginController = new LoginController();
        settingsController = new SettingsController();

        borderPane = new BorderPane();
        initialize();
    }

    @Override
    public void initialize() {
        borderPane.setCenter(playlistController.getPlaylistView());
        borderPane.setLeft(libraryController.getLibraryView());
        borderPane.setBottom(mediaPlayerController.getControlsView());
        getRoot().getChildren().addAll(
            loginController.getView()
        );
        VBox.setVgrow(loginController.getView(), Priority.ALWAYS);
        VBox.setVgrow(borderPane, Priority.ALWAYS);

        subscriptions.addSubscription(EventStreams.changesOf(EventObservables.getInstance().centerViewProperty).subscribe(centerViewChange -> {
            if (centerViewChange.getNewValue() == null) {
                return;
            }
            switch (centerViewChange.getNewValue()) {
                case PLAYLISTS -> {
                    if (borderPane.getCenter() != playlistController.getPlaylistView()) {
                        borderPane.setCenter(playlistController.getPlaylistView());
                    }
                }
                case SEARCH -> {
                    if (borderPane.getCenter() != searchController.getSearchView()) {
                        borderPane.setCenter(searchController.getSearchView());
                    }
                }
                case SETTINGS -> {
                    if (borderPane.getCenter() != settingsController.getSettingsView()) {
                        borderPane.setCenter(settingsController.getSettingsView());
                    }
                }
            }
        }));
        subscriptions.addSubscription(Main.globalEventManager.eventsOf(CustomEventTypes.LOGIN_SUCCESS).subscribe(event -> {
            if (getRoot().getChildren().contains(loginController.getView())) {
                // TODO: Add a loading view?
                Main.getData().refreshData(Main.getDatabase());
                mediaPlayerController.initialize();
                libraryController.initialize();
                playlistController.initialize();
                settingsController.initialize();
                getRoot().getChildren().clear();
                getRoot().getChildren().add(borderPane);
                loginController.cleanUp();
            }
        }));
        subscriptions.addSubscription(EventStreams.changesOf(EventObservables.getInstance().currentCursor).subscribe(cursorChange -> {
            if (!isLoading()) {
                getScene().setCursor(cursorChange.getNewValue());
            }
        }));
        mediaPlayerController.initialize();
        loginController.initialize();
    }

    @Override
    public void cleanUp() {
        loginController.cleanUp();

        libraryController.cleanUp();
        playlistController.cleanUp();
        mediaPlayerController.cleanUp();
        searchController.cleanUp();

        subscriptions.unsubscribeFromSubs();
    }
}
