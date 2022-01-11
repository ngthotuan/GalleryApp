package com.example.galleryapp.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;

import java.util.ArrayList;
import java.util.List;

import static com.example.galleryapp.database.util.Config.*;

public class LinkQueryImplementation implements QueryContract.LinkQuery {
    private final DatabaseHelper databaseHelper = DatabaseHelper.getInstance();

    @Override
    public void insertImagesToAlbums(List<Picture> pictures, Album album) {
        pictures.forEach(picture -> {
            QueryContract.ImageQuery imageQuery = new ImageQueryImplementation();
            QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();

            long id = imageQuery.insertPicture(picture);
            if (id > 0) {
                linkQuery.insertLink((int) id, album.getId());
            }
        });
    }


    @Override
    public long insertLink(int imageID, int albumID) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(IMAGE_ID_FK, imageID);
        contentValues.put(ALBUM_ID_FK, albumID);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_LINK, null, contentValues);

            if (id > 0) {
                return id;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public List<Picture> getAllPictureInAlbum(int albumID) {
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


            }
            return pictureList;
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
    public boolean deleteLink(int imageID, int albumID) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            Log.d("TAG", "deleteLink " + imageID + " " + albumID);
            long rowAffected = sqLiteDatabase.delete(TABLE_LINK,
                    IMAGE_ID_FK + " =? AND " + ALBUM_ID_FK + " =? ",
                    new String[]{String.valueOf(imageID), String.valueOf(albumID)});

            if (rowAffected > 0) {
                Log.d("TAG", "deleteLink OK");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }

        return false;
    }

    @Override
    public long countImage(int albumId) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_LINK,
                ALBUM_ID_FK +" =? ", new String[] {String.valueOf(albumId)});

    }

    @Override
    public Uri getFirstImage(int albumId) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = null;
        Uri uri = null;
        int idImage ;
        try {
            cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TABLE_LINK + " WHERE " +ALBUM_ID_FK +" =? ",new String[]{String.valueOf(albumId)});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                idImage = cursor.getInt(cursor.getColumnIndex(IMAGE_ID_FK));
                QueryContract.ImageQuery imageQuery = new ImageQueryImplementation();
                Picture picture = imageQuery.getPictureByID(idImage);
                uri = picture.getUri();
            }
            return uri;
        }finally {
            cursor.close();
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
