package com.example.galleryapp.database;

import android.net.Uri;

import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;

import java.util.List;

public class QueryContract {
    public interface ImageQuery {
        long insertPicture(Picture picture);

        Picture getPictureByID(int imageID);

        List<Picture> getAllPicture();

        boolean deletePicture(int imageID);

        List<Picture> getAllFavourite();
    }

    public interface AlbumQuery {
        long insertAlbum(Album album);

        Album getAlbumByID(int albumID);

        List<Album> getAllAlbum();

        int getAlbumCount();

        Album getAlbumByName(String albumName);

        default Album getAlbumFavorite() {
            return getAlbumByName("Favorites");
        }

        default Album getAlbumHidden() {
            return getAlbumByName("Hidden");
        }


        boolean deleteAlbum(int albumID);
    }

    public interface LinkQuery {
        void insertImagesToAlbums(List<Picture> pictures, Album album);

        long insertLink(int imageID, int albumID);

        List<Picture> getAllPictureInAlbum(int albumID);

        boolean deleteLink(int imageID, int albumID);
        long countImage(int albumId);
        Uri getFirstImage(int albumId);
    }
}
