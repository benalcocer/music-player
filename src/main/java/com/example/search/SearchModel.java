package com.example.search;

import com.example.Main;
import com.example.models.Song;
import com.example.util.CustomThreadPoolExecutor;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class SearchModel {

    private final ObservableList<Song> songsList = FXCollections.observableArrayList();
    private final CustomThreadPoolExecutor executor = new CustomThreadPoolExecutor(1);
    private Task<Void> currentTask = null;
    private String previousSearch = null;

    public SearchModel() {

    }

    public final ObservableList<Song> getSongsList() {
        return songsList;
    }

    public void handleClose() {
        songsList.clear();
        previousSearch = null;
    }

    private Task<Void> createSearchTextTask(String searchText) {
        return new Task<>() {
            @Override
            protected Void call() {
                Main.getMainApplicationWindow().setLoading(true);
                try {
                    List<Song> songsFound = Main.getDatabase().searchSongs(searchText);
                    Platform.runLater(() -> {
                        Main.getMainApplicationWindow().setLoading(false);
                        previousSearch = searchText;
                        songsList.clear();
                        songsList.addAll(songsFound);
                    });
                } catch (Exception e) {
                    Logger.getGlobal().severe(e.getMessage());
                    Main.getMainApplicationWindow().setLoading(false);
                }
                return null;
            }
        };
    }

    private void cancelPreviousTask() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
    }

    public void searchImmediately(String searchText) {
        search(searchText, false);
    }

    public void handleTextChange(String oldText, String newText) {
        search(newText, true);
    }

    private void search(String searchText, boolean delayed) {
        cancelPreviousTask();
        if (searchText == null || searchText.isBlank() || Objects.equals(previousSearch, searchText)) {
            return;
        }
        currentTask = createSearchTextTask(searchText);
        if (delayed) {
            executor.schedule(currentTask, 1, TimeUnit.SECONDS);
        } else {
            executor.execute(currentTask);
        }
    }
}
