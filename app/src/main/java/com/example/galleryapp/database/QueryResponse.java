package com.example.galleryapp.database;

import com.example.galleryapp.model.Picture;

import java.util.List;

public interface QueryResponse<T> {
    void onSuccess(T data);
    void onFailure(String message);
}

/* GUIDE: example
   public void example() {
        int album_id = 2;
        QueryContract.LinkQuery query = new LinkQueryImplementation();
        query.getAllPictureInAlbum(2, new QueryResponse<List<Picture>>() {
            @Override
            public void onSuccess(List<Picture> data) {
                // data is a list picture get from DB
                // do something with it
            }

            @Override
            public void onFailure(String message) {

            }
    });
   }
 */
