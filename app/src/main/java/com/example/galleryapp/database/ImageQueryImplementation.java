package com.example.galleryapp.database;

import static com.example.galleryapp.database.Config.*;

import android.content.ContentValues;

import com.example.galleryapp.model.Picture;


public class ImageQueryImplementation implements QueryContract.ImageQuery {
    private static final String TAG = "ImageQuery";

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

    @Override
    public void insertPicture(Picture picture, QueryResponse<Boolean> response) {

    }

    @Override
    public void getPictureByID(QueryResponse<Picture> response) {

    }
}
//package com.example.galleryapp.database;
//
//import static com.example.galleryapp.database.Config.IMAGE_CREATED_DATE;
//import static com.example.galleryapp.database.Config.IMAGE_FAVOURITE;
//import static com.example.galleryapp.database.Config.IMAGE_MODIFIED_DATE;
//import static com.example.galleryapp.database.Config.IMAGE_NAME;
//import static com.example.galleryapp.database.Config.IMAGE_PATH;
//import static com.example.galleryapp.database.Config.IMAGE_SIZE;
//import static com.example.galleryapp.database.Config.IMAGE_TYPE;
//import static com.example.galleryapp.database.Config.IMAGE_URI;
//import static com.example.galleryapp.database.Config.TABLE_IMAGE;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.galleryapp.model.Picture;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class ImageQueryImplementation implements QueryContract.ImageQuery {
//    private static final String TAG = "ImageQuery";
//
//    public long insertPicture(Picture picture) {
//        long id = -1;
//
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(IMAGE_NAME, picture.getName());
//        contentValues.put(IMAGE_PATH, picture.getPath());
//        contentValues.put(IMAGE_SIZE, picture.getSize());
//        contentValues.put(IMAGE_TYPE, picture.getType());
//        contentValues.put(IMAGE_URI, picture.getUri().toString());
//        contentValues.put(IMAGE_CREATED_DATE, picture.getCreatedDate());
//        contentValues.put(IMAGE_MODIFIED_DATE, picture.getModifiedDate());
//        contentValues.put(IMAGE_FAVOURITE, picture.getFavourite());
//
//        try {
//            id = sqLiteDatabase.insertOrThrow(TABLE_IMAGE, null, contentValues);
//        } catch (SQLException e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//            Log.e(TAG, "Error while inserting data");
//            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
//        } finally {
//            sqLiteDatabase.close();
//        }
//
//        return id;
//    }
//
//    public List<Picture> getAllPicture() {
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
//
//        Cursor cursor = null;
//
//        try {
//            cursor = sqLiteDatabase.query(TABLE_IMAGE,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    List<Picture> pictureList = new ArrayList<>();
//
//                    do {
//                        String path = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
//                        String name = cursor.getString(cursor.getColumnIndex(IMAGE_NAME));
//                        long size   = cursor.getLong(cursor.getColumnIndex(IMAGE_SIZE));
//                        String type = cursor.getString(cursor.getColumnIndex(IMAGE_TYPE));
//                        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(IMAGE_URI))) ;
//                        long createdDate  = cursor.getLong(cursor.getColumnIndex(IMAGE_CREATED_DATE));
//                        long modifiedDate = cursor.getLong(cursor.getColumnIndex(IMAGE_MODIFIED_DATE));
//                        String favourite = cursor.getString(cursor.getColumnIndex(IMAGE_FAVOURITE));
//
//                        //Picture constructor
//                        //pictureList.add(new Picture())
//                    } while (cursor.moveToNext());
//
//                    return pictureList;
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//            Log.e(TAG, "Error while getting all pictures data");
//            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            sqLiteDatabase.close();
//        }
//
//        return Collections.emptyList();
//    }
//
//    public Picture getPictureByPath(String picPath) {
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
//
//        Cursor cursor = null;
//        Picture picture = null;
//
//        try {
//            cursor = sqLiteDatabase.query(TABLE_IMAGE,
//                    null,
//                    IMAGE_PATH + " = ?",
//                    new String[] { String.valueOf(picPath) },
//                    null,
//                    null,
//                    null);
//
//            if (cursor.moveToFirst()) {
//                String path = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
//                String name = cursor.getString(cursor.getColumnIndex(IMAGE_NAME));
//                long size   = cursor.getLong(cursor.getColumnIndex(IMAGE_SIZE));
//                String type = cursor.getString(cursor.getColumnIndex(IMAGE_TYPE));
//                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(IMAGE_URI))) ;
//                long createdDate  = cursor.getLong(cursor.getColumnIndex(IMAGE_CREATED_DATE));
//                long modifiedDate = cursor.getLong(cursor.getColumnIndex(IMAGE_MODIFIED_DATE));
//                String favourite = cursor.getString(cursor.getColumnIndex(IMAGE_FAVOURITE));
//
//                //Picture constructor
//                //pictureList.add(new Picture())
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//            Log.e(TAG, "Error while getting 1 picture data");
//            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            sqLiteDatabase.close();
//        }
//
//        return picture;
//    }
//
//    public long updatePicture(Picture picture) {
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//
//        long rowAffected = 0;
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(IMAGE_NAME, picture.getName());
//        contentValues.put(IMAGE_PATH, picture.getPath());
//        contentValues.put(IMAGE_SIZE, picture.getSize());
//        contentValues.put(IMAGE_TYPE, picture.getType());
//        contentValues.put(IMAGE_URI, picture.getUri().toString());
//        contentValues.put(IMAGE_CREATED_DATE, picture.getCreatedDate());
//        contentValues.put(IMAGE_MODIFIED_DATE, picture.getModifiedDate());
//        contentValues.put(IMAGE_FAVOURITE, picture.getFavourite());
//
//        try {
//            rowAffected = sqLiteDatabase.update(TABLE_IMAGE, contentValues,
//                    IMAGE_PATH + " = ?",
//                    new String[] { String.valueOf(picture.getPath())});
//        } catch (SQLException e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//            Log.e(TAG, "Error while updating picture data in database");
//        } finally {
//            sqLiteDatabase.close();
//        }
//
//        return rowAffected;
//    }
//
//    public long deletePictureByPath(String picPath) {
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//
//        long deletedRowCount = -1;
//
//        try {
//            deletedRowCount = sqLiteDatabase.delete(TABLE_IMAGE,
//                    IMAGE_PATH + " = ? ",
//                    new String[] { String.valueOf(picPath) });
//        } catch (SQLException e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//            Log.e(TAG, "Error while deleting picture data from database");
//        } finally {
//            sqLiteDatabase.close();
//        }
//
//        return deletedRowCount;
//    }
//}
