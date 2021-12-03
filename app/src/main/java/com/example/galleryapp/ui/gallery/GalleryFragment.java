package com.example.galleryapp.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.listener.PictureListener;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private RecyclerView rvPictures;
    private List<Picture> pictures;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        pictures = PictureUtil.getPictures(activity, null);
        rvPictures = binding.rvPictures;
        rvPictures.hasFixedSize();
        PictureAdapter adapter = new PictureAdapter(R.layout.picture_item, pictures);
        adapter.setListener(new PictureListener() {
            @Override
            public void onClick(Picture picture, int pos) {
                Toast.makeText(getContext(), picture.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rvPictures.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rvPictures.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}