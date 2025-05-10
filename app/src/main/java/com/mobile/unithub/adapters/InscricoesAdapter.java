package com.mobile.unithub.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.unithub.R;
import com.mobile.unithub.api.responses.FeedItemResponse;

import java.util.List;

public class InscricoesAdapter extends RecyclerView.Adapter<InscricoesAdapter.InscricoesViewHolder> {

    private final Context context;
    private final List<FeedItemResponse> inscricoes;
    private final OnDesinscreverClickListener listener;

    public interface OnDesinscreverClickListener {
        void onDesinscreverClick(String eventId);
    }

    public InscricoesAdapter(Context context, List<FeedItemResponse> inscricoes, OnDesinscreverClickListener listener) {
        this.context = context;
        this.inscricoes = inscricoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InscricoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        return new InscricoesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InscricoesViewHolder holder, int position) {
        FeedItemResponse inscricao = inscricoes.get(position);

        // Log para verificar os dados
        Log.d("InscricoesAdapter", "Título: " + inscricao.getTitle());
        Log.d("InscricoesAdapter", "Categorias: " + inscricao.getCategory());

        // Configurar os textos
        holder.title.setText(inscricao.getTitle());
        holder.categories.setText(String.join(" | ", inscricao.getCategory()));

        // Configurar o botão "Desinscrever-se"
        holder.btnDesinscrever.setText("Desinscrever-se");
        holder.btnDesinscrever.setOnClickListener(v -> listener.onDesinscreverClick(inscricao.getEventId()));
    }

    @Override
    public int getItemCount() {
        return inscricoes.size();
    }

    public static class InscricoesViewHolder extends RecyclerView.ViewHolder {
        TextView title, categories;
        Button btnDesinscrever;

        public InscricoesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feedItemTitle);
            categories = itemView.findViewById(R.id.feedItemCategories);
            btnDesinscrever = itemView.findViewById(R.id.button); // Reutilizando o botão existente
        }
    }
}