package com.mobile.unithub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mobile.unithub.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuração do botão de cadastro
        binding.btnCadastre.setOnClickListener(v -> {
            if (validarCampos()) {
                // Lógica de cadastro (substitua pelo seu backend)
                cadastrarUsuario(
                        binding.NomeCompleto.getText().toString(),
                        binding.numero.getText().toString(),
                        binding.email.getText().toString(),
                        binding.senha.getText().toString()
                );
            }
        });

        // Configuração do link para login
        binding.loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Opcional: remove a CadastroActivity da pilha
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

        return true;
    }

    private void cadastrarUsuario(String nome, String numero, String email, String senha) {
        // Implemente aqui a lógica real de cadastro (Firebase, API, etc)
        // Exemplo simulado:
        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

        // Redireciona para login após cadastro
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}