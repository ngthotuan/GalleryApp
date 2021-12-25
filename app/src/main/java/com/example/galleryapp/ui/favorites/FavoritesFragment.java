package com.example.galleryapp.ui.favorites;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.ImageQueryImplementation;
import com.example.galleryapp.databinding.FragmentFavoritesBinding;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private RecyclerView rvFav;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private List<Picture> pictureList;
    private TextView txtSort;
    private Spinner spSort;
    private boolean isGridView = true;
    private List<String> sortFields;
    private String sortField = "";
    private String sortType = "";
    private LinearLayout filters;
    private boolean showFilter = false;
    private final List<Picture> listLongImage = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

//        rvFav = binding.rvFav;
//        rvFav.hasFixedSize();
//        txtSort = binding.txtSort;
//        spSort = binding.spSort;
//        filters = binding.filters;

        // do something here

        // get favourite pic from DB
        List<Picture> favouriteList = new ArrayList<>();
        getAllFavourite(favouriteList);

        return root;
    }

    private void getAllFavourite(List<Picture> pictureList) {
        List<Picture> favouriteList = new ArrayList<>();
        QueryContract.ImageQuery imageQuery = new ImageQueryImplementation();

        imageQuery.getAllFavourite(new DatabaseHelper.QueryResponse<List<Picture>>() {
            @Override
            public void onSuccess(List<Picture> data) {
                favouriteList.clear();
                favouriteList.addAll(data);
            }

            @Override
            public void onFailure(String message) {
                Log.e("Favourite Fragmant", "Error while getting favourited images from DB");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
