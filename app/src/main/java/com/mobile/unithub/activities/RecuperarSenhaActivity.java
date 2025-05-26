package com.mobile.unithub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.unithub.R;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.requests.RecoverPasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private static final String TAG = "RecuperarSenhaActivity";
    private EditText emailEditText;
    private Button btnEnviar;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_recuperar_senha);

            // Inicializar os elementos do layout
            emailEditText = findViewById(R.id.email);
            btnEnviar = findViewById(R.id.btnEnviar);
            progressBar = findViewById(R.id.progressBar);

            // Inicializar o ApiService
            apiService = ApiClient.getClient(this).create(ApiService.class);

            // Configurar o botão "Enviar"
            btnEnviar.setOnClickListener(v -> enviarEmailRecuperacao());
            Log.d(TAG, "Tela de recuperação de senha inicializada.");
        } catch (Exception e) {
            Log.e(TAG, "Erro na inicialização da tela de recuperação de senha", e);
            Toast.makeText(this, "Erro ao iniciar tela de recuperação de senha.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarEmailRecuperacao() {
        try {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu e-mail.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Campo de e-mail vazio.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            btnEnviar.setEnabled(false);

            // Criar o objeto de requisição
            RecoverPasswordRequest request = new RecoverPasswordRequest(email);

            Log.d(TAG, "Enviando requisição de recuperação para o e-mail: " + email);

            apiService.emailRecoverPassword(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressBar.setVisibility(View.GONE);
                    btnEnviar.setEnabled(true);

                    if (response.isSuccessful()) {
                        Log.i(TAG, "E-mail de recuperação enviado com sucesso para: " + email);
                        Toast.makeText(RecuperarSenhaActivity.this, "As instruções de recuperação foram enviadas por e-mail.", Toast.LENGTH_LONG).show();

                        // Redirecionar para LoginActivity após 3 segundos
                        new Handler().postDelayed(() -> {
                            try {
                                Intent intent = new Intent(RecuperarSenhaActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Log.d(TAG, "Redirecionado para LoginActivity após recuperação.");
                            } catch (Exception e) {
                                Log.e(TAG, "Erro ao redirecionar para LoginActivity", e);
                            }
                        }, 3000);
                    } else if (response.code() == 404) {
                        Log.w(TAG, "E-mail não encontrado: " + email);
                        Toast.makeText(RecuperarSenhaActivity.this, "E-mail não encontrado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Erro ao enviar e-mail de recuperação. Código: " + response.code());
                        Toast.makeText(RecuperarSenhaActivity.this, "Erro ao enviar e-mail de recuperação.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    btnEnviar.setEnabled(true);
                    Log.e(TAG, "Erro ao enviar e-mail de recuperação", t);
                    Toast.makeText(RecuperarSenhaActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            btnEnviar.setEnabled(true);
            Log.e(TAG, "Exceção ao tentar enviar e-mail de recuperação", e);
            Toast.makeText(this, "Erro inesperado ao enviar e-mail de recuperação.", Toast.LENGTH_SHORT).show();
        }
    }
}