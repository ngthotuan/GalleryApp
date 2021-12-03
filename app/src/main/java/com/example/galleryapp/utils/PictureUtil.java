package com.example.galleryapp.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PictureUtil {

    public static ArrayList<Picture> getPictures(Activity activity, String path) {
        ArrayList<Picture> pictures = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
        };
        Cursor cursor;
        if (path != null) {
            cursor = activity.getContentResolver().query(uri, projection,
                    MediaStore.Images.Media.DATA + " like ? ",
                    new String[]{"%" + path + "%"}, null);
        } else {
            cursor = activity.getContentResolver().query(uri, projection,
                    null, null, null);
        }
        try {
            cursor.moveToFirst();
            do {
                Picture picture = new Picture();
                picture.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                picture.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                picture.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                picture.setCreatedDate(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)));
                picture.setModifiedDate(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)));
                picture.setUri(FileProvider.getUriForFile(activity,
                        activity.getApplicationContext().getPackageName() + ".provider", new File(picture.getPath())));
                picture.setType("jpg png".contains(picture.getPath().substring(picture.getPath().lastIndexOf("."))) ? "image" : "video");
                pictures.add(picture);
            } while (cursor.moveToNext());
            cursor.close();
            Collections.reverse(pictures);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictures;
    }

    public static ArrayList<PictureFolder> getPictureFolders(Activity activity) {
        ArrayList<PictureFolder> pictureFolders = new ArrayList<>();
        PictureFolder pictureFolder = new PictureFolder();
        ArrayList<String> pictureFolderPaths = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String folderPath = dataPath.substring(0, dataPath.lastIndexOf(name));
                if (!pictureFolderPaths.contains(folderPath)) {
                    pictureFolderPaths.add(folderPath);
                    pictureFolder.setPath(folderPath);
                    pictureFolder.setName(folder);
                    pictureFolder.setFirstPicture(FileProvider.getUriForFile(activity,
                            activity.getApplicationContext().getPackageName() + ".provider", new File(dataPath)));
                    pictureFolder.increaseTotalPicture();
                    pictureFolders.add(pictureFolder);
                } else {
                    for (PictureFolder pic : pictureFolders) {
                        if (pic.getPath().equals(folderPath)) {
                            pic.setFirstPicture(FileProvider.getUriForFile(activity,
                                    activity.getApplicationContext().getPackageName() + ".provider", new File(dataPath)));
                            pic.increaseTotalPicture();
                            break;
                        }
                    }
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictureFolders;
    }
}
