package com.example.galleryapp.adapter;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {
    private List<Picture> pictures;
    private int resource;
    private OnItemClick<Picture> listener;

    public PictureAdapter(int resource, @NonNull List<Picture> pictures, OnItemClick<Picture> listener) {
        this.resource = resource;
        this.pictures = pictures;
        this.listener = listener;
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
        if (holder.picture != null) {
            Picasso.get()
                    .load(picture.getUri())
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.picture);
            setTransitionName(holder.picture, position + "_image");

        }
        if (holder.picName != null) {
            String name = picture.getName();
            if (name.length() > 22) {
                name = name.substring(0, 22);
                name += "...";
            }
            holder.picName.setText(name);
        }
        if (holder.picSize != null) {
            holder.picSize.setText(picture.getSizeStr());
        }
        if (holder.picCreatedDate != null) {
            holder.picCreatedDate.setText(DateUtil.getDate(picture.getCreatedDate()));
        }

        holder.picLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(picture, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class PictureHolder extends RecyclerView.ViewHolder {

        public View picLayout;
        public ImageView picture;
        public TextView picName, picSize, picCreatedDate;

        PictureHolder(@NonNull View itemView) {
            super(itemView);
            picLayout = itemView.findViewById(R.id.picLayout);
            picture = itemView.findViewById(R.id.picture);
            picName = itemView.findViewById(R.id.picName);
            picSize = itemView.findViewById(R.id.picSize);
            picCreatedDate = itemView.findViewById(R.id.picCreatedDate);
        }
    }
}
