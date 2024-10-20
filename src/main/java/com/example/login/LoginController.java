package com.example.login;

import com.example.Main;
import com.example.events.CustomEvent;
import com.example.events.CustomEventTypes;
import com.example.fxutil.AbstractController;
import com.example.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.reactfx.EventStreams;

public class LoginController extends AbstractController {
    /**
     * The base view that is returned that contains all the views and switches between them.
     */
    private final StackPane baseView = new StackPane();

    /**
     * The LoginModel that handles the logic of the LoginView;
     */
    private final LoginModel loginModel = new LoginModel(Main.getData(), Main.getDatabase());

    /**
     * The LoginView that shows the login page.
     */
    private final LoginView loginView = new LoginView();

    /**
     * The VerificationView that shows the verification code page.
     */
    private final VerificationView verificationView = new VerificationView();

    public LoginController() {
        baseView.getChildren().addAll(
            verificationView,
            loginView
        );
    }

    @Override
    public void initialize() {
        loginView.initialize();
        verificationView.initialize();
        setLoginViewVisible();

        loginView.managedProperty().bind(loginView.visibleProperty());
        verificationView.managedProperty().bind(verificationView.visibleProperty());

        subscriptions.addSubscription(EventStreams.eventsOf(baseView, MouseEvent.MOUSE_PRESSED).subscribe(mouseEvent -> {
            if (mouseEvent.isBackButtonDown()) {
                if (verificationView.isVisible()) {
                    Main.getDatabase().setToken("");
                    setLoginViewVisible();
                }
            }
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(loginView.getLoginButton(), ActionEvent.ACTION).subscribe(event -> {
            handleLoginButton();
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(loginView.getSignUpButton(), ActionEvent.ACTION).subscribe(event -> {
            handleSignUpButton();
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(loginView.getPasswordField().getRevealButton(), ActionEvent.ACTION).subscribe(event -> {
            loginView.getPasswordField().toggleHidden();
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(verificationView.getOkButton(), ActionEvent.ACTION).subscribe(event -> {
            handleVerifyCodeButton();
        }));
        subscriptions.addSubscription(EventStreams.eventsOf(verificationView.getResendButton(), ActionEvent.ACTION).subscribe(event -> {
            handleResendButton();
        }));
    }

    @Override
    public void cleanUp() {
        loginView.cleanUp();
        verificationView.cleanUp();

        loginView.managedProperty().unbind();
        verificationView.managedProperty().unbind();

        subscriptions.unsubscribeFromSubs();
    }

    private void handleLoginButton() {
        if (loginInputsInvalid()) {
            return;
        }
        String email = loginView.getEmailField().getText();
        String password = loginView.getPasswordField().getText();

        if (!loginModel.isUserEnabled(email)) {
            setVerificationViewVisible();
            return;
        }
        if (!loginModel.login(email, password)) {
            setInfoText("Email or password was incorrect");
            return;
        }
        Main.globalEventManager.fireEvent(new CustomEvent<>(CustomEventTypes.LOGIN_SUCCESS, null));
    }

    private void handleSignUpButton() {
        if (loginInputsInvalid()) {
            return;
        }
        String email = loginView.getEmailField().getText();
        String password = loginView.getPasswordField().getText();
        if (loginModel.doesUserExist(email)) {
            setInfoText("User already exists, try logging in");
            return;
        }
        if (!loginModel.signup(email, password)) {
            setInfoText("Failed to sign up");
            return;
        }
        if (!loginModel.isUserEnabled(email)) {
            setVerificationViewVisible();
            return;
        }
        if (!loginModel.login(email, password)) {
            setInfoText("Failed to login");
            return;
        }
        Main.globalEventManager.fireEvent(new CustomEvent<>(CustomEventTypes.LOGIN_SUCCESS, null));
    }

    private void handleVerifyCodeButton() {
        String email = loginView.getEmailField().getText();
        String code = verificationView.getCodeField().getText().trim();
        if (!StringUtils.isValidVerificationCode(code)) {
            setInfoText("Code was invalid");
            return;
        }
        if (!loginModel.verifyUser(email, code)) {
            setInfoText("Code was incorrect");
            return;
        }
        setInfoText("Code was correct!");
        Main.getMainApplicationWindow().awaitRoutine(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            setLoginViewVisible();
        });
    }

    private void handleResendButton() {
        String email = loginView.getEmailField().getText();
        if (!loginModel.resendVerificationCode(email)) {
            setInfoText("Failed to resend verification code");
            return;
        }
        setInfoText("Successfully resent verification code...");
    }

    private void setLoginViewVisible() {
        loginView.setVisible(true);
        verificationView.setVisible(false);
        verificationView.getInfoLabel().setText("");
    }

    private void setVerificationViewVisible() {
        loginView.setVisible(false);
        verificationView.setVisible(true);
        loginView.getInfoLabel().setText("");
    }

    private boolean loginInputsInvalid() {
        if (!StringUtils.isValidEmail(loginView.getEmailField().getText())) {
            setInfoText("Email address is invalid");
            return true;
        }
        if (!StringUtils.isValidPassword(loginView.getPasswordField().getText())) {
            setInfoText("Password is invalid, expected length between 3 and 80 characters");
            return true;
        }

        return false;
    }

    public final Node getView() {
        return baseView;
    }

    private void setInfoText(String text) {
        if (loginView.isVisible()) {
            loginView.getInfoLabel().setText(text);
        } else {
            verificationView.getInfoLabel().setText(text);
        }
    }
}
