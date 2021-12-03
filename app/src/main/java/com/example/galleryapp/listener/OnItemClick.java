package com.example.galleryapp.listener;

public interface OnItemClick<T> {
    void onClick(T item, int pos);
}
