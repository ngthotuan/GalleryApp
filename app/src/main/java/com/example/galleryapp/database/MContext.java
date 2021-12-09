package com.example.galleryapp.database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MContext extends Application {
    public static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "===============onCreate: ");
        context = getApplicationContext();
    }
}
