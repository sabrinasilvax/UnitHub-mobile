package com.mobile.unithub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.unithub.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Elementos da UI
        EditText editEmail = binding.email;
        EditText editSenha = binding.senha;
        Button btnEntrar = binding.btnEntrar;
        Button btnCadastre = binding.btnCadastre; // certifique-se que o ID no XML é btnCadastrar

        btnEntrar.setOnClickListener(v -> {
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (validarLogin(email, senha)) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
            }
        });

        btnCadastre.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        });
    }

    private boolean validarLogin(String email, String senha) {
        // Implemente sua lógica de validação real aqui
        return !email.isEmpty() && !senha.isEmpty();
    }
}