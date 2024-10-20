package com.example.settings;

import com.example.fxutil.AbstractController;
import com.example.fxutil.CustomStyleSheet;
import com.example.fxutil.StyleSheetManager;
import javafx.collections.FXCollections;

public class SettingsController extends AbstractController {

    private final SettingsView settingsView = new SettingsView();

    public SettingsController() {

    }

    @Override
    public void initialize() {
        settingsView.getStyleSheetComboBox().setItems(FXCollections.observableArrayList(CustomStyleSheet.getThemes()));
        settingsView.getStyleSheetComboBox().setValue(StyleSheetManager.getInstance().getTheme());
        settingsView.getStyleSheetComboBox().setOnHidden(event -> {
            StyleSheetManager.getInstance().setTheme(settingsView.getStyleSheetComboBox().getValue());
        });
    }

    @Override
    public void cleanUp() {
        subscriptions.unsubscribeFromSubs();
        settingsView.getStyleSheetComboBox().getItems().clear();
    }

    public SettingsView getSettingsView() {
        return settingsView;
    }
}
