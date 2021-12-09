package com.example.galleryapp;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
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
import com.example.galleryapp.adapter.ImagesPagerAdapter;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.adapter.RecyclerViewPagerImageIndicator;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.DateUtil;
import com.example.galleryapp.utils.ShareUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PictureShowFragment extends Fragment implements OnItemClick<Picture> {

    private List<Picture> images = new ArrayList<>();
    private int position;
    private ViewPager viewPager;
    private RecyclerView indicatorRecycler;

    private ImagesPagerAdapter pagerAdapter;
    private int previousSelected = -1;

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
        return inflater.inflate(R.layout.fragment_picture_browser, container, false);

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
            case R.id.imgEdit:{
                Intent editIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                editIntent.setData(images.get(position).getUri());
                //set directory
                editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "edit");
                editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF6200EE"));

                editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,Color.parseColor("#FFFFFF"));
                editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,new int[]{DsPhotoEditorActivity.TOOL_WARMTH,DsPhotoEditorActivity.TOOL_PIXELATE});
                getActivity().startActivityForResult(editIntent,201);
                break;
            }
            case R.id.imgViewDetail:{
                showDetails();
                break;
            }
            case R.id.imgViewDelete: {
                //deleteImage();
            }
            case R.id.imgWallpaper:{
                setWallpaper(images.get(position));
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void setWallpaper(Picture picture) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picture.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG" , "setWallpaper: " + bitmap);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
        try{
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(getContext(),"Wallpaper set !!!",Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(getContext(),"Wallpaper not set !!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDetails(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Image Infomation.");
        alertDialog.setIcon(R.drawable.ic_baseline_info_24);
        alertDialog.setMessage(
                "Name: " + images.get(position).getName()+ "\n"
                + "Path: " + images.get(position).getPath()+ "\n"
                + "Size: " + images.get(position).getSizeStr()+ "\n"
                + "Type: " + images.get(position).getType()+ "\n"
                + "Uri: " + images.get(position).getUri()+ "\n"
                + "Created date: " + DateUtil.getDate(images.get(position).getCreatedDate()) + "\n"
                + "Modified date" + DateUtil.getDate(images.get(position).getModifiedDate()) + "\n");
        alertDialog.setPositiveButton("Close",null);
        alertDialog.show();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Set and show apdapter for list picture in can pick and rely on current picture

        indicatorRecycler = view.findViewById(R.id.indicatorRecycler);
        indicatorRecycler.hasFixedSize();
        indicatorRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));
        RecyclerView.Adapter indicatorAdapter = new RecyclerViewPagerImageIndicator(images, getContext(), this);
        indicatorRecycler.setAdapter(indicatorAdapter);


        //show image current
        viewPager = view.findViewById(R.id.imagePager);
        pagerAdapter = new ImagesPagerAdapter(getContext(),indicatorRecycler,images);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(position);

        images.get(position).setSelected(true);
        previousSelected = position;
        indicatorAdapter.notifyDataSetChanged();
        indicatorRecycler.scrollToPosition(position);

        viewPager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDetails();
                return  false;
            }
        });





        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //handle load left right pager view
            @Override
            public void onPageSelected(int position) {

                if (previousSelected != -1) {
                    images.get(previousSelected).setSelected(false);
                    previousSelected = position;
                    images.get(position).setSelected(true);
                    indicatorRecycler.getAdapter().notifyDataSetChanged();
                    indicatorRecycler.scrollToPosition(position);
                } else {
                    previousSelected = position;
                    images.get(position).setSelected(true);
                    indicatorRecycler.getAdapter().notifyDataSetChanged();
                    indicatorRecycler.scrollToPosition(position);
                }
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
            indicatorRecycler.getAdapter().notifyDataSetChanged();
        } else {
            previousSelected = pos;
        }

        position = pos;
        viewPager.setCurrentItem(pos);
    }


    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pics) {
        Log.e("TAG", "onPicClicked: " );
    }



}