package com.example.galleryapp.ui.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.AlbumQueryImplementation;
import com.example.galleryapp.database.impl.LinkQueryImplementation;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.ui.empty.EmptyFragment;
import com.example.galleryapp.ui.showImage.PictureShowFragment;
import com.example.galleryapp.utils.DateUtil;
import com.example.galleryapp.utils.PictureUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class GalleryFragment extends Fragment implements OnItemClick<Picture> {
    private FragmentGalleryBinding binding;
    private RecyclerView rvPictures;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private List<Picture> pictures;
    private TextView txtSort;
    private Spinner spSort;
    private ImageSwitcher imgAnimation;
    private boolean showAnimation = true;
    private boolean isGridView = true;
    private List<String> sortFields;
    private String sortField = "";
    private String sortType = "";
    private LinearLayout filters;
    private boolean showFilter = false;
    private final List<Picture> listLongImage = new ArrayList<>();
    FloatingActionButton cameraButton;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    private List<Picture> getAllFavourite() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        Album album = albumQuery.getAlbumFavorite();
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        return linkQuery.getAllPictureInAlbum(album.getId());
    }

    private List<Picture> getAllHidden() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        Album album = albumQuery.getAlbumHidden();
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        return linkQuery.getAllPictureInAlbum(album.getId());
    }

    private List<Picture> getAllLocked() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        Album album = albumQuery.getAlbumLocked();
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        return linkQuery.getAllPictureInAlbum(album.getId());
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = getActivity();

        rvPictures = binding.rvPictures;
        rvPictures.hasFixedSize();
        txtSort = binding.txtSort;
        spSort = binding.spSort;
        imgAnimation = binding.imgAnimation;
        filters = binding.filters;
        cameraButton= binding.camera;

        List<Picture> allPicture = PictureUtil.getPictures(activity, null);
        pictures = allPicture;

        sortFields = getSortFields();

        setViewMode(isGridView);

        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortType.equals(getResources().getString(R.string.down_icon))) {
                    sortType = getResources().getString(R.string.up_icon);
                } else {
                    sortType = getResources().getString(R.string.down_icon);
                }
                txtSort.setText(sortType);
                Log.e("TAG", "onClick: " + listLongImage.size());
                updateSort();
            }
        });

        spSort.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, sortFields));
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortField = sortFields.get(i);
                updateSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgAnimation.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        imgAnimation.setInAnimation(in);
        imgAnimation.setOutAnimation(out);
        autoShowImageAnimation();
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        return root;
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(cameraButton.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) cameraButton.getContext(), new String[]{Manifest.permission.CAMERA, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        imageView.setImageBitmap(imageBitmap);
//        Uri uri = data.getData();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), imageBitmap, "Title", null);

        Intent editIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);

        editIntent.setData(Uri.parse(path));
        //set directory
        editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "edit");
        editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));

        editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFF"));
        editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, new int[]{DsPhotoEditorActivity.TOOL_WARMTH, DsPhotoEditorActivity.TOOL_PIXELATE});
        getActivity().startActivityForResult(editIntent, 201);
//        break;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<Picture> allFavourite = getAllFavourite();
        List<Picture> allHidden = getAllHidden();
        List<Picture> allLocked = getAllLocked();
        PictureUtil.updateFavorites(pictures, allFavourite);
        PictureUtil.updateHidden(pictures, allHidden);
        PictureUtil.updateLocked(pictures,allLocked);
        pictures = pictures.stream().filter(p -> !p.isHidden()&&!p.isLocked()).collect(Collectors.toList());
        adapter.setPictures(pictures);
        adapter.notifyDataSetChanged();
    }

    private PictureAdapter getPictureAdapter(boolean isGridView) {
        int resource = isGridView ? R.layout.picture_item_gird : R.layout.picture_item_list;
        return new PictureAdapter(resource, pictures, this, listLongImage);
    }

    private RecyclerView.LayoutManager getLayoutManger(boolean isGridView) {
        return isGridView ? new GridLayoutManager(getContext(), 4)
                : new LinearLayoutManager(getContext());
    }

    private List<String> getSortFields() {
        return Arrays.asList(
                getResources().getString(R.string.created_date),
                getResources().getString(R.string.name),
                getResources().getString(R.string.size)
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

        if (sortType.equals(getResources().getString(R.string.down_icon))) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private void updateSort() {
        Comparator<Picture> comparator = getComparator();
        pictures.sort(comparator);
        adapter.notifyDataSetChanged();
    }

    private void autoShowImageAnimation() {
        new Thread() {
            public void run() {
                while (showAnimation) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Random rand = new Random();
                                if (pictures.size() > 0) {
                                    Picture picture = pictures.get(rand.nextInt(pictures.size()));
                                    imgAnimation.setImageURI(picture.getUri());
                                }
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
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment_content_main, new EmptyFragment());
        ft.commit();
        showAnimation = false;
    }

    @Override
    public void onClick(Picture item, int pos) {
        Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pictures) {
        PictureShowFragment browser = PictureShowFragment.newInstance(pictures, position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position + "picture")
                .replace(R.id.nav_host_fragment_content_main, browser)
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
                pictures.sort(getComparator());
                adapter.setPictures(pictures);
                return false;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSwitch:
                reversedViewMode(item);
                setViewMode(isGridView);
                break;
            case R.id.mnuFilter:
                showFilter = !showFilter;
                if (showFilter) {
                    filters.setVisibility(View.VISIBLE);
                    imgAnimation.setVisibility(View.GONE);
                } else {
                    filters.setVisibility(View.GONE);
                    if (isGridView) {
                        imgAnimation.setVisibility(View.VISIBLE);
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void reversedViewMode(@NonNull MenuItem item) {
        isGridView = !isGridView;
        if (isGridView) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_grid_view_24));
            if (!showFilter) {
                imgAnimation.setVisibility(View.VISIBLE);
            }
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_list_24));
            imgAnimation.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setViewMode(boolean isGird) {
        layoutManager = getLayoutManger(isGird);
        adapter = getPictureAdapter(isGird);
        rvPictures.setAdapter(adapter);
        rvPictures.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();
    }
}