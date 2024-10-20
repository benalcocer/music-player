package com.example;

import com.example.events.CustomEventManager;
import com.example.fxutil.CustomStyleSheet;
import com.example.fxutil.StyleSheetManager;
import com.example.models.Data;
import com.example.util.Database;
import com.example.util.Constants;
import com.example.util.CustomThreadPoolExecutor;
import com.example.windows.MainApplicationWindow;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    private static MainApplicationWindow mainApplicationWindow;

    public static final CustomEventManager globalEventManager = new CustomEventManager();

    public static final ScheduledThreadPoolExecutor executor = new CustomThreadPoolExecutor(1);

    private static Database database;

    private static Data data;

    public static Database getDatabase() {
        return database;
    }

    public static Data getData() {
        return data;
    }

    private static void handleUncaughtException(Thread thread, Throwable throwable) {
        Logger.getGlobal().severe("Uncaught exception in " + thread.getName() + " " + throwable.getMessage());
    }

    public static MainApplicationWindow getMainApplicationWindow() {
        return mainApplicationWindow;
    }

    public static void main(String[] args) {
        if (Constants.getInstance().DATABASE_HOST.isBlank()) {
            Logger.getGlobal().severe("Could not find DATABASE_HOST environment property.");
            System.exit(1);
        } else {
            Thread.setDefaultUncaughtExceptionHandler(Main::handleUncaughtException);
            launch();
        }
    }

    @Override
    public void start(Stage stage) {
        executor.execute(() -> {
            try {
                database = new Database(Constants.getInstance().DATABASE_HOST);
                data = new Data();
            } catch (Exception e) {
                Logger.getGlobal().severe(e.getMessage());
                System.exit(1);
                return;
            }
            Platform.runLater(() -> {
                StyleSheetManager.getInstance().setTheme(CustomStyleSheet.DARK_MODE);
                mainApplicationWindow = new MainApplicationWindow(stage);
                mainApplicationWindow.show();
            });
        });
    }

    @Override
    public void stop(){
        mainApplicationWindow.close();
        mainApplicationWindow.cleanUp();
        CustomThreadPoolExecutor.shutAllDown();
    }
}
