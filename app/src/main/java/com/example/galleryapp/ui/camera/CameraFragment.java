package com.example.galleryapp.ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.galleryapp.databinding.FragmentCameraBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class CameraFragment extends Fragment{
    private FragmentCameraBinding binding;
    private Button takeImg;
    private Button saveImg;
    private ImageView imageView;
    static final int CAPTURE_IMAGE_REQUEST=1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        takeImg=binding.takePicture;
        imageView = binding.imgCamera;
        saveImg = binding.saveImage;
        takeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery();
            }
        });
        // do something here
        return root;
    }
    private void captureImage() {
        if(ContextCompat.checkSelfPermission(takeImg.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) takeImg.getContext(),new String[] {Manifest.permission.CAMERA,Manifest.permission
                    .WRITE_EXTERNAL_STORAGE},0);
        }
        else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent,CAPTURE_IMAGE_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        imageView.setImageBitmap(imageBitmap);
//        Uri uri = data.getData();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),imageBitmap, "Title", null);

        Intent editIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);

        editIntent.setData(Uri.parse(path));
        //set directory
        editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "edit");
        editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));

        editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,Color.parseColor("#FFFFFF"));
        editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,new int[]{DsPhotoEditorActivity.TOOL_WARMTH,DsPhotoEditorActivity.TOOL_PIXELATE});
        getActivity().startActivityForResult(editIntent,201);
//        break;
    }
    private void saveToGallery(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        FileOutputStream outputStream= null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath()+ "/MyPics");
        dir.mkdirs();
        String filename= String.format("%d.jpg",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try {
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try {
            outputStream.flush();
            Toast.makeText(getContext(),"Save complete!!!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
