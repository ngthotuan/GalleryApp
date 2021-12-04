package com.example.galleryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.DateUtil;
import com.example.galleryapp.utils.PictureUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryFragment extends Fragment implements OnItemClick<Picture> {
    private FragmentGalleryBinding binding;
    private RecyclerView rvPictures;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private List<Picture> pictures;
    private Button btnChangeView, btnSort;
    private EditText edtSearch;
    private Spinner spSort;
    private boolean isGridView = true;
    private boolean sortDecreasing = true;
    private List<String> sortFields;
    private String sortField = "";
    private String sortType = "desc";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        rvPictures = binding.rvPictures;
        btnChangeView = binding.btnChangeView;
        btnSort = binding.btnSort;
        edtSearch = binding.edtSearch;
        spSort = binding.spSort;

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
                adapter.notifyDataSetChanged();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Log.d("TAG", "sortField= " + sortField + " sortType= " + sortType);
                Comparator<Picture> comparator = getComparator();
                pictures.sort(comparator);
                reversedSortType();
                adapter.notifyDataSetChanged();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence keyword, int i, int i1, int i2) {
                pictures = PictureUtil.getPictures(activity, null);
                if (keyword.length() > 0) {
                    pictures = pictures.stream()
                            .filter(picture -> picture.getName().toLowerCase()
                                    .contains(keyword.toString().toLowerCase())
                                    || DateUtil.getDate(picture.getCreatedDate()).contains(keyword))
                            .collect(Collectors.toList());
                }
                adapter.setPictures(pictures);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sortFields = getSortFields();
        spSort.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, sortFields));

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortField = sortFields.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    @NonNull
    private PictureAdapter getPictureAdapter(boolean isGridView) {
        int resource = isGridView ? R.layout.picture_item_gird : R.layout.picture_item_list;
        return new PictureAdapter(resource, pictures, this);
    }

    private RecyclerView.LayoutManager getLayoutManger(boolean isGridView) {
        return isGridView ? new GridLayoutManager(getContext(), 4)
                : new LinearLayoutManager(getContext());
    }

    private List<String> getSortFields() {
        return Arrays.asList(
                getResources().getString(R.string.name),
                getResources().getString(R.string.size),
                getResources().getString(R.string.created_date)
        );
    }

    private Comparator<Picture> getComparator() {
        Comparator<Picture> comparator;
        if (sortField.equals(getResources().getString(R.string.size))) {
            comparator = Comparator.comparing(Picture::getSize);
        } else if (sortField.equals(getResources().getString(R.string.name))) {
            comparator = Comparator.comparing(Picture::getName);
        } else {
            comparator = Comparator.comparing(Picture::getCreatedDate);
        }

        if (sortType.equals("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private void reversedSortType() {
        if (sortType.equals("desc")) {
            sortType = "asc";
        } else {
            sortType = "desc";
        }
        btnSort.setText(sortType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Picture item, int pos) {
        Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();
    }
}