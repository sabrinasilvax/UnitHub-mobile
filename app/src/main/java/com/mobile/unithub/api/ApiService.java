package com.mobile.unithub.api;

import com.mobile.unithub.api.requests.CadastroRequest;
import com.mobile.unithub.api.requests.LoginRequest;
import com.mobile.unithub.api.responses.FeedItemResponse;
import com.mobile.unithub.api.responses.FeedResponse;
import com.mobile.unithub.api.responses.ListarCursosResponse;
import com.mobile.unithub.api.responses.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @GET("events/feed")
    Call<FeedResponse> getFeed(@Query("pages") int page);

    @GET("events/{id}")
    Call<FeedItemResponse> getEventDetails(@Path("id") String eventId);

    @POST("events/subscribe/{id}")
    Call<Void> subscribeToEvent(@Path("id") String eventId);

    @POST("events/unsubscribe/{id}")
    Call<Void> unsubscribeFromEvent(@Path("id") String eventId);
}