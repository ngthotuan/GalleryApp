package com.example.galleryapp.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        RecyclerView imageRecycler;
        ProgressBar load;
        imageRecycler = binding.recycler;
        load = binding.loader;
        imageRecycler.hasFixedSize();
        load.setVisibility(View.VISIBLE);
        List<Picture> pictures = PictureUtil.getPictures(activity, null);
        imageRecycler.setAdapter(new PictureAdapter(R.layout.picture_item, pictures));
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