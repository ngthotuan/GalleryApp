package com.example.galleryapp.adapter;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {
    private List<Picture> pictures;
    private int resource;

    public PictureAdapter(int resource, @NonNull List<Picture> pictures) {
        this.resource = resource;
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cell = inflater.inflate(resource, parent, false);
        return new PictureHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureHolder holder, @SuppressLint("RecyclerView") int position) {
        final Picture picture = pictures.get(position);
        Log.d("TAG", "onBindViewHolder: " + picture);
        Picasso.get()
                .load(picture.getUri())
                .resize(200, 200)
                .centerCrop()
                .into(holder.picture);

        setTransitionName(holder.picture, position + "_image");

//        holder.picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                picListerner.onPicClicked(holder,position, pictureList);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class PictureHolder extends RecyclerView.ViewHolder {

        public ImageView picture;

        PictureHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
        }
    }
}
