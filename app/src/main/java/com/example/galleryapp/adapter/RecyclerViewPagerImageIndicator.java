package com.example.galleryapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewPagerImageIndicator extends RecyclerView.Adapter<RecyclerViewPagerImageIndicator.indicatorHolder> {

    List<Picture> pictures;
    Context context;
    private final OnItemClick listener;


    public RecyclerViewPagerImageIndicator(List<Picture> pictures, Context context, OnItemClick listener) {
        this.pictures = pictures;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public indicatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.indicator_holder, parent, false);
        return new indicatorHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull indicatorHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Picture picture = pictures.get(position);

        holder.positionController.setBackgroundColor(picture.isSelected() ? Color.parseColor("#00000000") : Color.parseColor("#8c000000"));

        Picasso.get()
                .load(picture.getUri())
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.card.setCardElevation(5);
                picture.setSelected(true);
                notifyDataSetChanged();
                listener.onClick(picture, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class indicatorHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        private final CardView card;
        View positionController;

        indicatorHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageIndicator);
            card = itemView.findViewById(R.id.indicatorCard);
            positionController = itemView.findViewById(R.id.activeImage);
        }
    }
}
