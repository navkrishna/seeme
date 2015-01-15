package com.intelligrape.seeme.model;

/**
 * Created by rajendra on 17/10/14.
 */
public class Login extends Model {
    boolean isAccountVerified;
    private String accessToken, email, userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAccountVerified() {
        return isAccountVerified;
    }

    public void setAccountVerified(boolean isAccountVerified) {
        this.isAccountVerified = isAccountVerified;
    }
}
