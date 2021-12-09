package com.example.galleryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.galleryapp.database.databaseImplementation.AlbumQueryImplementation;
import com.example.galleryapp.database.databaseInterface.QueryContract;
import com.example.galleryapp.database.databaseInterface.QueryResponse;
import com.example.galleryapp.database.databaseUtil.mContext;
import com.example.galleryapp.databinding.ActivityMainBinding;
import com.example.galleryapp.model.Album;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!checkPermissionForReadExternalStorage()) {
            requestPermissionForReadExternalStorage();
        }
        if (!checkPermissionForCamera()) {
            requestPermissionForCamera();
        }

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_folder, R.id.nav_album,
                R.id.nav_camera, R.id.nav_favourites, R.id.nav_hidden,
                R.id.nav_locked, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController,
                mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // setup db
        mContext.context = getApplicationContext();
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        QueryResponse<Boolean> onCreateAlbum = new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                System.out.println("success insert new album");
            }

            @Override
            public void onFailure(String message) {
                System.out.println("fail: " + message);
            }
        };
        albumQuery.getAllAlbum(new QueryResponse<List<Album>>() {
            @Override
            public void onSuccess(List<Album> data) {
                if (data != null && data.size() >= 2) {
                } else {
                    albumQuery.insertAlbum(new Album("Favorites"), onCreateAlbum);
                    albumQuery.insertAlbum(new Album("Hidden"), onCreateAlbum);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 40;

    public boolean checkPermissionForReadExternalStorage() {
        int result = this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForCamera() {
        int result = this.checkSelfPermission(Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestPermissionForCamera() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_STORAGE_PERMISSION_REQUEST_CODE:
            case CAMERA_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // add other cases for more permissions
        }
    }


}