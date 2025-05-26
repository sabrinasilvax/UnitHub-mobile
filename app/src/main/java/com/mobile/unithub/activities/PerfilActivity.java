package com.mobile.unithub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mobile.unithub.R;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.requests.UpdatePasswordRequest;
import com.mobile.unithub.api.responses.UserProfileResponse;
import com.mobile.unithub.components.AppBarMenu;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private static final String TAG = "PerfilActivity";
    private EditText nomeCompleto, numero, email, senha, confirmarSenha;
    private Button btnSalvar;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_perfil);

            AppBarMenu appBarMenu = findViewById(R.id.appBarMenu);
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            appBarMenu.attachDrawer(drawerLayout, this);

            // Inicializar os elementos do layout
            nomeCompleto = findViewById(R.id.NomeCompleto);
            numero = findViewById(R.id.numero);
            email = findViewById(R.id.email);
            senha = findViewById(R.id.senha);
            confirmarSenha = findViewById(R.id.confirmarSenha);
            btnSalvar = findViewById(R.id.btnSalvar);
            progressBar = findViewById(R.id.progressBar);

            // Tornar os campos de nome, número e e-mail somente leitura
            nomeCompleto.setEnabled(false);
            numero.setEnabled(false);
            email.setEnabled(false);

            // Inicializar o ApiService
            apiService = ApiClient.getClient(this).create(ApiService.class);

            // Carregar os dados do perfil
            carregarPerfil();

            // Configurar o botão "Salvar"
            btnSalvar.setOnClickListener(v -> salvarSenha());
        } catch (Exception e) {
            Log.e(TAG, "Erro na inicialização da tela de perfil", e);
            Toast.makeText(this, "Erro ao iniciar tela de perfil.", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarPerfil() {
        try {
            progressBar.setVisibility(View.VISIBLE);

            apiService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
                @Override
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        UserProfileResponse userProfile = response.body();
                        nomeCompleto.setText(userProfile.getName());
                        numero.setText(userProfile.getTelephone());
                        email.setText(userProfile.getEmail());
                        Log.d(TAG, "Perfil carregado com sucesso.");
                    } else if (response.code() == 401) {
                        Log.w(TAG, "Sessão expirada ao carregar perfil.");
                        redirecionarParaLogin();
                    } else {
                        Log.e(TAG, "Erro ao carregar perfil. Código: " + response.code());
                        Toast.makeText(PerfilActivity.this, "Erro ao carregar perfil.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Erro ao carregar perfil", t);
                    Toast.makeText(PerfilActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Exceção ao carregar perfil", e);
            Toast.makeText(this, "Erro inesperado ao carregar perfil.", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarSenha() {
        try {
            String novaSenha = senha.getText().toString();
            String confirmarNovaSenha = confirmarSenha.getText().toString();

            // Validações
            if (novaSenha.isEmpty() || confirmarNovaSenha.isEmpty()) {
                Toast.makeText(this, "Os campos de senha não podem estar vazios.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Tentativa de salvar senha com campos vazios.");
                return;
            }

            if (novaSenha.length() < 6) {
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Tentativa de salvar senha com menos de 6 caracteres.");
                return;
            }

            if (!novaSenha.equals(confirmarNovaSenha)) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Tentativa de salvar senha com confirmação diferente.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // Criar o objeto de requisição com a senha e a confirmação
            UpdatePasswordRequest request = new UpdatePasswordRequest(novaSenha, confirmarNovaSenha);

            apiService.updateUserProfile(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        Toast.makeText(PerfilActivity.this, "Senha atualizada com sucesso.", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Senha atualizada com sucesso.");
                        senha.setText(""); // Limpar os campos de senha após salvar
                        confirmarSenha.setText("");
                    } else if (response.code() == 401) {
                        Log.w(TAG, "Sessão expirada ao atualizar senha.");
                        redirecionarParaLogin();
                    } else {
                        Log.e(TAG, "Erro ao atualizar senha. Código: " + response.code());
                        Toast.makeText(PerfilActivity.this, "Erro ao atualizar senha.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Erro ao atualizar senha", t);
                    Toast.makeText(PerfilActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Exceção ao tentar salvar senha", e);
            Toast.makeText(this, "Erro inesperado ao salvar senha.", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirecionarParaLogin() {
        try {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao redirecionar para login", e);
            Toast.makeText(this, "Erro ao redirecionar para login.", Toast.LENGTH_SHORT).show();
        }
    }
}