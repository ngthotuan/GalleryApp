package com.example.galleryapp.database;

import android.app.Application;
import android.content.Context;

public class mContext extends Application {
    public static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
