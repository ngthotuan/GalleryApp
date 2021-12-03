package com.example.galleryapp.ui.album;

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
import com.example.galleryapp.adapter.pictureFolderAdapter;
import com.example.galleryapp.databinding.FragmentAlbumBinding;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

public class AlbumFragment extends Fragment {
    FragmentAlbumBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        RecyclerView imageRecycler;
        ProgressBar load;
        imageRecycler = binding.recyclerAlbum;
        load = binding.loader;
        imageRecycler.hasFixedSize();
        load.setVisibility(View.VISIBLE);
        List<PictureFolder> pictures = PictureUtil.getPictureFolders(activity);
        imageRecycler.setAdapter(new pictureFolderAdapter(pictures,this.getContext(),R.layout.folder_item));
        imageRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        load.setVisibility(View.GONE);

        return root;

        // do something here

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
