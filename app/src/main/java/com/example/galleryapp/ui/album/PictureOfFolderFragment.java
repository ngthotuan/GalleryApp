package com.example.galleryapp.ui.album;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentAlbumBinding;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.databinding.FragmentPictureOfFolderBinding;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PictureOfFolderFragment extends Fragment {

    private FragmentPictureOfFolderBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPictureOfFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();
        PictureFolder pictureFolder = (PictureFolder) getArguments().getSerializable("pictureFolder");
        this.getActivity().setTitle(pictureFolder.getName());

//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this.getActivity(), callback);
        RecyclerView imageRecycler;
        TextView txtTitle;
        ProgressBar load;
        imageRecycler = binding.recycler;
//        txtTitle = binding.txtTitle;
//        txtTitle.setText(pictureFolder.getName());
        load = binding.loader;
        imageRecycler.hasFixedSize();
        load.setVisibility(View.VISIBLE);
        List<Picture> pictures = PictureUtil.getPictures(activity, pictureFolder.getPath());
        imageRecycler.setAdapter(new PictureAdapter(R.layout.picture_item_gird, pictures));
        imageRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        load.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}