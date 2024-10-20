package com.example.login;

import com.example.util.Cleaner;
import com.example.views.CustomButton;
import com.example.views.CustomPasswordField;
import com.example.views.FormView;
import com.example.views.InputTextField;
import com.example.views.InputView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginView extends FormView implements Cleaner {

    private final InputTextField emailField = new InputTextField();
    private final CustomPasswordField passwordField = new CustomPasswordField();

    private final InputView buttonContainer = new InputView();
    private final CustomButton loginButton = new CustomButton("Login");
    private final CustomButton signUpButton = new CustomButton("Sign up");

    private final InputView infoContainer = new InputView();
    private final Label infoLabel = new Label("");

    public LoginView() {
        getStyleClass().add("login-view");
        buttonContainer.getStyleClass().add("button-container");
        infoLabel.getStyleClass().add("info-label");
        this.setFillWidth(false);

        emailField.getPrefixLabel().setText("Email:");
        passwordField.getPrefixLabel().setText("Password:");
        emailField.getTextField().setPromptText("Enter Email");
        passwordField.setPromptText("Enter Password");

        buttonContainer.getChildren().addAll(
            loginButton,
            signUpButton
        );
        infoContainer.getChildren().add(infoLabel);
        addInputViews(
            emailField,
            passwordField,
            infoContainer,
            buttonContainer
        );
    }

    public TextField getEmailField() {
        return emailField.getTextField();
    }

    public CustomPasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getSignUpButton() {
        return signUpButton;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    @Override
    public void initialize() {
        passwordField.initialize();
    }

    @Override
    public void cleanUp() {
        emailField.getTextField().setText("");
        infoLabel.setText("");
        passwordField.cleanUp();
    }
}
