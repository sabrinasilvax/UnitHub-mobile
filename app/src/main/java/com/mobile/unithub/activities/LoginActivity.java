package com.mobile.unithub.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.databinding.ActivityLoginBinding;
import com.mobile.unithub.api.requests.LoginRequest;
import com.mobile.unithub.api.responses.LoginResponse;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = "LoginAuth"; // Tag única para filtrar no Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "Activity criada");
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnEntrar.setOnClickListener(v -> {
            String email = binding.email.getText().toString().trim();
            String senha = binding.senha.getText().toString().trim();
            Log.d(TAG, "Botão entrar clicado - Email: " + email + " | Senha: " + (senha.isEmpty() ? "vazia" : "preenchida"));

            if (!validarCampos(email, senha)) return;
            fazerLogin(email, senha);
        });

        binding.btnCadastre.setOnClickListener(v -> {
            Log.d(TAG, "Redirecionando para CadastroActivity");
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        });

        binding.loginLink.setOnClickListener(v -> {
            Log.d(TAG, "Link 'Esqueceu a senha' clicado");
            Toast.makeText(this, "Redirecionando para recuperação de senha", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validarCampos(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            Log.w(TAG, "Validação: Campos vazios");
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.w(TAG, "Validação: Email inválido - " + email);
            binding.email.setError("Email inválido");
            return false;
        }
        if (senha.length() < 6) {
            Log.w(TAG, "Validação: Senha curta - " + senha.length() + " caracteres");
            binding.senha.setError("Mínimo 6 caracteres");
            return false;
        }
        Log.d(TAG, "Validação: Campos OK");
        return true;
    }

    private void fazerLogin(String email, String senha) {
        Log.d(TAG, "Iniciando processo de login...");
        binding.progressBar.setVisibility(View.VISIBLE);
        disableButtons();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, senha);

        // Log do corpo da requisição
        Log.d(TAG, "Corpo da requisição enviado: " + new Gson().toJson(loginRequest));

        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        Log.d(TAG, "Requisição criada: " + call.request().url());
        Log.d(TAG, "Headers da requisição: " + call.request().headers());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                enableButtons();
                Log.d(TAG, "Resposta recebida. Código: " + response.code());

                if (response.isSuccessful()) {
                    Log.d(TAG, "Resposta bem-sucedida");
                    handleSuccessResponse(response.body());
                } else {
                    Log.w(TAG, "Resposta com erro");
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                enableButtons();
                Log.e(TAG, "Falha na requisição: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleSuccessResponse(LoginResponse response) {
        try {
            if (response != null && response.getAccessToken() != null) {
                Log.d(TAG, "Token recebido: " + response.getAccessToken());
                Log.d(TAG, "Dados recebidos - Expira em: " + response.getExpiresIn() + " | Role: " + response.getRole());
    
                saveAuthData(response);
                Log.d(TAG, "Dados salvos no SharedPreferences");
    
                Log.d(TAG, "Iniciando MainActivity...");
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                intent.putExtra("ACCESS_TOKEN", response.getAccessToken()); // Passa o token para a MainActivity
                startActivity(intent);
                finish();
                Log.d(TAG, "LoginActivity finalizada");
            } else {
                Log.w(TAG, "Resposta vazia ou sem token");
                Toast.makeText(this, "Resposta inválida da API", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro no handleSuccessResponse", e);
            Toast.makeText(this, "Erro ao processar login", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleErrorResponse(Response<LoginResponse> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Corpo de erro vazio";
            Log.e(TAG, "Corpo do erro (" + response.code() + "): " + errorBody);

            try {
                JsonObject jsonObject = JsonParser.parseString(errorBody).getAsJsonObject();
                String errorMsg = jsonObject.has("message") ?
                        jsonObject.get("message").getAsString() : "Erro desconhecido";
                Log.w(TAG, "Mensagem de erro extraída: " + errorMsg);
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.w(TAG, "Erro não está em formato JSON");
                Toast.makeText(this,
                        "Erro " + response.code() + ": " + response.message(),
                        Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Erro ao ler corpo de erro", e);
            Toast.makeText(this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAuthData(LoginResponse response) {
        try {
            SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("ACCESS_TOKEN", response.getAccessToken())
                    .putInt("EXPIRES_IN", response.getExpiresIn())
                    .putString("ROLE", response.getRole())
                    .apply();
            Log.d(TAG, "Dados de autenticação salvos com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar dados de autenticação", e);
        }
    }

    private void disableButtons() {
        binding.btnEntrar.setEnabled(false);
        binding.btnCadastre.setEnabled(false);
        Log.d(TAG, "Botões desabilitados durante requisição");
    }

    private void enableButtons() {
        binding.btnEntrar.setEnabled(true);
        binding.btnCadastre.setEnabled(true);
        Log.d(TAG, "Botões reabilitados");
    }
}