package com.example.galleryapp.database;

import static com.example.galleryapp.database.Config.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.galleryapp.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class LinkQueryImplementation implements QueryContract.LinkQuery {
    private DatabaseHelper databaseHelper = DatabaseHelper.getInstance();

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
                response.onFailure("LinkQuery: Add image to album failed");
            }
        } catch (SQLException e) {
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
                + TABLE_LINK  + " AS " + asLINK
                + " ON " + asIMG + "." + IMAGE_ID + " = " + asLINK + "." + IMAGE_ID_FK
                + " WHERE " + asLINK + "." + ALBUM_ID_FK + " = " + albumID;

        Cursor cursor = null;

        try {
            List<Picture> pictureList = new ArrayList<>();
            cursor = sqLiteDatabase.rawQuery(QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(IMAGE_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
                    long size= cursor.getLong(cursor.getColumnIndex(IMAGE_SIZE));
                    String type = cursor.getString(cursor.getColumnIndex(IMAGE_TYPE));
                    Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(IMAGE_URI)));
                    long createdDate = cursor.getLong(cursor.getColumnIndex(IMAGE_CREATED_DATE));
                    long modifiedDate = cursor.getLong(cursor.getColumnIndex(IMAGE_MODIFIED_DATE));
                    int favourite = cursor.getInt(cursor.getColumnIndex(IMAGE_FAVOURITE));

                    // Picture constructor with all attribute above
                    Picture picture = new Picture();

                    pictureList.add(picture);
                    
                } while (cursor.moveToNext());

                response.onSuccess(pictureList);
            } else {
                response.onFailure("There are no image in this album");
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
}
