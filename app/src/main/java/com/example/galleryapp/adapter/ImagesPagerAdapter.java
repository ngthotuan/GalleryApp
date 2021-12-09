package com.example.galleryapp.adapter;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.galleryapp.R;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesPagerAdapter extends PagerAdapter {
    private Context context;
    private RecyclerView indicatorRecycler;
    private List<Picture> images;
    private ImageView imgView;

    public ImagesPagerAdapter(Context context , RecyclerView indicatorRecycler, List<Picture> images) {
        this.context = context;
        this.indicatorRecycler = indicatorRecycler;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup containerCollection, int position) {
        LayoutInflater layoutinflater = (LayoutInflater) containerCollection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutinflater.inflate(R.layout.picture_browser_pager, null);
        imgView = view.findViewById(R.id.image);

        setTransitionName(imgView, position + "picture");

        Picture pic = images.get(position);
//        pic.setSelected(true);
        Picasso.get()
                .load(pic.getUri())
                .into(imgView);

        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    showDetails(position);
                    // Do what you want
                    return true;
                }
                ;
                return false;
            }

        });
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (indicatorRecycler.getVisibility() == View.GONE) {
                    indicatorRecycler.setVisibility(View.VISIBLE);
                } else {
                    indicatorRecycler.setVisibility(View.GONE);
                }
            }
        });


        containerCollection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup containerCollection, int position, Object view) {
        containerCollection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    private void showDetails(int pos){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setTitle("Image Infomation.");
        alertDialog.setIcon(R.drawable.ic_baseline_info_24);
        alertDialog.setMessage(
                "Name: " + images.get(pos).getName()+ "\n"
                        + "Path: " + images.get(pos).getPath()+ "\n"
                        + "Size: " + images.get(pos).getSizeStr()+ "\n"
                        + "Type: " + images.get(pos).getType()+ "\n"
                        + "Uri: " + images.get(pos).getUri()+ "\n"
                        + "Created date: " + DateUtil.getDate(images.get(pos).getCreatedDate()) + "\n"
                        + "Modified date" + DateUtil.getDate(images.get(pos).getModifiedDate()) + "\n");
        alertDialog.setPositiveButton("Close",null);
        alertDialog.show();
    }

}

