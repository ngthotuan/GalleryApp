package com.example.galleryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.PictureBrowserFragment;
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
import java.util.Random;
import java.util.stream.Collectors;

public class GalleryFragment extends Fragment implements OnItemClick<Picture> {
    private FragmentGalleryBinding binding;
    private RecyclerView rvPictures;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private List<Picture> pictures;
    private Button btnSort;
    private Spinner spSort;
    private ImageSwitcher imageViewQuick;
    private boolean isGridView = true;
    private List<String> sortFields;
    private String sortField = "";
    private String sortType = "desc";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        rvPictures = binding.rvPictures;
        btnSort = binding.btnSort;
        spSort = binding.spSort;
        imageViewQuick = binding.imgViewQuick;

        pictures = PictureUtil.getPictures(activity, null);
        adapter = getPictureAdapter(isGridView);
        layoutManager = getLayoutManger(isGridView);

        rvPictures.hasFixedSize();
        rvPictures.setAdapter(adapter);
        rvPictures.setLayoutManager(layoutManager);

        btnSort.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Comparator<Picture> comparator = getComparator();
                pictures.sort(comparator);
                reversedSortType();
                adapter.notifyDataSetChanged();
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

        imageViewQuick.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        imageViewQuick.setInAnimation(in);
        imageViewQuick.setOutAnimation(out);

        autoUpdateQuickImage();

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

    private void autoUpdateQuickImage() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Random rand = new Random();
                                Picture picture = pictures.get(rand.nextInt(pictures.size()));
                                imageViewQuick.setImageURI(picture.getUri());
                            }
                        });
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pics) {
        PictureBrowserFragment browser = PictureBrowserFragment.newInstance(pics, position, getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position + "picture")
                .add(R.id.nav_host_fragment_content_main, browser)
                .addToBackStack(null)
                .commit();


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gallery_menu, menu);
        MenuItem mnuSearchPicture = menu.findItem(R.id.mnuSearchPicture);
        SearchView searchView = (SearchView) mnuSearchPicture.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                pictures = PictureUtil.getPictures(getActivity(), null);
                if (s.length() > 0) {
                    pictures = pictures.stream()
                            .filter(picture -> picture.getName().toLowerCase()
                                    .contains(s.toLowerCase())
                                    || DateUtil.getDate(picture.getCreatedDate()).contains(s))
                            .collect(Collectors.toList());
                }
                adapter.setPictures(pictures);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSwitch:
                if (isGridView) {
                    item.setIcon(getResources().getDrawable(R.drawable.list));
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.grid));
                }
                isGridView = !isGridView;
                layoutManager = getLayoutManger(isGridView);
                adapter = getPictureAdapter(isGridView);
                rvPictures.setAdapter(adapter);
                rvPictures.setLayoutManager(layoutManager);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}