package com.mobile.unithub.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.mobile.unithub.R;
import com.mobile.unithub.adapters.ImageCarouselAdapter;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.responses.FeedItemResponse;
import com.mobile.unithub.components.AppBarMenu;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetalhesEventosActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView title, description, dateTime, location, category;
    private ViewPager2 eventImageCarousel;
    private static final String TAG = "DetalhesEventosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_detalhe_evento);

            AppBarMenu appBarMenu = findViewById(R.id.appBarMenu);
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            appBarMenu.attachDrawer(drawerLayout, this);

            // Inicializar os elementos do layout
            title = findViewById(R.id.eventTitle);
            category = findViewById(R.id.eventCategories);
            description = findViewById(R.id.eventDescription);
            dateTime = findViewById(R.id.eventDateTime);
            location = findViewById(R.id.eventLocation);
            eventImageCarousel = findViewById(R.id.eventImageCarousel);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            Button subscribeButton = findViewById(R.id.subscribeButton);

            // Inicializar o ApiService
            apiService = ApiClient.getClient(this).create(ApiService.class);

            // Obter o ID do evento passado pela Intent
            String eventId = getIntent().getStringExtra("EVENT_ID");
            if (eventId != null) {
                progressBar.setVisibility(View.VISIBLE);
                carregarDetalhesEvento(eventId, progressBar, subscribeButton);
            } else {
                Log.e(TAG, "ID do evento não encontrado na Intent");
                Toast.makeText(this, "ID do evento não encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro na inicialização da tela de detalhes do evento", e);
            Toast.makeText(this, "Erro ao iniciar tela de detalhes.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void carregarDetalhesEvento(String eventId, ProgressBar progressBar, Button subscribeButton) {
        try {
            apiService.getEventDetails(eventId).enqueue(new Callback<FeedItemResponse>() {
                @Override
                public void onResponse(Call<FeedItemResponse> call, Response<FeedItemResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        FeedItemResponse event = response.body();

                        // Atualizar os elementos do layout com os detalhes do evento
                        title.setText(event.getTitle());
                        description.setText(event.getDescription());
                        dateTime.setText(formatarData(event.getDateTime()));
                        location.setText(event.getLocation());

                        List<String> categorias = event.getCategory();
                        Log.i(TAG, "Categorias do evento: " + categorias);
                        String categoriasTexto = "";
                        if (categorias != null && !categorias.isEmpty()) {
                            if (event.isOfficial()) {
                                categoriasTexto = "Oficial | " + String.join(" | ", categorias);
                            } else {
                                categoriasTexto = "Não Oficial | " + String.join(" | ", categorias);
                            }
                        } else {
                            categoriasTexto = event.isOfficial() ? "Oficial" : "Não Oficial";
                        }
                        category.setText(categoriasTexto);

                        if (event.getImages() != null && !event.getImages().isEmpty()) {
                            ImageCarouselAdapter carouselAdapter = new ImageCarouselAdapter(DetalhesEventosActivity.this, event.getImages());
                            eventImageCarousel.setAdapter(carouselAdapter);
                            eventImageCarousel.setVisibility(View.VISIBLE);
                            Log.d(TAG, "Imagens do evento exibidas no carrossel.");
                        } else {
                            eventImageCarousel.setVisibility(View.GONE);
                            Log.d(TAG, "Evento sem imagens, carrossel oculto.");
                        }

                        if (event.getMaxParticipants() > 0) {
                            subscribeButton.setVisibility(View.VISIBLE);
                            subscribeButton.setOnClickListener(v -> {
                                try {
                                    inscreverEvento(eventId, progressBar, subscribeButton);
                                } catch (Exception e) {
                                    Log.e(TAG, "Erro ao tentar inscrever no evento", e);
                                    Toast.makeText(DetalhesEventosActivity.this, "Erro ao tentar inscrever.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            subscribeButton.setVisibility(View.GONE);
                            Log.d(TAG, "Evento sem vagas, botão de inscrição oculto.");
                        }
                    } else {
                        Log.e(TAG, "Falha ao carregar detalhes do evento. Código: " + (response != null ? response.code() : "null"));
                        Toast.makeText(DetalhesEventosActivity.this, "Falha ao carregar os detalhes do evento", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeedItemResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Erro na conexão ao carregar detalhes do evento", t);
                    Toast.makeText(DetalhesEventosActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Exceção ao carregar detalhes do evento", e);
            Toast.makeText(this, "Erro inesperado ao carregar detalhes.", Toast.LENGTH_SHORT).show();
        }
    }

    private void inscreverEvento(String eventId, ProgressBar progressBar, Button subscribeButton) {
        try {
            progressBar.setVisibility(View.VISIBLE);

            apiService.subscribeToEvent(eventId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        Log.i(TAG, "Inscrição realizada com sucesso para o evento: " + eventId);
                        Toast.makeText(DetalhesEventosActivity.this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
                        subscribeButton.setVisibility(View.GONE);
                    } else if (response.code() == 409) {
                        Log.w(TAG, "Usuário já está inscrito no evento: " + eventId);
                        Toast.makeText(DetalhesEventosActivity.this, "Você já está inscrito neste evento", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        Log.w(TAG, "Inscrição não permitida para o evento: " + eventId);
                        Toast.makeText(DetalhesEventosActivity.this, "Inscrição não permitida", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Erro ao tentar se inscrever. Código: " + response.code());
                        Toast.makeText(DetalhesEventosActivity.this, "Erro ao tentar se inscrever", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Erro ao tentar se inscrever no evento", t);
                    Toast.makeText(DetalhesEventosActivity.this, "Erro ao tentar se inscrever: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Exceção ao tentar se inscrever no evento", e);
            Toast.makeText(this, "Erro inesperado ao tentar se inscrever.", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatarData(String dataOriginal) {
        try {
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date data = formatoOriginal.parse(dataOriginal);
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return formatoDesejado.format(data);
        } catch (ParseException e) {
            Log.e(TAG, "Erro ao formatar data: " + dataOriginal, e);
            return dataOriginal;
        } catch (Exception e) {
            Log.e(TAG, "Erro inesperado ao formatar data: " + dataOriginal, e);
            return dataOriginal;
        }
    }
}