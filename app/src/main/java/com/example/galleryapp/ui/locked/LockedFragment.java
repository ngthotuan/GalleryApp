package com.example.galleryapp.ui.locked;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureFolderAdapter;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.AlbumQueryImplementation;
import com.example.galleryapp.database.impl.LinkQueryImplementation;
import com.example.galleryapp.databinding.FragmentLockedBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.ui.empty.EmptyFragment;
import com.example.galleryapp.ui.showImage.PictureShowFragment;

import java.util.List;

public class LockedFragment extends Fragment  implements OnItemClick<Picture> {
    FragmentLockedBinding binding;

    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private List<Picture> pictures;
    RecyclerView recyclerView ;


    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLockedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.rvLockPictures;
        layoutManager = new GridLayoutManager(getContext(), 4);
        final EditText editText = new EditText(getContext());
        createLayoutPassword(editText);


        return root;
    }

    public void createLayoutPassword(EditText editText){

        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("myRef", Context.MODE_PRIVATE);
            String password = sharedPreferences.getString("password", "");
            new AlertDialog.Builder(getContext())
                .setTitle("Please! Enter your password")
                .setMessage("Enter your password to open list images")
                .setView(editText)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    public void onClick(DialogInterface dialog, int which) {

                        if(!password.equals("")&&password.equals(editText.getText().toString())){
                            getAllLocked();

                            adapter = new PictureAdapter(R.layout.picture_item_gird, pictures, LockedFragment.this, null);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(layoutManager);


                        }
                        else {
                            Toast.makeText(getContext(),"Mật khẩu không đúng" , Toast.LENGTH_SHORT).show();
                        }

                    }


                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(),"Mật khẩu không đúng" , Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG);
        }

    }
    private void getAllLocked() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        Album album = albumQuery.getAlbumLocked();
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        pictures = linkQuery.getAllLocked(album.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment_content_main, new EmptyFragment());
        ft.commit();
    }

    @Override
    public void onClick(Picture item, int pos) {

    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pics) {
        PictureShowFragment browser = PictureShowFragment.newInstance(pictures, position);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, browser)
                .commit();
    }
}
