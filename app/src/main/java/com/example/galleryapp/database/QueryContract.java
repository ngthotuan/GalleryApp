package com.example.galleryapp.database;

import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;

import java.util.List;

public class QueryContract {
    public interface ImageQuery {
        void insertPicture(Picture picture, DatabaseHelper.QueryResponse<Boolean> response);

        void getPictureByID(int imageID, DatabaseHelper.QueryResponse<Picture> response);

        void getAllPicture(DatabaseHelper.QueryResponse<List<Picture>> response);

        void deletePicture(int imageID, DatabaseHelper.QueryResponse<Boolean> response);
    }

    public interface AlbumQuery {
        void insertAlbum(Album album, DatabaseHelper.QueryResponse<Boolean> response);

        void getAlbumByID(int albumID, DatabaseHelper.QueryResponse<Album> response);

        void getAllAlbum(DatabaseHelper.QueryResponse<List<Album>> response);

        void deleteAlbum(int albumID, DatabaseHelper.QueryResponse<Boolean> response);
    }

    public interface LinkQuery {
        void insertImagesToAlbums(List<Picture> pictures, Album album);

        void insertLink(int imageID, int albumID, DatabaseHelper.QueryResponse<Boolean> response);

        void getAllPictureInAlbum(int albumID, DatabaseHelper.QueryResponse<List<Picture>> response);

        void deleteLink(int imageID, int albumID, DatabaseHelper.QueryResponse<Boolean> response);
    }
}
