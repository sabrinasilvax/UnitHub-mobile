package com.mobile.unithub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // Configurar as categorias
        if (feedItem.getCategory() != null && !feedItem.getCategory().isEmpty()) {
            String categories = String.join(" | ", feedItem.getCategory());
            holder.categories.setText(categories);
        } else {
            holder.categories.setText(""); // Limpar texto se não houver categorias
        }

        // Configurar o carrossel de imagens
        if (feedItem.getImages() != null && !feedItem.getImages().isEmpty()) {
            ImageCarouselAdapter carouselAdapter = new ImageCarouselAdapter(context, feedItem.getImages());
            holder.imageCarousel.setAdapter(carouselAdapter);
            holder.imageCarousel.setVisibility(View.VISIBLE); // Exibe o carrossel
        } else {
            holder.imageCarousel.setVisibility(View.GONE); // Oculta o carrossel se não houver imagens
        }

        // Configurar o clique no item
        holder.itemView.setOnClickListener(v -> {
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
    
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feedItemTitle);
            description = itemView.findViewById(R.id.feedItemDescription);
            categories = itemView.findViewById(R.id.feedItemCategories);
            imageCarousel = itemView.findViewById(R.id.feedItemImageCarousel);
        }
    }
}