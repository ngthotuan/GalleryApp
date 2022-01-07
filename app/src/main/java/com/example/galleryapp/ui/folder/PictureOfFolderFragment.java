package com.example.galleryapp.ui.folder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.example.galleryapp.ui.showImage.PictureShowFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PictureOfFolderFragment extends Fragment implements OnItemClick<Picture> {

    private FragmentPictureOfFolderBinding binding;
    private PictureAdapter pictureAdapter;
    ImageView imageView;
    List<Picture> pictures;
    List<Picture> longClick = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPictureOfFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        List<Picture> pictures = (List<Picture>) getArguments().getSerializable("pictures");
        RecyclerView imageRecycler;
        imageRecycler = binding.recycler;

        imageRecycler.hasFixedSize();
        pictureAdapter = new PictureAdapter(R.layout.picture_item_gird, pictures, this, longClick);

        imageRecycler.setAdapter(pictureAdapter);
        imageRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));

        return root;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(photo);


                    }
                }
            });


    private void print(String str) {
        Log.d("TAG", str);
    }

    public void openSomeActivityForResult() {
//
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        startActivity(intent);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Picture item, int pos) {

    }


    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pictures) {
        PictureShowFragment browser = PictureShowFragment.newInstance(pictures, position);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
//                .addSharedElement(holder.picture, holder.picName.getText().toString())
                .replace(R.id.nav_host_fragment_content_main, browser)
                .commit();
    }


}