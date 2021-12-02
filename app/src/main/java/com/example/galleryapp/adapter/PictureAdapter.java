package com.example.galleryapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.galleryapp.R;
import com.example.galleryapp.model.Picture;

import java.util.List;

public class PictureAdapter extends ArrayAdapter<Picture> {
    private Activity context;
    private List<Picture> pictures;
    private int resource;

    public PictureAdapter(@NonNull Activity context, int resource, @NonNull List<Picture> pictures) {
        super(context, resource, pictures);
        this.context = context;
        this.resource = resource;
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Picture picture = pictures.get(position);

        @SuppressLint("ViewHolder")
        View view = this.context.getLayoutInflater().inflate(this.resource, null);
        ImageView img = (ImageView) view.findViewById(R.id.picture);
        Bitmap myBitmap = BitmapFactory.decodeFile(picture.getPath());
        img.setImageBitmap(myBitmap);

        return view;

    }
}
