package com.mobile.unithub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.AuthInterceptor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ApiClientTest {

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void deveConfigurarCorretamenteORetrofit() {
        Retrofit retrofit = ApiClient.getClient(context);
        Assert.assertNotNull(retrofit);
        Assert.assertEquals("https://unithub-3a018275aeb8.herokuapp.com/", retrofit.baseUrl().toString());
    }

    @Test
    public void deveAdicionarAuthInterceptor() throws Exception {
        Retrofit retrofit = ApiClient.getClient(context);
        OkHttpClient client = (OkHttpClient) getField(retrofit, "callFactory");
        List<Interceptor> interceptors = client.interceptors();
        boolean hasAuthInterceptor = false;
        for (Interceptor interceptor : interceptors) {
            if (interceptor instanceof AuthInterceptor) {
                hasAuthInterceptor = true;
                break;
            }
        }
        Assert.assertTrue(hasAuthInterceptor);
    }

    @Test
    public void deveAdicionarTokenJWTNoHeader() throws IOException {
        // Salva um token de teste nas SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("ACCESS_TOKEN", "token_teste").apply();

        AuthInterceptor interceptor = new AuthInterceptor(context);
        Request request = new Request.Builder()
                .url("https://unithub-3a018275aeb8.herokuapp.com/test")
                .build();

        Interceptor.Chain chain = new TestChain(request);
        interceptor.intercept(chain);

        Request interceptedRequest = ((TestChain) chain).getInterceptedRequest();
        Assert.assertEquals("Bearer token_teste", interceptedRequest.header("Authorization"));
    }

    @Test
    public void deveLimparDadosERedirecionarParaLoginEmCasoDe401() throws IOException {
        // Salva um token de teste nas SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("ACCESS_TOKEN", "token_teste").apply();

        AuthInterceptor interceptor = new AuthInterceptor(context);
        Request request = new Request.Builder()
                .url("https://unithub-3a018275aeb8.herokuapp.com/test")
                .build();

        Interceptor.Chain chain = new TestChain(request, 401);
        interceptor.intercept(chain);

        // O token deve ser removido das SharedPreferences
        String token = prefs.getString("ACCESS_TOKEN", null);
        Assert.assertNull(token);
        // Não é possível testar o startActivity diretamente aqui, mas pode-se verificar o token limpo
    }

    // Utilitário para acessar campos privados via reflexão
    private Object getField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    // Mock do Interceptor.Chain para testar interceptação
    private static class TestChain implements Interceptor.Chain {
        private final Request request;
        private Request interceptedRequest;
        private final int responseCode;

        public TestChain(Request request) {
            this(request, 200);
        }

        public TestChain(Request request, int responseCode) {
            this.request = request;
            this.responseCode = responseCode;
        }

        @Override
        public Request request() {
            return request;
        }

        @Override
        public Response proceed(Request request) throws IOException {
            this.interceptedRequest = request;
            return new Response.Builder()
                    .request(request)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(responseCode)
                    .message("Test")
                    .body(okhttp3.ResponseBody.create(null, ""))
                    .sentRequestAtMillis(0)
                    .receivedResponseAtMillis(0)
                    .build();
        }

        public Request getInterceptedRequest() {
            return interceptedRequest;
        }

        // Métodos não utilizados
        @Override public Connection connection() { return null; }
        @Override public okhttp3.Call call() { return null; }
        @Override public int connectTimeoutMillis() { return 0; }
        @Override public Interceptor.Chain withConnectTimeout(int i, java.util.concurrent.TimeUnit timeUnit) { return this; }
        @Override public int readTimeoutMillis() { return 0; }
        @Override public Interceptor.Chain withReadTimeout(int i, java.util.concurrent.TimeUnit timeUnit) { return this; }
        @Override public int writeTimeoutMillis() { return 0; }
        @Override public Interceptor.Chain withWriteTimeout(int i, java.util.concurrent.TimeUnit timeUnit) { return this; }
    }
}