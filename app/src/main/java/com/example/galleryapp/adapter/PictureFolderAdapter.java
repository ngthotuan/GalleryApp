package com.example.galleryapp.adapter;

import android.annotation.SuppressLint;
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
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.PictureFolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PictureFolderAdapter extends RecyclerView.Adapter<PictureFolderAdapter.FolderHolder> {

    private final List<PictureFolder> folders;
    private final int resource;
    private final OnItemClick<PictureFolder> itemClickListener;

    public PictureFolderAdapter(List<PictureFolder> folders, int resource, OnItemClick<PictureFolder> itemClickListener) {
        this.folders = folders;
        this.resource = resource;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(resource, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, @SuppressLint("RecyclerView") int position) {
        final PictureFolder folder = folders.get(position);

        Picasso.get()
                .load(folder.getFirstPicture())
                .resize(1000, 320)
                .centerCrop()
                .into(holder.folderPic);

        //setting the number of images
        String text = "" + folder.getName();
        String folderSizeString = "" + folder.getTotalPicture() + " Media";
        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);
        holder.folderItemLayoutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(folder, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public static class FolderHolder extends RecyclerView.ViewHolder {
        ImageView folderPic;
        TextView folderName;
        TextView folderSize;
        CardView folderCard;
        RelativeLayout folderItemLayoutId;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize = itemView.findViewById(R.id.folderSize);
            folderCard = itemView.findViewById(R.id.folderCard);
            folderItemLayoutId = itemView.findViewById(R.id.folderItemLayoutId);
        }
    }

}
