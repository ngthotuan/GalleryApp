package com.example.galleryapp.database;

import android.app.DownloadManager;

import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;

import java.util.List;

public class QueryContract {
    public interface ImageQuery {
        void insertPicture(Picture picture, QueryResponse<Boolean> response);
        void getPictureByID(int imageID, QueryResponse<Picture> response);
        void getAllPicture(QueryResponse<List<Picture>> response);
        void deletePicture(int imageID, QueryResponse<Boolean> response);
    }

    public interface AlbumQuery {
        void insertAlbum(Album album, QueryResponse<Boolean> response);
        void getAlbumByID(int albumID, QueryResponse<Album> response);
        void getAllAlbum(QueryResponse<List<Album>> response);
        void deleteAlbum(int albumID, QueryResponse<Boolean> response);
    }

    public interface LinkQuery {
        void insertLink(int imageID, int albumID, QueryResponse<Boolean> response);
        void getAllPictureInAlbum(int albumID, QueryResponse<List<Picture>> response);
        void deleteLink(int imageID, int albumID, QueryResponse<Boolean> response);
    }
}
