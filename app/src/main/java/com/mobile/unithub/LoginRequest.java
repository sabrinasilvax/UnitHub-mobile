package com.mobile.unithub;

public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters (opcional, mas recomendado)
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}