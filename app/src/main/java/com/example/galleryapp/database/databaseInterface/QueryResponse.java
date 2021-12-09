package com.example.galleryapp.database.databaseInterface;

public interface QueryResponse<T> {
    void onSuccess(T data);
    void onFailure(String message);
}
