package com.example.galleryapp.database;

import static com.example.galleryapp.database.Config.*;

import android.content.ContentValues;

import com.example.galleryapp.model.Picture;


public class ImageQueryImplementation implements QueryContract.ImageQuery {
    private static final String TAG = "ImageQuery";

    private ContentValues getContentValuesForPicture(Picture picture) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(IMAGE_NAME, picture.getName());
        contentValues.put(IMAGE_PATH, picture.getPath());
        contentValues.put(IMAGE_SIZE, picture.getSize());
        contentValues.put(IMAGE_TYPE, picture.getType());
        contentValues.put(IMAGE_URI, picture.getUri().toString());
        contentValues.put(IMAGE_CREATED_DATE, picture.getCreatedDate());
        contentValues.put(IMAGE_MODIFIED_DATE, picture.getModifiedDate());
        contentValues.put(IMAGE_FAVOURITE, picture.getFavourite());

        return contentValues;
    }

    @Override
    public void insertPicture(Picture picture, QueryResponse<Boolean> response) {

    }

    @Override
    public void getPictureByID(QueryResponse<Picture> response) {

    }
}
