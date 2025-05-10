package com.mobile.unithub.api;

import com.mobile.unithub.api.requests.CadastroRequest;
import com.mobile.unithub.api.requests.LoginRequest;
import com.mobile.unithub.api.requests.RecoverPasswordRequest;
import com.mobile.unithub.api.requests.UpdatePasswordRequest;
import com.mobile.unithub.api.responses.FeedItemResponse;
import com.mobile.unithub.api.responses.FeedResponse;
import com.mobile.unithub.api.responses.ListarCursosResponse;
import com.mobile.unithub.api.responses.LoginResponse;
import com.mobile.unithub.api.responses.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("register")
    Call<Void> registerUser(@Body CadastroRequest request);

    @GET("courses")
    Call<List<ListarCursosResponse>> getCourses();

    @GET("/events/feed-by-course")
    Call<FeedResponse> getFeed(@Query("pages") int page);

    @GET("events/{id}")
    Call<FeedItemResponse> getEventDetails(@Path("id") String eventId);

    @POST("events/subscribe/{id}")
    Call<Void> subscribeToEvent(@Path("id") String eventId);

    @POST("events/unsubscribe/{id}")
    Call<Void> unsubscribeFromEvent(@Path("id") String eventId);

    @GET("events/subscribed")
    Call<List<FeedItemResponse>> getEventDetails();
    
    @POST("recover-password")
    Call<Void> emailRecoverPassword(@Body RecoverPasswordRequest request);

    @GET("users/profile")
    Call<UserProfileResponse> getUserProfile();

    @PATCH("users/profile")
    Call<Void> updateUserProfile(@Body UpdatePasswordRequest request);
}