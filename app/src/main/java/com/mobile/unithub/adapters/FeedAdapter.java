package com.mobile.unithub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.mobile.unithub.R;
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
        holder.location.setText(feedItem.getLocation());
        holder.dateTime.setText(feedItem.getDateTime());
    
        // Configurar o carrossel de imagens
        if (feedItem.getImages() != null && !feedItem.getImages().isEmpty()) {
            ImageCarouselAdapter carouselAdapter = new ImageCarouselAdapter(context, feedItem.getImages());
            holder.imageCarousel.setAdapter(carouselAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, location, dateTime;
        ViewPager2 imageCarousel;
    
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feedItemTitle);
            description = itemView.findViewById(R.id.feedItemDescription);
            location = itemView.findViewById(R.id.feedItemLocation);
            dateTime = itemView.findViewById(R.id.feedItemDateTime);
            imageCarousel = itemView.findViewById(R.id.feedItemImageCarousel);
        }
    }
}