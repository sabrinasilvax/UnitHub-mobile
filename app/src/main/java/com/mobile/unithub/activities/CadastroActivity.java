package com.mobile.unithub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.requests.CadastroRequest;
import com.mobile.unithub.api.responses.ListarCursosResponse;
import com.mobile.unithub.databinding.ActivityCadastroBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private ApiService apiService;
    private List<ListarCursosResponse> listaCursos = new ArrayList<>();
    private int selectedCourseId = -1; // ID do curso selecionado
    private static final String TAG = "CadastroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityCadastroBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            apiService = ApiClient.getClient(this).create(ApiService.class);

            // Carregar cursos no Spinner
            carregarCursos();

            // Configuração do botão de cadastro
            binding.btnCadastre.setOnClickListener(v -> {
                try {
                    if (validarCampos()) {
                        cadastrarUsuario(
                                binding.email.getText().toString(),
                                binding.senha.getText().toString(),
                                binding.confirmarSenha.getText().toString(),
                                binding.numero.getText().toString(),
                                binding.NomeCompleto.getText().toString(),
                                selectedCourseId
                        );
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao tentar cadastrar usuário: ", e);
                    Toast.makeText(this, "Erro inesperado ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            });

            // Configuração do link para login
            binding.loginLink.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao abrir tela de login: ", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro na inicialização da tela de cadastro: ", e);
            Toast.makeText(this, "Erro ao iniciar tela de cadastro.", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarCursos() {
        binding.progressBar.setVisibility(View.VISIBLE); // Exibe um indicador de carregamento
        apiService.getCourses().enqueue(new Callback<List<ListarCursosResponse>>() {
            @Override
            public void onResponse(Call<List<ListarCursosResponse>> call, Response<List<ListarCursosResponse>> response) {
                binding.progressBar.setVisibility(View.GONE); // Oculta o indicador de carregamento
                if (response.isSuccessful() && response.body() != null) {
                    listaCursos = response.body();
                    List<String> nomesCursos = new ArrayList<>();

                    // Adiciona a mensagem padrão como primeiro item
                    nomesCursos.add("Escolha seu curso");

                    for (ListarCursosResponse curso : listaCursos) {
                        nomesCursos.add(curso.getNome());
                    }

                    // Configurar o Spinner com os nomes dos cursos
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroActivity.this,
                            android.R.layout.simple_spinner_item, nomesCursos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCursos.setAdapter(adapter);

                    // Define a seleção inicial como "Escolha seu curso"
                    binding.spinnerCursos.setSelection(0, false);

                    // Listener para capturar o curso selecionado
                    binding.spinnerCursos.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                            // Ignora a seleção se for o primeiro item (mensagem)
                            if (position > 0) {
                                selectedCourseId = listaCursos.get(position - 1).getCursoId(); // Ajusta o índice (-1)
                                Log.d(TAG, "Curso selecionado: " + selectedCourseId);
                            } else {
                                selectedCourseId = -1; // Nenhum curso válido selecionado
                                Log.d(TAG, "Nenhum curso selecionado");
                            }
                        }

                        @Override
                        public void onNothingSelected(android.widget.AdapterView<?> parent) {
                            selectedCourseId = -1; // Nenhum curso selecionado
                            Log.d(TAG, "Nenhum curso selecionado (onNothingSelected)");
                        }
                    });
                    Log.d(TAG, "Cursos carregados com sucesso: " + listaCursos.size());
                } else {
                    Log.e(TAG, "Falha ao carregar cursos: response.isSuccessful=" + response.isSuccessful());
                    Toast.makeText(CadastroActivity.this, "Falha ao carregar cursos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ListarCursosResponse>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Erro na conexão ao carregar cursos", t);
                Toast.makeText(CadastroActivity.this, "Erro na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarCampos() {
        try {
            String nome = binding.NomeCompleto.getText().toString().trim();
            String numero = binding.numero.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String senha = binding.senha.getText().toString();
            String confirmarSenha = binding.confirmarSenha.getText().toString();

            if (nome.isEmpty() || nome.length() < 3) {
                Log.w(TAG, "Nome inválido: " + nome);
                Toast.makeText(this, "Digite um nome completo válido (mínimo 3 letras)", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Validação robusta de telefone: apenas números, 8 a 15 dígitos
            if (numero.isEmpty() || !numero.matches("^\\d{8,15}$")) {
                Log.w(TAG, "Número de telefone inválido: " + numero);
                Toast.makeText(this, "Digite um número de telefone válido (apenas números, 8 a 15 dígitos)", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.w(TAG, "E-mail inválido: " + email);
                Toast.makeText(this, "Digite um e-mail institucional válido", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (senha.isEmpty() || senha.length() < 6) {
                Log.w(TAG, "Senha inválida: " + senha);
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!senha.equals(confirmarSenha)) {
                Log.w(TAG, "Senhas não coincidem");
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (selectedCourseId == -1) {
                Log.w(TAG, "Nenhum curso selecionado");
                Toast.makeText(this, "Selecione um curso", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao validar campos: ", e);
            Toast.makeText(this, "Erro ao validar campos.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void cadastrarUsuario(String email, String senha, String confirmarSenha, String numero, String nome, int courseId) {
        try {
            CadastroRequest request = new CadastroRequest(email, senha, confirmarSenha, numero, nome, courseId);

            binding.progressBar.setVisibility(View.VISIBLE); // Exibe um indicador de carregamento
            apiService.registerUser(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    binding.progressBar.setVisibility(View.GONE); // Oculta o indicador de carregamento
                    if (response.isSuccessful()) {
                        Log.i(TAG, "Cadastro realizado com sucesso para o e-mail: " + email);
                        Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso! Agora faça o login.", Toast.LENGTH_LONG).show();

                        // Redireciona para a LoginActivity
                        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza a CadastroActivity
                    } else {
                        Log.e(TAG, "Falha ao realizar cadastro. Código: " + response.code());
                        Toast.makeText(CadastroActivity.this, "Falha ao realizar cadastro", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE); // Oculta o indicador de carregamento
                    Log.e(TAG, "Erro ao cadastrar usuário", t);
                    Toast.makeText(CadastroActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exceção ao cadastrar usuário: ", e);
            Toast.makeText(this, "Erro inesperado ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
        }
    }
}