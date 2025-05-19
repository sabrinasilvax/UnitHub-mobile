package com.mobile.unithub.activities;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Carregar cursos no Spinner
        carregarCursos();

        // Configuração do botão de cadastro
        binding.btnCadastre.setOnClickListener(v -> {
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
        });

        // Configuração do link para login
        binding.loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
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
                            } else {
                                selectedCourseId = -1; // Nenhum curso válido selecionado
                            }
                        }

                        @Override
                        public void onNothingSelected(android.widget.AdapterView<?> parent) {
                            selectedCourseId = -1; // Nenhum curso selecionado
                        }
                    });
                } else {
                    Toast.makeText(CadastroActivity.this, "Falha ao carregar cursos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ListarCursosResponse>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CadastroActivity.this, "Erro na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarCampos() {
        if (binding.NomeCompleto.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite seu nome completo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.numero.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite seu número", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite seu e-mail institucional", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.senha.getText().toString().isEmpty()) {
            Toast.makeText(this, "Crie uma senha", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!binding.senha.getText().toString().equals(binding.confirmarSenha.getText().toString())) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedCourseId == -1) {
            Toast.makeText(this, "Selecione um curso", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void cadastrarUsuario(String email, String senha, String confirmarSenha, String numero, String nome, int courseId) {
        CadastroRequest request = new CadastroRequest(email, senha, confirmarSenha, numero, nome, courseId);

        binding.progressBar.setVisibility(View.VISIBLE); // Exibe um indicador de carregamento
        apiService.registerUser(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.progressBar.setVisibility(View.GONE); // Oculta o indicador de carregamento
                if (response.isSuccessful()) {
                    // Exibe mensagem de sucesso
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso! Agora faça o login.", Toast.LENGTH_LONG).show();

                    // Redireciona para a LoginActivity
                    Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Finaliza a CadastroActivity
                } else {
                    Toast.makeText(CadastroActivity.this, "Falha ao realizar cadastro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE); // Oculta o indicador de carregamento
                Toast.makeText(CadastroActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}