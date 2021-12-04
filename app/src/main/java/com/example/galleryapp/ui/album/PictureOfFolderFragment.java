package com.example.galleryapp.ui.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.PictureBrowserFragment;
import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentPictureOfFolderBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;
import com.example.galleryapp.utils.PictureUtil;
import com.example.galleryapp.utils.ShareUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PictureOfFolderFragment extends Fragment implements OnItemClick<Picture> {

    private FragmentPictureOfFolderBinding binding;
    private final static int REQUEST_CODE_PHOTO = 50;
    private PictureAdapter pictureAdapter;
    ImageView imageView;
    List<Picture> pictures;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = FragmentPictureOfFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();
        PictureFolder pictureFolder = (PictureFolder) getArguments().getSerializable("pictureFolder");

        RecyclerView imageRecycler;
        TextView txtTitle;

        Button btnTakePhoto;
        ProgressBar load;
        imageView = binding.showingImage;
        btnTakePhoto = binding.btnTakePhoto;
        imageRecycler = binding.recycler;
        load = binding.loader;

        imageRecycler.hasFixedSize();
        load.setVisibility(View.VISIBLE);
        pictures = PictureUtil.getPictures(activity, pictureFolder.getPath());
        pictureAdapter = new PictureAdapter(R.layout.picture_item_gird, pictures, this);
        pictureAdapter.notifyDataSetChanged();

        imageRecycler.setAdapter(pictureAdapter);
        imageRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));




        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult();
//                onLaunchCamera(view);
            }
        });


//        load.setVisibility(View.GONE);

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
                        Log.e("TAG", "onActivityResult: " );
//                        new ImageSave(getContext()).save(photo);
                        imageView.setImageBitmap(photo);


                    }
                }
            });




    private void print(String str){
        Log.d("TAG", str);
    }
    public void openSomeActivityForResult() {
//
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        startActivity(intent);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.image_view_option_drawer,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.imgShare:{
                ShareUtils.shareImage(getContext(),pictures.get(0));
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Picture item, int pos) {
//        Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pics) {
        PictureBrowserFragment browser = PictureBrowserFragment.newInstance(pics,position,getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position+"picture")
                .add(R.id.nav_host_fragment_content_main, browser)
                .addToBackStack(null)
                .commit();


    }


}