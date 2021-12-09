package com.example.galleryapp.ui.folder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.adapter.PictureFolderAdapter;
import com.example.galleryapp.databinding.FragmentFolderBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.PictureFolder;
import com.example.galleryapp.utils.PictureUtil;

import java.util.List;

public class FolderFragment extends Fragment implements OnItemClick<PictureFolder> {
    FragmentFolderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        RecyclerView imageRecycler;

        imageRecycler = binding.recyclerFolder;
        imageRecycler.hasFixedSize();
        List<PictureFolder> pictures = PictureUtil.getPictureFolders(activity);
        imageRecycler.setAdapter(new PictureFolderAdapter(pictures, R.layout.folder_item, this));
        imageRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));

        return root;

        // do something here

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment_content_main, new EmptyFragment());
        ft.commit();
    }

    @Override
    public void onClick(PictureFolder pictureFolder, int pos) {
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PictureOfFolderFragment llf = new PictureOfFolderFragment();
        Bundle args = new Bundle();
        args.putSerializable("pictureFolder", pictureFolder);
        llf.setArguments(args);
        ft.replace(R.id.nav_host_fragment_content_main, llf);
        ft.commit();
    }


    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<PictureFolder> pics) {

    }
}
