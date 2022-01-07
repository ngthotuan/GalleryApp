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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.AlbumQueryImplementation;
import com.example.galleryapp.database.impl.ImageQueryImplementation;
import com.example.galleryapp.database.impl.LinkQueryImplementation;
import com.example.galleryapp.databinding.FragmentFavoritesBinding;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.ui.folder.PictureOfFolderFragment;

import java.io.Serializable;
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

        getAllFavourite();

        return root;
    }

    private void getAllFavourite() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        Album album = albumQuery.getAlbumFavorite();
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        List<Picture> data = linkQuery.getAllPictureInAlbum(album.getId());Log.d("TAG", "favorites size: " + data.size());
        Log.d("TAG", "favorites: " + data);
        Toast.makeText(getContext(), "favorites size: " + data.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
