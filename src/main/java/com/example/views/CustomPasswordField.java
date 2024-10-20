package com.example.views;

import com.example.fxutil.Icon;
import com.example.util.Cleaner;
import com.example.fxutil.Subscriptions;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.reactfx.EventStreams;

public class CustomPasswordField extends InputView implements Cleaner {

    private final PasswordField passwordField = new PasswordField();
    private final TextField revealedPasswordField = new TextField();
    private final IconButton revealButton = new IconButton(null);
    private final SimpleBooleanProperty isHidden = new SimpleBooleanProperty(true);

    private final Subscriptions subscriptions = new Subscriptions();

    public CustomPasswordField() {
        super();
        getStyleClass().add("custom-password-field");
        getChildren().addAll(
            passwordField,
            revealedPasswordField,
            revealButton
        );
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        HBox.setHgrow(revealedPasswordField, Priority.ALWAYS);
    }

    public String getText() {
        return passwordField.getText();
    }

    public void setPromptText(String text) {
        passwordField.setPromptText(text);
        revealedPasswordField.setPromptText(text);
    }

    public void toggleHidden() {
        isHidden.setValue(!isHidden.getValue());
    }

    public IconButton getRevealButton() {
        return revealButton;
    }

    @Override
    public void initialize() {
        passwordField.managedProperty().bind(passwordField.visibleProperty());
        revealedPasswordField.managedProperty().bind(revealedPasswordField.visibleProperty());

        revealButton.initialize();
        subscriptions.addSubscription(EventStreams.valuesOf(isHidden).subscribe(hidden -> {
            if (hidden) {
                revealButton.getIconView().setIcon(Icon.EYE_CLOSED);
            } else {
                revealButton.getIconView().setIcon(Icon.EYE_OPEN);
            }
            passwordField.setVisible(hidden);
            revealedPasswordField.setVisible(!hidden);
        }));
        subscriptions.addSubscription(EventStreams.valuesOf(passwordField.textProperty()).subscribe(
            revealedPasswordField::setText
        ));
        subscriptions.addSubscription(EventStreams.valuesOf(revealedPasswordField.textProperty()).subscribe(
            passwordField::setText
        ));
    }

    @Override
    public void cleanUp() {
        passwordField.visibleProperty().unbind();
        revealedPasswordField.visibleProperty().unbind();
        passwordField.managedProperty().unbind();
        revealedPasswordField.managedProperty().unbind();

        subscriptions.unsubscribeFromSubs();
        revealButton.cleanUp();
        passwordField.setText("");
        revealedPasswordField.setText("");
    }
}
