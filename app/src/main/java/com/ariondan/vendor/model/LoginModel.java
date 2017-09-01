package com.ariondan.vendor.model;

/**
 * Created by Akitektuo on 01.09.2017.
 */

public class LoginModel {

    private String email;
    private String password;
    private boolean rememberMe;

    public LoginModel(String email, String password) {
        setEmail(email);
        setPassword(password);
        setRememberMe(false);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
