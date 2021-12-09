package com.example.galleryapp.database;

import com.example.galleryapp.model.Picture;

import java.util.List;

public interface QueryResponse<T> {
    void onSuccess(T data);
    void onFailure(String message);
}
