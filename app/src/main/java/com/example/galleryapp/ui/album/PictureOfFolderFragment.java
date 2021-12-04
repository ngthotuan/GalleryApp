package com.example.galleryapp.ui.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentPictureOfFolderBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PictureOfFolderFragment extends Fragment implements OnItemClick<Picture> {

    private FragmentPictureOfFolderBinding binding;
    private final static int REQUEST_CODE_PHOTO = 50;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPictureOfFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();
        PictureFolder pictureFolder = (PictureFolder) getArguments().getSerializable("pictureFolder");

        RecyclerView imageRecycler;
        TextView txtTitle;
        Button btnTakePhoto;
        ProgressBar load;
        btnTakePhoto = binding.btnTakePhoto;
        imageRecycler = binding.recycler;
        load = binding.loader;
        imageRecycler.hasFixedSize();
        load.setVisibility(View.VISIBLE);
        List<Picture> pictures = PictureUtil.getPictures(activity, pictureFolder.getPath());
        imageRecycler.setAdapter(new PictureAdapter(R.layout.picture_item_gird, pictures, this));
        imageRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));




        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult();
            }
        });


//        load.setVisibility(View.GONE);

        return root;
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Log.e("TAG", "onActivityResult: "+ photo );
                        ;
                    }
                }
            });


    public void openSomeActivityForResult() {
        Intent [] intentArray;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        intentArray = new Intent[]{takePictureIntent,takeVideoIntent};
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose an action");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        someActivityResultLauncher.launch(chooserIntent);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Picture item, int pos) {
        Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();
    }
}