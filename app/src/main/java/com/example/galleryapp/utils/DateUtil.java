package com.example.galleryapp.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    @SuppressLint("SimpleDateFormat")
    public static String getDate(long timeMillis, String pattern) {
        timeMillis *= 1000L;
        return new SimpleDateFormat(pattern).format(new Date(timeMillis));
    }

    public static String getDate(long timeMillis) {
        return getDate(timeMillis, "dd/MM/yyyy HH:mm:ss");
    }
}
