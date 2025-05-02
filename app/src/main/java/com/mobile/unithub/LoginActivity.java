package com.mobile.unithub;

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
import com.mobile.unithub.databinding.ActivityLoginBinding;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnEntrar.setOnClickListener(v -> {
            String email = binding.email.getText().toString().trim();
            String senha = binding.senha.getText().toString().trim();

            if (!validarCampos(email, senha)) return;
            fazerLogin(email, senha);
        });

        binding.btnCadastre.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        });

        binding.loginLink.setOnClickListener(v -> {
            Toast.makeText(this, "Redirecionando para recuperação de senha", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validarCampos(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError("Email inválido");
            return false;
        }
        if (senha.length() < 6) {
            binding.senha.setError("Mínimo 6 caracteres");
            return false;
        }
        return true;
    }

    private void fazerLogin(String email, String senha) {
        binding.progressBar.setVisibility(View.VISIBLE);
        disableButtons();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(new LoginRequest(email, senha));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                enableButtons();

                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body());
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                enableButtons();
                Toast.makeText(LoginActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "Erro na requisição: " + t.getMessage());
            }
        });
    }

    private void handleSuccessResponse(LoginResponse response) {
        if (response != null && response.getAccessToken() != null) {
            saveAuthData(response);
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .putExtra("ACCESS_TOKEN", response.getAccessToken()));
            finish();
        } else {
            Toast.makeText(this, "Resposta inválida da API", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleErrorResponse(Response<LoginResponse> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e(TAG, "Erro na API: " + errorBody);

            try {
                // Tenta parsear como JSON
                JsonObject jsonObject = JsonParser.parseString(errorBody).getAsJsonObject();
                String errorMsg = jsonObject.has("message") ?
                        jsonObject.get("message").getAsString() : "Erro desconhecido";
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // Se não for JSON, mostra mensagem genérica
                Toast.makeText(this,
                        "Erro " + response.code() + ": " + response.message(),
                        Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAuthData(LoginResponse response) {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        prefs.edit()
                .putString("ACCESS_TOKEN", response.getAccessToken())
                .putInt("EXPIRES_IN", response.getExpiresIn())
                .putString("ROLE", response.getRole())
                .apply();
    }

    private void disableButtons() {
        binding.btnEntrar.setEnabled(false);
        binding.btnCadastre.setEnabled(false);
    }

    private void enableButtons() {
        binding.btnEntrar.setEnabled(true);
        binding.btnCadastre.setEnabled(true);
    }
}