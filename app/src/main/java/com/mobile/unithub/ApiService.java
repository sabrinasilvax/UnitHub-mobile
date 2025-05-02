package com.mobile.unithub;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}