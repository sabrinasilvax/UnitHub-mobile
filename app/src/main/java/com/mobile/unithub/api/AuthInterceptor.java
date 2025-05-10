package com.mobile.unithub.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.mobile.unithub.activities.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("ACCESS_TOKEN", null);

        // Adicionar o token JWT no cabeçalho Authorization, se disponível
        Request.Builder requestBuilder = chain.request().newBuilder();
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Response response = chain.proceed(requestBuilder.build());

        // Verificar se a resposta é 401 Unauthorized
        if (response.code() == 401) {
            // Remover o token antigo
            prefs.edit().clear().apply();

            // Redirecionar para a LoginActivity
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}