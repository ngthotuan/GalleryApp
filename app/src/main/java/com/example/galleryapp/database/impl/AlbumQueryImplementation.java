package com.example.galleryapp.database.impl;

import static com.example.galleryapp.database.util.Config.ALBUM_ID;
import static com.example.galleryapp.database.util.Config.ALBUM_NAME;
import static com.example.galleryapp.database.util.Config.TABLE_ALBUM;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.model.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlbumQueryImplementation implements QueryContract.AlbumQuery {

    DatabaseHelper databaseHelper = DatabaseHelper.getInstance();

    @Override
    public long insertAlbum(Album album) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ALBUM_NAME, album.getName());

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_ALBUM, null, contentValues);

            if (id > 0) {
                return id;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            return -1;
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public Album getAlbumByID(int albumID) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Album album = null;

        try {
            cursor = sqLiteDatabase.query(TABLE_ALBUM, null,
                    ALBUM_ID + " = ? ",
                    new String[]{String.valueOf(albumID)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(ALBUM_ID));
                String name = cursor.getString(cursor.getColumnIndex(ALBUM_NAME));

                album = new Album(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }

        return album;
    }

    @Override
    public List<Album> getAllAlbum() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Album> albumList = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(TABLE_ALBUM,
                    null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ALBUM_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ALBUM_NAME));
                    if(!Objects.equals(name, "Favorites") && !Objects.equals(name, "Hidden")) {
                        Album album = new Album(id, name);
                        albumList.add(album);
                    }
                } while (cursor.moveToNext());

            }
            return albumList;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
    }

    @Override
    public Album getAlbumByName(String albumName) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Album album = null;

        try {
            cursor = sqLiteDatabase.query(TABLE_ALBUM, null,
                    ALBUM_NAME + " = ? ",
                    new String[]{albumName},
                    null, null, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(ALBUM_ID));
                String name = cursor.getString(cursor.getColumnIndex(ALBUM_NAME));

                return new Album(id, name);
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
    }

    @Override
    public int getAlbumCount() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor mCount= sqLiteDatabase.rawQuery("select count(*) from " + TABLE_ALBUM, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;
    }

    @Override
    public boolean deleteAlbum(int albumID) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            long rowAffected = sqLiteDatabase.delete(TABLE_ALBUM,
                    ALBUM_ID + " =? ",
                    new String[]{String.valueOf(albumID)});

            if (rowAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
        return false;
    }
}
