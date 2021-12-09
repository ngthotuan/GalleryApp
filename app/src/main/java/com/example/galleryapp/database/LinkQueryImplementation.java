package com.example.galleryapp.database;

import static com.example.galleryapp.database.Config.ALBUM_ID_FK;
import static com.example.galleryapp.database.Config.IMAGE_CREATED_DATE;
import static com.example.galleryapp.database.Config.IMAGE_FAVOURITE;
import static com.example.galleryapp.database.Config.IMAGE_ID;
import static com.example.galleryapp.database.Config.IMAGE_ID_FK;
import static com.example.galleryapp.database.Config.IMAGE_MODIFIED_DATE;
import static com.example.galleryapp.database.Config.IMAGE_NAME;
import static com.example.galleryapp.database.Config.IMAGE_PATH;
import static com.example.galleryapp.database.Config.IMAGE_SIZE;
import static com.example.galleryapp.database.Config.IMAGE_TYPE;
import static com.example.galleryapp.database.Config.IMAGE_URI;
import static com.example.galleryapp.database.Config.TABLE_IMAGE;
import static com.example.galleryapp.database.Config.TABLE_LINK;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class LinkQueryImplementation implements QueryContract.LinkQuery {
    private final DatabaseHelper databaseHelper = DatabaseHelper.getInstance();

    @Override
    public void insertImagesToAlbums(List<Picture> pictures, Album album) {
        pictures.forEach(picture -> {
            QueryContract.ImageQuery imageQuery = new ImageQueryImplementation();
            imageQuery.insertPicture(picture, new QueryResponse<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
                    linkQuery.insertLink(picture.getId(), album.getId(), new QueryResponse<Boolean>() {
                        @Override
                        public void onSuccess(Boolean data) {
//                                    Toast.makeText(, "Save Success", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onSuccess: insert imagealbum " + data);
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d("TAG", message);

                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                }
            });
        });
    }


    @Override
    public void insertLink(int imageID, int albumID, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(IMAGE_ID_FK, imageID);
        contentValues.put(ALBUM_ID_FK, albumID);

        try {
            long rowCount = sqLiteDatabase.insertOrThrow(TABLE_LINK, null, contentValues);

            if (rowCount > 0) {
                response.onSuccess(true);
            } else {
                response.onFailure("LinkQuery: Add image to album (database) failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public void getAllPictureInAlbum(int albumID, QueryResponse<List<Picture>> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String asIMG = "img";
        String asLINK = "lk";

        String QUERY = "SELECT * FROM "
                + TABLE_IMAGE + " AS " + asIMG
                + " JOIN "
                + TABLE_LINK + " AS " + asLINK
                + " ON " + asIMG + "." + IMAGE_ID + " = " + asLINK + "." + IMAGE_ID_FK
                + " WHERE " + asLINK + "." + ALBUM_ID_FK + " = " + albumID;

        Cursor cursor = null;

        try {
            List<Picture> pictureList = new ArrayList<>();
            cursor = sqLiteDatabase.rawQuery(QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Picture picture = getPictureFromCursor(cursor);
                    pictureList.add(picture);

                } while (cursor.moveToNext());

                response.onSuccess(pictureList);
            } else {
                response.onSuccess(new ArrayList<>());
            }
        } catch (Exception e) {
            response.onFailure(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
    }

    @Override
    public void deleteLink(int imageID, int albumID, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            long rowAffected = sqLiteDatabase.delete(TABLE_LINK,
                    IMAGE_ID_FK + " =? AND " + ALBUM_ID_FK + " =? ",
                    new String[]{String.valueOf(imageID), String.valueOf(albumID)});

            if (rowAffected > 0) {
                response.onSuccess(true);
            } else {
                response.onFailure("Delete image from album (database) failed");
            }
        } catch (Exception e) {
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    private Picture getPictureFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(IMAGE_ID));
        String name = cursor.getString(cursor.getColumnIndex(IMAGE_NAME));
        String path = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
        long size = cursor.getLong(cursor.getColumnIndex(IMAGE_SIZE));
        String type = cursor.getString(cursor.getColumnIndex(IMAGE_TYPE));
        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(IMAGE_URI)));
        long createdDate = cursor.getLong(cursor.getColumnIndex(IMAGE_CREATED_DATE));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(IMAGE_MODIFIED_DATE));
        int favourite = cursor.getInt(cursor.getColumnIndex(IMAGE_FAVOURITE));

        Picture picture = new Picture(id, name, path, size, type, uri, false, createdDate, modifiedDate, favourite);
        return picture;
    }
}
