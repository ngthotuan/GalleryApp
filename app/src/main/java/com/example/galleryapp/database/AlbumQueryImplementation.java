package com.example.galleryapp.database;

import static com.example.galleryapp.database.Config.ALBUM_ID;
import static com.example.galleryapp.database.Config.ALBUM_NAME;
import static com.example.galleryapp.database.Config.TABLE_ALBUM;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.galleryapp.model.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumQueryImplementation implements QueryContract.AlbumQuery {

    DatabaseHelper databaseHelper = DatabaseHelper.getInstance();

    @Override
    public void insertAlbum(Album album, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ALBUM_NAME, album.getName());

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_ALBUM, null, contentValues);

            if (id > 0) {
                response.onSuccess(true);
                album.setId((int) id);
            } else {
                response.onFailure("Insert album to database failed");
            }
        } catch (SQLException e) {
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public void getAlbumByID(int albumID, QueryResponse<Album> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor= null;
        Album album = null;

        try {
            cursor = sqLiteDatabase.query(TABLE_ALBUM, null,
                    ALBUM_ID + " = ? ",
                    new String[] { String.valueOf(albumID) },
                    null, null, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(ALBUM_ID));
                String name = cursor.getString(cursor.getColumnIndex(ALBUM_NAME));

                album = new Album(id, name);

                response.onSuccess(album);
            } else {
                response.onFailure("Get album from database failed");
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
    public void getAllAlbum(QueryResponse<List<Album>> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Album> albumList = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_ALBUM,
                    null, null, null,null,null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ALBUM_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ALBUM_NAME));

                    Album album = new Album(id, name);
                    albumList.add(album);
                } while (cursor.moveToNext());

            }
            response.onSuccess(albumList);
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
    public void deleteAlbum(int albumID, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            long rowAffected =  sqLiteDatabase.delete(TABLE_ALBUM,
                    ALBUM_ID + " =? ",
                    new String[] { String.valueOf(albumID) });

            if (rowAffected > 0) {
                response.onSuccess(true);
            } else {
                response.onFailure("Delete album from database failed");
            }
        } catch (Exception e) {
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }
}
