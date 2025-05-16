package com.mobile.unithub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.unithub.R;
import com.mobile.unithub.adapters.InscricoesAdapter;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.responses.FeedItemResponse;
import com.mobile.unithub.components.AppBarMenu;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscricoesActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscricoes);

        AppBarMenu appBarMenu = findViewById(R.id.appBarMenu);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        appBarMenu.attachDrawer(drawerLayout, this);

        // Inicializar os elementos do layout
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Inicializar o ApiService
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Carregar as inscrições
        carregarInscricoes();
    }

    private void carregarInscricoes() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        apiService.getEventDetails().enqueue(new Callback<List<FeedItemResponse>>() { // Alterado para List<FeedItemResponse>
            @Override
            public void onResponse(Call<List<FeedItemResponse>> call, Response<List<FeedItemResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<FeedItemResponse> inscricoes = response.body();
                    exibirInscricoes(inscricoes);
                } else if (response.code() == 401) {
                    redirecionarParaLogin();
                } else {
                    Toast.makeText(InscricoesActivity.this, "Erro ao carregar inscrições.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FeedItemResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(InscricoesActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exibirInscricoes(List<FeedItemResponse> inscricoes) {
        InscricoesAdapter adapter = new InscricoesAdapter(this, inscricoes, this::cancelarInscricao);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Notificar o adaptador sobre os dados
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void cancelarInscricao(String eventId) {
        progressBar.setVisibility(View.VISIBLE);

        apiService.unsubscribeFromEvent(eventId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(InscricoesActivity.this, "Inscrição cancelada com sucesso.", Toast.LENGTH_SHORT).show();
                    carregarInscricoes(); // Recarregar a lista de inscrições
                } else if (response.code() == 401) {
                    redirecionarParaLogin();
                } else {
                    Toast.makeText(InscricoesActivity.this, "Erro ao cancelar inscrição.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(InscricoesActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirecionarParaLogin() {
        Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}