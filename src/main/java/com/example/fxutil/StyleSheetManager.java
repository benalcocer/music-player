package com.example.fxutil;

import com.example.Main;
import com.example.events.CustomEvent;
import com.example.events.CustomEventTypes;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.stage.Window;

/**
 * Class that controls setting/applying the external stylesheets.
 */
public class StyleSheetManager {

    /**
     * The Base CSS StyleSheet.
     */
    private static final CustomStyleSheet baseCss = CustomStyleSheet.CSS_BASE;

    /**
     * The current theme.
     */
    private static CustomStyleSheet currentTheme;

    private StyleSheetManager() {
        Window.getWindows().addListener((ListChangeListener<? super Window>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Window window : change.getAddedSubList()) {
                        Scene scene = window.getScene();
                        if (scene != null) {
                            scene.getStylesheets().clear();
                            scene.getStylesheets().addAll(
                                baseCss.getStyleSheetLocation(),
                                currentTheme.getStyleSheetLocation()
                            );
                        }
                    }
                }
            }
        });
    }

    /**
     * Set the theme for the Application.
     *
     * @param newTheme The new theme to set to the application.
     */
    public void setTheme(CustomStyleSheet newTheme) {
        if (newTheme != null && currentTheme != newTheme) {
            if (!newTheme.exists()) {
                Logger.getGlobal().warning("Failed to set new theme since the theme file does not exist.");
                return;
            }
            currentTheme = newTheme;
            Window.getWindows().forEach(window -> {
                Scene scene = window.getScene();
                if (scene != null) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().addAll(
                        baseCss.getStyleSheetLocation(),
                        currentTheme.getStyleSheetLocation()
                    );
                }
            });
            Main.globalEventManager.fireEvent(new CustomEvent<>(CustomEventTypes.STYLESHEET_CHANGE, null));
        }
    }

    public CustomStyleSheet getTheme() {
        return currentTheme;
    }

    private static class Singleton {
        public static StyleSheetManager INSTANCE = new StyleSheetManager();
    }

    public static StyleSheetManager getInstance() {
        return Singleton.INSTANCE;
    }
}
