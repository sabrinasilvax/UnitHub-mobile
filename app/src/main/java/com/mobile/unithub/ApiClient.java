package com.mobile.unithub;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://unithub-3a018275aeb8.herokuapp.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configuração do interceptor de logs
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Configuração do cliente HTTP
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        okhttp3.Request original = chain.request();

                        // Log da requisição
                        okhttp3.Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();
                        okhttp3.Response response = chain.proceed(request);

                        // Log da resposta
                        System.out.println("Requisição: " + request.url());
                        System.out.println("Headers: " + request.headers());
                        System.out.println("Resposta: " + response.code());

                        return response;
                    });

            // Configuração do Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}