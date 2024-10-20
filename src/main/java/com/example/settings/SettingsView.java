package com.example.settings;

import com.example.fxutil.CustomStyleSheet;
import com.example.views.FormView;
import com.example.views.InputView;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SettingsView extends FormView {
    private final InputView themeInput = new InputView();

    private final ComboBox<CustomStyleSheet> styleSheetComboBox = new ComboBox<>();

    public SettingsView() {
        getStyleClass().add("settings-view");
        this.setFillWidth(false);
        themeInput.getPrefixLabel().setText("Current Theme:");
        themeInput.getChildren().addAll(
            styleSheetComboBox
        );
        HBox.setHgrow(styleSheetComboBox, Priority.ALWAYS);
        addInputViews(
            themeInput
        );
    }

    public ComboBox<CustomStyleSheet> getStyleSheetComboBox() {
        return styleSheetComboBox;
    }
}
