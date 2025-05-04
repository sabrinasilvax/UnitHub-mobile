package com.mobile.unithub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobile.unithub.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;
    private final List<String> imageUrls;

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Corrigido para usar o layout correto
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // Usando Glide para carregar a imagem
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder) // Substitua por uma imagem válida em drawable
                .error(R.drawable.ic_error_image) // Substitua por uma imagem válida em drawable
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache para melhorar o desempenho
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0; // Evita NullPointerException
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}