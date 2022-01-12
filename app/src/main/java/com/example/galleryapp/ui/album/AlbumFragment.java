package com.example.galleryapp.ui.album;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.adapter.AlbumAdapter;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.database.DatabaseHelper;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.AlbumQueryImplementation;
import com.example.galleryapp.database.impl.LinkQueryImplementation;
import com.example.galleryapp.databinding.FragmentAlbumBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.ui.empty.EmptyFragment;
import com.example.galleryapp.ui.folder.PictureOfFolderFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment implements OnItemClick<Album> {
    FragmentAlbumBinding binding;
    Button btnCreate;
    RecyclerView rvAlbum;
    QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
    List<Album> albums = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnCreate = binding.btnCreateAlbum;
        rvAlbum = binding.rvAlbum;

        // get list album
        getAllAlbum();

        AlbumAdapter albumAdapter = new AlbumAdapter(albums, this);
        rvAlbum.setAdapter(albumAdapter);
        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 1));


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create new Album");
                EditText input = new EditText(getContext());
                builder.setView(input);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().isEmpty()) {
                            long id = albumQuery.insertAlbum(new Album(input.getText().toString()));
                            if(id != -1) {
                                getAllAlbum();
                                albumAdapter.setAlbums(albums);
                            } else {
                                Toast.makeText(getContext(), "Create album failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return root;
    }

    private void getAllAlbum() {
        albums.clear();
        albums.addAll(albumQuery.getAllAlbum());
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
    public void onClick(Album item, int pos) {
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        List<Picture> allPictureInAlbum = linkQuery.getAllPictureInAlbum(item.getId());
        if (allPictureInAlbum != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();
            PictureOfFolderFragment llf = new PictureOfFolderFragment();
            Bundle args = new Bundle();
            args.putSerializable("pictures", (Serializable) allPictureInAlbum);
            llf.setArguments(args);
            ft.replace(R.id.nav_host_fragment_content_main, llf);
            ft.commit();
        } else {
            Toast.makeText(getContext(), "Get picture in album fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Album> pics) {

    }
}
