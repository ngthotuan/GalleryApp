package com.example.galleryapp.utils.cropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.galleryapp.R;

public class CropImageTest extends Activity {
    public static final String TAG = "CropImageActivity";

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    private ImageView mImageView;
    private File mFileTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_crop_screen);

        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mImageView = findViewById(R.id.image);

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }

    }

    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage(String filePath) {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, filePath);
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        Bitmap bitmap;
        String filePath = mFileTemp.getPath();

        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                try {
                    checkPermissionWRITE();
                    checkPermissionREAD();
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage(filePath);
                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }

                break;
            case REQUEST_CODE_CROP_IMAGE:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);

                if (path == null) {
                    return;
                }

                bitmap = BitmapFactory.decodeFile(filePath);

                mImageView.setImageBitmap(bitmap);

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public void grantPermissionREAD() {
        int permissionCheckREAD = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        }
    }

    public void grantPermissionWRITE() {
        int permissionCheckWRITE = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        }
    }

    public void checkPermissionWRITE() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "WRITE EXTERNAL Permission is granted");
        } else {
            Log.v(TAG, "WRITE EXTERNAL Permission denied");
            grantPermissionWRITE();
        }
    }

    public void checkPermissionREAD() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "READ EXTERNAL Permission is granted");
        } else {
            Log.v(TAG, "READ EXTERNAL Permission denied");
            grantPermissionREAD();
        }
    }
}
