package com.example.galleryapp.adapter;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.galleryapp.click.ItemClickListener;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class pictureFolderAdapter extends RecyclerView.Adapter<pictureFolderAdapter.FolderHolder>{

    private List<PictureFolder> folders;
    private int resource;
    private ItemClickListener itemClickListener;
//    private itemClickListener listenToClick;

    /**
     *
     * @param folders An ArrayList of String that represents paths to folders on the external storage that contain pictures
     * @param folderContx The Activity or fragment Context
//     * @param listen interFace for communication between adapter and fragment or activity
     */
    public pictureFolderAdapter(List<PictureFolder> folders, Context folderContx, int resource, ItemClickListener itemClickListener) {
        this.folders = folders;
        this.resource = resource;
        this.itemClickListener = itemClickListener;
//        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(resource, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final PictureFolder folder = folders.get(position);

//        Log.e("TAG", folder.getFirstPicture() );
        Picasso.get()
                .load(folder.getFirstPicture())
                .resize(200, 200)
                .centerCrop()
                .into(holder.folderPic);

        //setting the number of images
        String text = ""+folder.getName();
        String folderSizeString=""+folder.getTotalPicture()+" Media";
        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);
        holder.folderItemLayoutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.folderImageClick(folder);
                Log.e("TAG", "onClick: "+folder );
            }
        });

//        holder.folderPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listenToClick.onPicClicked(folder.getPath(),folder.getFolderName());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder{
        ImageView folderPic;
        TextView folderName;
        TextView folderSize;
        CardView folderCard;
        RelativeLayout folderItemLayoutId;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize=itemView.findViewById(R.id.folderSize);
            folderCard = itemView.findViewById(R.id.folderCard);
            folderItemLayoutId = itemView.findViewById(R.id.folderItemLayoutId);
        }
    }

}
