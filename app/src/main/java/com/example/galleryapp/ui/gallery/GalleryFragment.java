package com.example.galleryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.listener.PictureListener;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.PictureUtil;

import java.util.Comparator;
import java.util.List;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private RecyclerView rvPictures;
    private List<Picture> pictures;
    private Button btnChangeView, btnSort;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private boolean isGridView = true;
    private boolean sortDecreasing = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        rvPictures = binding.rvPictures;
        btnChangeView = binding.btnChangeView;
        btnSort = binding.btnSort;

        pictures = PictureUtil.getPictures(activity, null);
        adapter = getPictureAdapter(isGridView);
        layoutManager = getLayoutManger(isGridView);

        rvPictures.hasFixedSize();
        rvPictures.setAdapter(adapter);
        rvPictures.setLayoutManager(layoutManager);

        btnChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isGridView = !isGridView;
                layoutManager = getLayoutManger(isGridView);
                adapter = getPictureAdapter(isGridView);
                rvPictures.setAdapter(adapter);
                rvPictures.setLayoutManager(layoutManager);
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Comparator<Picture> comparator = (p1, p2) -> sortDecreasing
                        ? (int) (p1.getCreatedDate() - p2.getCreatedDate())
                        : (int) -(p1.getCreatedDate() - p2.getCreatedDate());
                pictures.sort(comparator);
                adapter.notifyDataSetChanged();
                sortDecreasing = !sortDecreasing;
            }
        });

        return root;
    }

    @NonNull
    private PictureAdapter getPictureAdapter(boolean isGridView) {
        int resource = isGridView ? R.layout.picture_item_gird : R.layout.picture_item_list;
        PictureAdapter adapter = new PictureAdapter(resource, pictures);
        adapter.setListener(new PictureListener() {
            @Override
            public void onClick(Picture picture, int pos) {
                Toast.makeText(getContext(), picture.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return adapter;
    }

    private RecyclerView.LayoutManager getLayoutManger(boolean isGridView) {
        return isGridView ? new GridLayoutManager(getContext(), 4)
                : new LinearLayoutManager(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}