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
import com.example.galleryapp.ui.folder.PictureOfFolderFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment implements OnItemClick<Album> {
    FragmentAlbumBinding binding;
    Button btnCreate;
    RecyclerView rvAlbum;
    QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnCreate = binding.btnCreateAlbum;
        rvAlbum = binding.rvAlbum;

        // get list album
        List<Album> albums = new ArrayList<>();
        getAllAlbum(albums);

        AlbumAdapter albumAdapter = new AlbumAdapter(albums, this);
        rvAlbum.setAdapter(albumAdapter);
        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 4));


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create new Album");
                final EditText input = new EditText(getContext());
                builder.setView(input);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().isEmpty()) {
                            albumQuery.insertAlbum(new Album(input.getText().toString()), new DatabaseHelper.QueryResponse<Boolean>() {
                                @Override
                                public void onSuccess(Boolean data) {
                                    getAllAlbum(albums);
                                    albumAdapter.setAlbums(albums);
                                }

                                @Override
                                public void onFailure(String message) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
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

    private void getAllAlbum(List<Album> albums) {
        albumQuery.getAllAlbum(new DatabaseHelper.QueryResponse<List<Album>>() {
            @Override
            public void onSuccess(List<Album> data) {
                albums.clear();
                albums.addAll(data);
            }

            @Override
            public void onFailure(String message) {
                System.out.println("Album Fail: " + message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Album item, int pos) {
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        linkQuery.getAllPictureInAlbum(item.getId(), new DatabaseHelper.QueryResponse<List<Picture>>() {
            @Override
            public void onSuccess(List<Picture> data) {
                FragmentManager fm = getActivity().getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();

                PictureOfFolderFragment llf = new PictureOfFolderFragment();
                Bundle args = new Bundle();
                args.putSerializable("pictures", (Serializable) data);
                llf.setArguments(args);
                ft.replace(R.id.nav_host_fragment_content_main, llf);
                ft.commit();
            }

            @Override
            public void onFailure(String message) {
                Log.d("TAG", "Fail: " + message);
            }
        });
    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Album> pics) {

    }
}
