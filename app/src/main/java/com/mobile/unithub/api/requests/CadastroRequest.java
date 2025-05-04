package com.mobile.unithub.api.requests;
public class CadastroRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String telephone;
    private String name;
    private int courseId;

    public CadastroRequest(String email, String password, String confirmPassword, String telephone, String name, int courseId) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.telephone = telephone;
        this.name = name;
        this.courseId = courseId;
    }
}
