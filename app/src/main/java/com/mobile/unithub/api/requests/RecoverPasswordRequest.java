package com.mobile.unithub.api.requests;

public class RecoverPasswordRequest {
    private String email;

    public RecoverPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}