package com.example.galleryapp.database;

import android.app.DownloadManager;

import com.example.galleryapp.model.Picture;

import java.util.List;

public class QueryContract {
    public interface ImageQuery {
        void insertPicture(Picture picture, QueryResponse<Boolean> response);
        void getPictureByID(QueryResponse<Picture> response);

    }

    public interface AlbumQuery {

    }

    public interface LinkQuery {

    }
}
