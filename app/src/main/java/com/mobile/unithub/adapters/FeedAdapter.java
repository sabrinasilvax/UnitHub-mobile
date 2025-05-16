package com.mobile.unithub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.mobile.unithub.R;
import com.mobile.unithub.activities.DetalhesEventosActivity;
import com.mobile.unithub.api.responses.FeedItemResponse;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private final Context context;
    private final List<FeedItemResponse> feedItems;

    public FeedAdapter(Context context, List<FeedItemResponse> feedItems) {
        this.context = context;
        this.feedItems = feedItems;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedItemResponse feedItem = feedItems.get(position);

        // Configurar os textos
        holder.title.setText(feedItem.getTitle());
        holder.description.setText(feedItem.getDescription());
        

        List<String> categorias = feedItem.getCategory();
        String categoriasTexto = "";
        if (categorias != null && !categorias.isEmpty()) {
            if (feedItem.isOfficial()) {
                categoriasTexto = "Oficial | " + String.join(" | ", categorias);
            } else {
                categoriasTexto = "Não Oficial | " + String.join(" | ", categorias);
            }
        } else {
            categoriasTexto = feedItem.isOfficial() ? "Oficial" : "Não Oficial";
        }
        holder.categories.setText(categoriasTexto);

        // Configurar o carrossel de imagens
        if (feedItem.getImages() != null && !feedItem.getImages().isEmpty()) {
            ImageCarouselAdapter carouselAdapter = new ImageCarouselAdapter(context, feedItem.getImages());
            holder.imageCarousel.setAdapter(carouselAdapter);
            holder.imageCarousel.setVisibility(View.VISIBLE); // Exibe o carrossel
        } else {
            holder.imageCarousel.setVisibility(View.GONE); // Oculta o carrossel se não houver imagens
        }
        
        // Configurar o clique no item
        holder.button.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalhesEventosActivity.class);
            intent.putExtra("EVENT_ID", feedItem.getEventId()); // Passar o ID do evento
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, categories;
        ViewPager2 imageCarousel;
        Button button;

    
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feedItemTitle);
            description = itemView.findViewById(R.id.feedItemDescription);
            categories = itemView.findViewById(R.id.feedItemCategories);
            imageCarousel = itemView.findViewById(R.id.feedItemImageCarousel);
            button = itemView.findViewById(R.id.button);
        }
    }
}