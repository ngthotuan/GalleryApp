package com.example.galleryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.galleryapp.model.Picture;

public class DatabaseQuery {
    private Context context;

    public DatabaseQuery(Context context) {
        this.context = context;
    }

    public long insertPicture(Picture picture) {
        long id = -1;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.COLUMN_IMAGE_NAME, picture.getName());
        contentValues.put(Config.COLUMN_IMAGE_PATH, picture.getPath());
        contentValues.put(Config.COLUMN_IMAGE_SIZE, picture.getSize());
        contentValues.put(Config.COLUMN_IMAGE_TYPE, picture.getType());
        contentValues.put(Config.COLUMN_IMAGE_URI, picture.getUri().toString());
        contentValues.put(Config.COLUMN_IMAGE_CREATED_DATE, picture.getCreatedDate());
        contentValues.put(Config.COLUMN_IMAGE_MODIFIED_DATE, picture.getModifiedDate());
        //contentValues.put(Config.COLUMN_IMAGE_ALBUM, picture.getAlbum());
        //contentValues.put(Config.COLUMN_IMAGE_FAVOURITE, picture.getFavourite());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_IMAGE, null, contentValues);
        } catch (SQLException e) {
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }
}
