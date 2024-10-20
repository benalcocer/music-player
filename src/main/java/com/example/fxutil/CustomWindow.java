package com.example.fxutil;

import com.example.util.CustomThreadPoolExecutor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomWindow {
    private final Stage stage;
    private final VBox root = new VBox();
    private final Scene scene = new Scene(root);

    protected final Subscriptions subscriptions = new Subscriptions();

    private final ReadOnlyBooleanWrapper isLoading = new ReadOnlyBooleanWrapper(false);

    private static final ScheduledThreadPoolExecutor awaitExecutor = new CustomThreadPoolExecutor(1);

    private static final ScheduledThreadPoolExecutor cursorExecutor = new CustomThreadPoolExecutor(1);

    private final ChangeListener<Boolean> isLoadingCL = (observable, oldValue, newValue) -> {
        if (newValue) {
            root.setMouseTransparent(true);
            cursorExecutor.schedule(() -> {
                Platform.runLater(() -> {
                    if (isLoading.getValue()) {
                        scene.setCursor(Cursor.WAIT);
                    }
                });
            }, 5, TimeUnit.SECONDS);
        } else {
            root.setMouseTransparent(false);
            scene.setCursor(Cursor.DEFAULT);
        }
    };

    public CustomWindow() {
        this(new Stage());
    }

    public CustomWindow(Stage stage) {
        super();
        isLoading.addListener(isLoadingCL);

        this.stage = stage;
        root.getStyleClass().add("root-view");
        root.setMinSize(1280, 720);
        root.setFillWidth(true);
        stage.setScene(scene);
    }

    public void show() {
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    public void close() {
        if (stage.isShowing()) {
            stage.close();
        }
    }

    public final Stage getStage() {
        return stage;
    }

    public final Scene getScene() {
        return scene;
    }

    public final VBox getRoot() {
        return root;
    }

    public void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }

    public boolean isLoading() {
        return isLoading.getValue();
    }

    /**
     * Set the window in a loading state and run the routine. At the end of the routine update the
     * window to remove the loading state.
     */
    public void awaitRoutine(Runnable routine) {
        setLoading(true);
        awaitExecutor.execute(() -> {
            try {
                routine.run();
            } catch (Exception e) {
                Logger.getGlobal().severe(e.getMessage());
            } finally {
                Platform.runLater(() -> setLoading(false));
            }
        });
    }
}
