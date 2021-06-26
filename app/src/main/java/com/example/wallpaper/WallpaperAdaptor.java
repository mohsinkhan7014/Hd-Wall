package com.example.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WallpaperAdaptor extends RecyclerView.Adapter<WallpaperViewHolder>{

    private Context context;
    private List<WallpaperModel> wallpaperModelList;

    public WallpaperAdaptor(Context context, List<WallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    public WallpaperAdaptor() {
    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.wallpaper_item,parent,false);

        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder( WallpaperViewHolder holder, int position) {
        Glide.with(context).load(wallpaperModelList.get(position).getMediumUrl()).into(holder.imageView);
        holder.imageView.setOnClickListener(x->{
            context.startActivity(new Intent(context,FullScreenWallpaper.class)
                    .putExtra("originalUrl",wallpaperModelList.get(position).getOriginalUrl()));
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }
}

class WallpaperViewHolder extends RecyclerView.ViewHolder{


    ImageView imageView;
    CardView cardView;
    public WallpaperViewHolder( View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageViewItem);
        cardView=itemView.findViewById(R.id.card);

    }
}
