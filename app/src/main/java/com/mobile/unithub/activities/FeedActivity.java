package com.mobile.unithub.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.mobile.unithub.components.PaginationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PaginationView paginationView;

    private int currentPage = 1; // Página inicial
    private int totalPages = 1;  // Total de páginas será atualizado pela API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Inicializar os elementos do layout
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        paginationView = findViewById(R.id.paginationView);

        // Configurar o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Inicializar o ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        // Configurar o listener de paginação
        paginationView.setOnPageChangeListener(page -> {
            currentPage = page;
            carregarFeed(page);
        });

        // Carregar a primeira página
        carregarFeed(currentPage);
    }

private void carregarFeed(int page) {
    Log.d("FeedActivity", "Carregando página: " + page); // Log para depuração
    progressBar.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);

    apiService.getFeed(page).enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
            progressBar.setVisibility(View.GONE);

            if (response.isSuccessful() && response.body() != null) {
                FeedResponse feedResponse = response.body();
                List<FeedItemResponse> feedItems = feedResponse.getFeedItems();

                // Atualizar o RecyclerView com os itens do feed
                FeedAdapter adapter = new FeedAdapter(FeedActivity.this, feedItems);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);

                // Atualizar o PaginationView com os dados de paginação
                totalPages = feedResponse.getTotalPages();
                currentPage = feedResponse.getPage();
                paginationView.setTotalPages(totalPages);
                paginationView.setCurrentPage(currentPage);

                // Tornar o PaginationView visível
                paginationView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(FeedActivity.this, "Nenhuma publicação encontrada.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(FeedActivity.this, "Erro ao carregar o feed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
}