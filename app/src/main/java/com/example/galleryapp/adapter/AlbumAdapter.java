package com.example.galleryapp.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.LinkQueryImplementation;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {

    private List<Album> albums;
    private final OnItemClick<Album> listener;

    public AlbumAdapter(List<Album> albums, OnItemClick<Album> listener) {
        this.albums = albums;
        this.listener = listener;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_item, parent, false);
        return new AlbumHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, @SuppressLint("RecyclerView") int position) {
        Album album = albums.get(position);

        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        long number = linkQuery.countImage(album.getId());
        Uri uriFirstImage = linkQuery.getFirstImage(album.getId());


        Picasso.get()
                .load(uriFirstImage)
                .resize(1000, 320)
                .centerCrop()
                .into(holder.img);

        holder.albumName.setText(album.getName());

        holder.albumSize.setText(number + " Media");


        holder.folderItemLayoutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(album, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (albums != null) {
            return albums.size();
        }
        return 0;
    }

    public static class AlbumHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView albumName, albumSize;
        RelativeLayout folderItemLayoutId;
        CardView albumCard;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.folderPic);
            albumName = itemView.findViewById(R.id.folderName);
            albumSize = itemView.findViewById(R.id.folderSize);
            albumCard = itemView.findViewById(R.id.folderCard);
            folderItemLayoutId = itemView.findViewById(R.id.folderItemLayoutId);
        }
    }
}
