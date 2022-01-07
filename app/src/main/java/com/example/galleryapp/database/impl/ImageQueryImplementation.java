package com.example.galleryapp.database.impl;

import static com.example.galleryapp.database.util.Config.IMAGE_CREATED_DATE;
import static com.example.galleryapp.database.util.Config.IMAGE_FAVOURITE;
import static com.example.galleryapp.database.util.Config.IMAGE_ID;
import static com.example.galleryapp.database.util.Config.IMAGE_MODIFIED_DATE;
import static com.example.galleryapp.database.util.Config.IMAGE_NAME;
import static com.example.galleryapp.database.util.Config.IMAGE_PATH;
import static com.example.galleryapp.database.util.Config.IMAGE_SIZE;
import static com.example.galleryapp.database.util.Config.IMAGE_TYPE;
import static com.example.galleryapp.database.util.Config.IMAGE_URI;
import static com.example.galleryapp.database.util.Config.TABLE_IMAGE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.model.Picture;

import java.util.ArrayList;
import java.util.List;


public class ImageQueryImplementation implements QueryContract.ImageQuery {
    private static final String TAG = "ImageQuery";

    private final DatabaseHelper databaseHelper = DatabaseHelper.getInstance();

    @Override
    public long insertPicture(Picture picture) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForPicture(picture);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_IMAGE, null, contentValues);

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
    public Picture getPictureByID(int imageID) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Picture picture = null;

        try {
            cursor = sqLiteDatabase.query(TABLE_IMAGE, null,
                    IMAGE_ID + " = ? ",
                    new String[]{String.valueOf(imageID)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                picture = getPictureFromCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
        return picture;
    }

    @Override
    public List<Picture> getAllPicture() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Picture> pictureList = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_IMAGE,
                    null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Picture picture = getPictureFromCursor(cursor);
                    pictureList.add(picture);
                } while (cursor.moveToNext());
            }

            return pictureList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
    }

    @Override
    public boolean deletePicture(int imageID) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            long rowAffected = sqLiteDatabase.delete(TABLE_IMAGE,
                    IMAGE_ID + " =? ",
                    new String[]{ String.valueOf(imageID) });

            if (rowAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public List<Picture> getAllFavourite() {
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance().getReadableDatabase();

        List<Picture> favouriteList = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(TABLE_IMAGE, null,
                    IMAGE_FAVOURITE + " = ? ",
                    new String[]{ String.valueOf(1) },
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Picture picture = getPictureFromCursor(cursor);
                    favouriteList.add(picture);
                } while (cursor.moveToNext());
            }

            return favouriteList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            sqLiteDatabase.close();
        }
    }

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
