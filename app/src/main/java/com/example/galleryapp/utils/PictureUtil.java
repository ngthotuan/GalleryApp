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
import java.util.List;

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
                picture.setType("image");
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
        ArrayList<String> pictureFolderPaths = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        try {
            while (cursor.moveToNext()) {
                PictureFolder pictureFolder = new PictureFolder();
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

    public static ArrayList<PictureFolder> getPicturePaths(Activity activity) {
        ArrayList<PictureFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = activity.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                PictureFolder folds = new PictureFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = dataPath.substring(0, dataPath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setName(folder);
                    folds.setFirst(dataPath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.increaseTotalPicture();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
//                            picFolders.get(i).setFirstPicture(FileProvider.getUriForFile(activity,
//                                    activity.getApplicationContext().getPackageName() + ".provider",
//                                    new File(dataPath)));
                            picFolders.get(i).setFirst(dataPath);
                            picFolders.get(i).increaseTotalPicture();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(int i = 0;i < picFolders.size();i++){
//            Log.d("picture folders",picFolders.get(i).getFolderName()+" and path = "+picFolders.get(i).getPath()+" "+picFolders.get(i).getNumberOfPics());
//        }

        //reverse order ArrayList
       /* ArrayList<imageFolder> reverseFolders = new ArrayList<>();

        for(int i = picFolders.size()-1;i > reverseFolders.size()-1;i--){
            reverseFolders.add(picFolders.get(i));
        }*/

        return picFolders;
    }

    public static List<Picture> updateFavorites(List<Picture> pictures, List<Picture> favorites) {
        for (Picture picture : pictures) {
            for (Picture favorite : favorites) {
                if (picture.getPath().equals(favorite.getPath())) {
                    picture.setFavourite(1);
                    break;
                }
            }
        }
        return pictures;
    }

    public static List<Picture> updateHidden(List<Picture> pictures, List<Picture> hidden) {
        for (Picture picture : pictures) {
            for (Picture h : hidden) {
                if (picture.getPath().equals(h.getPath())) {
                    picture.setHidden(true);
                    break;
                }
            }
        }
        return pictures;
    }
    public static List<Picture> updateLocked(List<Picture> pictures, List<Picture> locked) {
        for (Picture picture : pictures) {
            for (Picture h : locked) {
                if (picture.getPath().equals(h.getPath())) {
                    picture.setLocked(true);
                    break;
                }
            }
        }
        return pictures;
    }

}
