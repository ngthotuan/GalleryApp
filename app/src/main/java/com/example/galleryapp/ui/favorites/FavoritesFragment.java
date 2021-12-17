package com.example.galleryapp.ui.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.ImageQueryImplementation;
import com.example.galleryapp.databinding.FragmentFavoritesBinding;
import com.example.galleryapp.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    FragmentFavoritesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // do something here
        List<Picture> favouriteList = new ArrayList<>();
        QueryContract.ImageQuery imageQuery = new ImageQueryImplementation();

        imageQuery.getAllFavourite(new DatabaseHelper.QueryResponse<List<Picture>>() {
            @Override
            public void onSuccess(List<Picture> data) {
                favouriteList.addAll(data);
            }

            @Override
            public void onFailure(String message) {
                Log.e("Favourite Fragmant", "Error while getting favourited images from DB");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
