package com.example.galleryapp.ui.showImage;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.galleryapp.R;
import com.example.galleryapp.adapter.ImagesPagerAdapter;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.adapter.RecyclerViewPagerImageIndicator;
import com.example.galleryapp.database.AlbumQueryImplementation;
import com.example.galleryapp.database.LinkQueryImplementation;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.QueryResponse;
import com.example.galleryapp.databinding.FragmentPictureShowBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.DateUtil;
import com.example.galleryapp.utils.ShareUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PictureShowFragment extends Fragment implements OnItemClick<Picture> {

    FragmentPictureShowBinding binding;
    private List<Picture> images = new ArrayList<>();
    private int position;
    private ViewPager viewPager;
    private RecyclerView indicatorRecycler;
    private ImagesPagerAdapter pagerAdapter;
    private int previousSelected = -1;
    private Context context = null;

    public PictureShowFragment() {

    }

    public PictureShowFragment(List<Picture> images, int position) {
        this.images = images;
        this.position = position;
    }

    public static PictureShowFragment newInstance(List<Picture> images, int position) {
        PictureShowFragment fragment = new PictureShowFragment(images, position);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getContext();
        binding = FragmentPictureShowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        indicatorRecycler = binding.indicatorRecycler;
        viewPager = binding.imagePager;
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.image_view_option_drawer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.imgShare: {
                ShareUtils.shareImage(getContext(), images.get(position));
                break;
            }
            case R.id.imgEdit: {
                Intent editIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                editIntent.setData(images.get(position).getUri());
                //set directory
                editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "edit");
                editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));

                editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFF"));
                editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, new int[]{DsPhotoEditorActivity.TOOL_WARMTH, DsPhotoEditorActivity.TOOL_PIXELATE});
                getActivity().startActivityForResult(editIntent, 201);
                break;
            }
            case R.id.imgViewDetail: {
                showDetails();
                break;
            }
            case R.id.imgViewDelete: {
                //deleteImage();
                break;
            }
            case R.id.imgWallpaper: {
                setWallpaper(images.get(position));
                break;
            }
            case R.id.imgAddAlbum: {
                addImageToAlbum();
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void addImageToAlbum() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        albumQuery.getAllAlbum(new QueryResponse<List<Album>>() {
            @Override
            public void onSuccess(List<Album> data) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose albums");
                String[] objects = data.stream().map(Album::getName)
                        .toArray(String[]::new);
                boolean[] checkedItems = new boolean[data.size()];
                Arrays.fill(checkedItems, Boolean.FALSE);

                builder.setMultiChoiceItems(objects, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // The user checked or unchecked a box
                        checkedItems[which] = isChecked;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The user clicked OK
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                List<Picture> pictures = Arrays.asList(images.get(position));
                                QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
                                linkQuery.insertImagesToAlbums(pictures, data.get(i));
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailure(String message) {
                System.out.println("error========");
                System.out.println(message);

            }
        });
    }

    private void setWallpaper(Picture picture) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picture.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "setWallpaper: " + bitmap);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(getContext(), "Wallpaper set !!!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(getContext(), "Wallpaper not set !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetails() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Image Infomation.");
        alertDialog.setIcon(R.drawable.ic_baseline_info_24);
        alertDialog.setMessage(
                "Name: " + images.get(position).getName() + "\n"
                        + "Path: " + images.get(position).getPath() + "\n"
                        + "Size: " + images.get(position).getSizeStr() + "\n"
                        + "Type: " + images.get(position).getType() + "\n"
                        + "Uri: " + images.get(position).getUri() + "\n"
                        + "Created date: " + DateUtil.getDate(images.get(position).getCreatedDate()) + "\n"
                        + "Modified date" + DateUtil.getDate(images.get(position).getModifiedDate()) + "\n");
        alertDialog.setPositiveButton("Close", null);
        alertDialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Set and show apdapter for list picture in can pick and rely on current picture


        indicatorRecycler.hasFixedSize();
        indicatorRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));
        RecyclerView.Adapter indicatorAdapter = new RecyclerViewPagerImageIndicator(images, getContext(), this);
        indicatorRecycler.setAdapter(indicatorAdapter);


        //show image current

        pagerAdapter = new ImagesPagerAdapter(getContext(), indicatorRecycler, images);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(position);

        indicatorRecycler.setVisibility(View.VISIBLE);
        images.get(position).setSelected(true);
        previousSelected = position;
        indicatorAdapter.notifyDataSetChanged();
        indicatorRecycler.scrollToPosition(position);


        viewPager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDetails();
                return false;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //handle load left right pager view
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPageSelected(int position) {

                if (previousSelected != -1) {
                    images.get(previousSelected).setSelected(false);
                    previousSelected = position;
                    images.get(position).setSelected(true);

                    indicatorRecycler.scrollToPosition(position);
                } else {
                    previousSelected = position;
                    images.get(position).setSelected(true);
                    indicatorRecycler.scrollToPosition(position);
                }
                indicatorRecycler.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(Picture item, int pos) {
        if (previousSelected != -1) {
            images.get(previousSelected).setSelected(false);
            previousSelected = pos;
            Objects.requireNonNull(indicatorRecycler.getAdapter()).notifyDataSetChanged();
        } else {
            previousSelected = pos;
        }

        position = pos;
        viewPager.setCurrentItem(pos);
    }


    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pics) {
        Log.e("TAG", "onPicClicked: ");
    }
}