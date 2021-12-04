package com.example.galleryapp.ui.album;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.adapter.PictureFolderAdapter;
import com.example.galleryapp.databinding.FragmentAlbumBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.model.PictureFolder;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

public class AlbumFragment extends Fragment implements OnItemClick<PictureFolder> {
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
        Log.e("TAG", "onCreateView: " + pictures.size());
        imageRecycler.setAdapter(new PictureFolderAdapter(pictures, R.layout.folder_item, this));
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

    @Override
    public void onClick(PictureFolder pictureFolder, int pos) {
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PictureOfFolderFragment llf = new PictureOfFolderFragment();
        Bundle args = new Bundle();
        args.putSerializable("pictureFolder", pictureFolder);
        llf.setArguments(args);
        ft.replace(R.id.nav_host_fragment_content_main, llf).addToBackStack(null);
        Log.e("TAG", "folderImageClick: ");
        ft.commit();
    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pics) {

    }
}
