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

    List<Picture> pictureList;
    Context pictureContx;
    private final OnItemClick imageListerner;

    /**
     * @param pictureList    ArrayList of pictureFacer objects
     * @param pictureContx   The Activity of fragment context
     * @param imageListerner Interface for communication between adapter and fragment
     */
    public RecyclerViewPagerImageIndicator(List<Picture> pictureList, Context pictureContx, OnItemClick imageListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.imageListerner = imageListerner;
    }


    @NonNull
    @Override
    public indicatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.indicator_holder, parent, false);
        return new indicatorHolder(cell);
    }


    @Override
    public void onBindViewHolder(@NonNull indicatorHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Picture pic = pictureList.get(position);

        holder.positionController.setBackgroundColor(pic.isSelected()? Color.parseColor("#00000000") : Color.parseColor("#8c000000"));

        Picasso.get()
                .load(pic.getUri())

                .into(holder.image);
//        Glide.with(pictureContx)
//                .load(pic.getPicturePath())
//                .apply(new RequestOptions().centerCrop())
//                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.card.setCardElevation(5);
                pic.setSelected(true);
                notifyDataSetChanged();
                imageListerner.onClick(pic, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
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
