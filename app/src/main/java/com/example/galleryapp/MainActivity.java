package com.example.galleryapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.AlbumQueryImplementation;
import com.example.galleryapp.database.util.mContext;
import com.example.galleryapp.databinding.ActivityMainBinding;
import com.example.galleryapp.model.Album;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 40;
    // Storing data into SharedPreferences


// Storing the key and its value as the data fetched from edittext


// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("myRef", Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!checkPermissionForReadExternalStorage()) {
            requestPermissionForReadExternalStorage();
        }
        if (!checkPermissionForCamera()) {
            requestPermissionForCamera();
        }


        // Creating an Editor object to edit(write to the file)

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setItemIconTintList(null);
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
        int count = albumQuery.getAlbumCount();



        if (count == 0) {

            this.createLayoutPassword();
            albumQuery.insertAlbum(new Album("Favorites"));
            albumQuery.insertAlbum(new Album("Hidden"));
            albumQuery.insertAlbum(new Album("Locked"));
        }
    }

    public void createLayoutPassword() {
        EditText editText = new EditText(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please! Enter your password");
        alert.setMessage("Password to access your Images Locked");

        alert.setView(editText);

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Continue with delete operation

                if (!editText.getText().toString().equals("")) {
                    myEdit.putString("password", editText.getText().toString());
                    myEdit.commit();
                    return;


                }
                else {
                    Toast.makeText(MainActivity.this, "Please! Input your password", Toast.LENGTH_SHORT).show();
                    createLayoutPassword();
                }

            }
        });

        // A null listener allows the button to dismiss the dialog and take no further action.
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Please! Input your password", Toast.LENGTH_SHORT).show();
                createLayoutPassword();
            }
        });
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

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

    //    private void showDialogCreateMessage(){
//        EditText editText = new EditText(this);
//        final Boolean[] loop = {true};
//        // TEXTVIEW
//        if(this.getParent() != null) {
//            ((ViewGroup)tv.getParent()).removeView(tv); // <- fix
//        }
//        layout.addView(tv)
//        while(true){
//            new AlertDialog.Builder(this)
//                    .setTitle("Please! Enter your password")
////                .setMessage("Are you sure you want to delete this entry?")
//                    .setView(editText)
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Continue with delete operation
//                            SharedPreferences sharedPreferences = getSharedPreferences("myRef", MODE_PRIVATE);
//                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                            if(!editText.getText().equals("")){
//                                myEdit.putString("password",editText.getText().toString());
//                                return;
//                            }
//
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
////                        Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//    }
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


}