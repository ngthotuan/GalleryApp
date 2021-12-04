package com.example.galleryapp.utils;

import android.content.Context;
import android.content.Intent;

import com.example.galleryapp.model.Picture;

public class ShareUtils {
    public static void shareImage(Context context, Picture picture) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        switch (picture.getType()) {
            case "image": {
                shareIntent.setType("image/*");
                break;
            }
            case "video": {
                shareIntent.setType("video/*");
                break;
            }
            default: {
                shareIntent.setType("text/*");
                break;
            }
        }

        shareIntent.putExtra(Intent.EXTRA_STREAM, picture.getUri());

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}
