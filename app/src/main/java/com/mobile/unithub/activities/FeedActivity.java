package com.mobile.unithub.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.unithub.R;
import com.mobile.unithub.adapters.FeedAdapter;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.responses.FeedResponse;
import com.mobile.unithub.api.responses.FeedItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private ApiService apiService; // Certifique-se de declarar a variável
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Inicializar os elementos do layout
        TextView feedTitle = findViewById(R.id.feedTitle);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Inicializar o ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        // Carregar os dados
        carregarFeed();
    }

    private void carregarFeed() {
        progressBar.setVisibility(View.VISIBLE); // Exibe o ProgressBar
        recyclerView.setVisibility(View.GONE); // Oculta o RecyclerView enquanto carrega os dados
    
        apiService.getFeed().enqueue(new Callback<FeedResponse>() { // Ajuste o tipo de retorno para FeedResponse
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                progressBar.setVisibility(View.GONE); // Oculta o ProgressBar
            
                if (response.isSuccessful() && response.body() != null) {
                    // Acessar a lista de itens dentro do objeto de resposta
                    List<FeedItemResponse> feedItems = response.body().getFeedItems();
            
                    // Configurar o adapter diretamente com FeedItemResponse
                    FeedAdapter adapter = new FeedAdapter(FeedActivity.this, feedItems);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE); // Exibe o RecyclerView
                } else {
                    Toast.makeText(FeedActivity.this, "Falha ao carregar os dados", Toast.LENGTH_SHORT).show();
                }
            }
    
            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Oculta o ProgressBar
                Toast.makeText(FeedActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}