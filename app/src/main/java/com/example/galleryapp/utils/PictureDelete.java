package com.example.galleryapp.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PictureDelete {
    // delete is perma delete. we cant recover so we move item
    // to a folder that gallery will not scan

    public void removePic(Context context, File file) {
        String trashPath = Environment.getExternalStorageDirectory() + "/.trash";
        File trashDir = new File(trashPath);
        if(!trashDir.isDirectory()) {
            //create trash dir if it doesn't exist
            trashDir.mkdirs();
        }

        File noMediaFile = new File(trashPath + ".nomedia");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ioException){
                ioException.printStackTrace();
            }
        }

        // move item to hidden dir: /.trash
        String inputPath = file.getPath();
        String inputFile = file.getName();
        moveFile(inputPath, inputFile, trashPath);

    }

    public static boolean deletePic(final Context context, final File file) {
        final String location = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[] {
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, location, selectionArgs);

        if (file.exists()) {
            contentResolver.delete(filesUri, location, selectionArgs);
        }
        return !file.exists();
    }

    public void deleteImg(Context context, File file) {
        // Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Images.Media._ID};

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store DB
        }
        cursor.close();
    }

    public void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            File outputDir = new File (outputPath);
            if (!outputDir.exists()) {
                // create output dir if it doesn't exist
                outputDir.mkdirs();
            }

            fileIn = new FileInputStream(inputPath + inputFile);
            fileOut = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = fileIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, read);
            }

            fileIn.close();
            fileIn = null;

            // write output file
            fileOut.flush();
            fileOut.close();
            fileOut = null;

            // delete original file
            new File(inputPath + inputFile).delete();
        }
        catch (FileNotFoundException fileNotFoundException) {
            Log.e("tag", fileNotFoundException.getMessage());
        }
        catch (Exception exception) {
            Log.e("tag", exception.getMessage());
        }

    }

    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[] {canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

}
