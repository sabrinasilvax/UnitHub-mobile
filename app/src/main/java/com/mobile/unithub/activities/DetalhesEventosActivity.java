package com.mobile.unithub.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.mobile.unithub.R;
import com.mobile.unithub.adapters.ImageCarouselAdapter;
import com.mobile.unithub.api.ApiClient;
import com.mobile.unithub.api.ApiService;
import com.mobile.unithub.api.responses.FeedItemResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalhesEventosActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView title, description, dateTime, location;
    private ViewPager2 eventImageCarousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);

        // Inicializar os elementos do layout
        title = findViewById(R.id.eventTitle);
        description = findViewById(R.id.eventDescription);
        dateTime = findViewById(R.id.eventDateTime);
        location = findViewById(R.id.eventLocation);
        eventImageCarousel = findViewById(R.id.eventImageCarousel); // Corrigido para ViewPager2
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button subscribeButton = findViewById(R.id.subscribeButton);

        // Inicializar o ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        // Obter o ID do evento passado pela Intent
        String eventId = getIntent().getStringExtra("EVENT_ID");
        if (eventId != null) {
            // Exibir o ProgressBar
            progressBar.setVisibility(View.VISIBLE);

            // Carregar os detalhes do evento
            carregarDetalhesEvento(eventId, progressBar, subscribeButton);
        } else {
            Toast.makeText(this, "ID do evento não encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void carregarDetalhesEvento(String eventId, ProgressBar progressBar, Button subscribeButton) {
        apiService.getEventDetails(eventId).enqueue(new Callback<FeedItemResponse>() {
            @Override
            public void onResponse(Call<FeedItemResponse> call, Response<FeedItemResponse> response) {
                // Ocultar o ProgressBar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    FeedItemResponse event = response.body();

                    // Atualizar os elementos do layout com os detalhes do evento
                    title.setText(event.getTitle());
                    description.setText(event.getDescription());
                    dateTime.setText(formatarData(event.getDateTime())); // Formatar e exibir a data
                    location.setText(event.getLocation());

                    // Configurar o carrossel de imagens
                    if (event.getImages() != null && !event.getImages().isEmpty()) {
                        ImageCarouselAdapter carouselAdapter = new ImageCarouselAdapter(DetalhesEventosActivity.this, event.getImages());
                        eventImageCarousel.setAdapter(carouselAdapter);
                    }

                    // Exibir o botão "Inscrever-se" se o número de participantes for maior que zero
                    if (event.getMaxParticipants() > 0) {
                        subscribeButton.setVisibility(View.VISIBLE);

                        // Configurar o clique no botão "Inscrever-se"
                        subscribeButton.setOnClickListener(v -> inscreverEvento(eventId, subscribeButton));
                    }
                } else {
                    Toast.makeText(DetalhesEventosActivity.this, "Falha ao carregar os detalhes do evento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedItemResponse> call, Throwable t) {
                // Ocultar o ProgressBar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DetalhesEventosActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inscreverEvento(String eventId, Button subscribeButton) {
        apiService.subscribeToEvent(eventId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalhesEventosActivity.this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    subscribeButton.setVisibility(View.GONE); // Ocultar o botão após a inscrição
                } else if (response.code() == 400) {
                    Toast.makeText(DetalhesEventosActivity.this, "Inscrição não permitida", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 409) {
                    Toast.makeText(DetalhesEventosActivity.this, "Você já está inscrito neste evento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalhesEventosActivity.this, "Erro ao tentar se inscrever: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatarData(String dataOriginal) {
        try {
            // Formato original da data (exemplo: "2025-05-06T15:30:00")
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date data = formatoOriginal.parse(dataOriginal);

            // Formato desejado (exemplo: "06 março 2025")
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return formatoDesejado.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return dataOriginal; // Retorna a data original em caso de erro
        }
    }
}