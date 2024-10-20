package com.example.login;

import com.example.models.Data;
import com.example.models.User;
import com.example.util.Database;
import com.example.util.JSONUtils;
import kong.unirest.json.JSONObject;

public class LoginModel {

    private final Data data;
    private final Database database;

    public LoginModel(Data data, Database database) {
        this.data = data;
        this.database = database;
    }

    /**
     * Attempt to log in using the given email and password.
     *
     * @return The User ID of the user that was successfully logged in, otherwise null.
     */
    public boolean login(String email, String password) {
        JSONObject jsonObject = database.login(email, password);
        if (jsonObject != null) {
            try {
                String token = jsonObject.getString("token");
                String userResponseJson = jsonObject.get("userResponse").toString();
                User user = JSONUtils.convertToObject(User.class, userResponseJson);
                database.setToken(token);
                data.setUser(user);
                return true;
            } catch (Exception e) {
                // ignore
            }
        }
        return false;
    }

    public boolean doesUserExist(String email) {
        return database.doesUserExist(email);
    }

    public boolean signup(String email, String password) {
        return database.userSignup(email, password);
    }

    public boolean isUserEnabled(String email) {
        return database.isUserEnabled(email);
    }

    public boolean verifyUser(String email, String verificationCode) {
        return database.verifyUser(email, verificationCode);
    }

    public boolean resendVerificationCode(String email) {
        return database.resendCode(email);
    }
}
