package com.example.galleryapp.listener;

import com.example.galleryapp.adapter.PictureAdapter;

import java.util.List;

public interface OnItemClick<T> {
    void onClick(T item, int pos);

    void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<T> pics);
}
