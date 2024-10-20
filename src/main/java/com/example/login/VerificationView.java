package com.example.login;

import com.example.fxutil.View;
import com.example.util.Cleaner;
import com.example.views.CustomButton;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class VerificationView extends View implements Cleaner {

    private final Label prompt = new Label("Please enter the verification code sent to your email before logging in");
    private final TextField codeField = new TextField();

    private final HBox buttonContainer = new HBox();
    private final CustomButton resendButton = new CustomButton("Resend Code");
    private final CustomButton okButton = new CustomButton("OK");

    private final Label infoLabel = new Label("");

    public VerificationView() {
        this.getStyleClass().add("login-view");
        buttonContainer.getStyleClass().add("button-container");
        this.setFillWidth(false);

        buttonContainer.getChildren().addAll(
            resendButton, okButton
        );
        getChildren().addAll(
            prompt,
            codeField,
            infoLabel,
            buttonContainer
        );
    }

    public TextField getCodeField() {
        return codeField;
    }

    public Button getResendButton() {
        return resendButton;
    }

    public Button getOkButton() {
        return okButton;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void cleanUp() {
        codeField.setText("");
        infoLabel.setText("");
    }
}
