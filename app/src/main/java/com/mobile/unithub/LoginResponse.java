package com.mobile.unithub;

public class LoginResponse {
    private String accessToken; // Renomeado de 'token' para 'accessToken'
    private int expiresIn;
    private String role;

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getRole() {
        return role;
    }

    public boolean isSuccess() {
        return accessToken != null && !accessToken.isEmpty();
    }
}