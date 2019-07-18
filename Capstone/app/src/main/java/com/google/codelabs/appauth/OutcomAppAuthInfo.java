package com.google.codelabs.appauth;

import com.example.capstone.LoginDTO;

public class OutcomAppAuthInfo {

    private static OutcomAppAuthInfo instance = new OutcomAppAuthInfo();

    private boolean loginGoogle = false;
    private String token;
    private String message;
    private int userId;

    private OutcomAppAuthInfo() { }

    public static OutcomAppAuthInfo getInstance() {
        return instance;
    }

    public void setLoginInfo(LoginDTO dto, String message) {
        if (dto == null) {
            this.userId = 0;
            this.token = null;
        } else {
            this.userId = dto.getId();
            this.token = dto.getToken();
        }
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoginGoogle() { return loginGoogle; }

    public void setLoginGoogle(boolean loginGoogle) { this.loginGoogle = loginGoogle; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("userId: " + userId + "\n");
        builder.append("token: " + token + "\n");
        builder.append("message: " + message + "\n");
        builder.append("loginGoogle: " + loginGoogle + "\n");
        return builder.toString();
    }

}
