package com.example.galleryapp.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galleryapp.R;
import com.example.galleryapp.adapter.PictureAdapter;
import com.example.galleryapp.database.QueryContract;
import com.example.galleryapp.database.impl.AlbumQueryImplementation;
import com.example.galleryapp.database.impl.LinkQueryImplementation;
import com.example.galleryapp.databinding.FragmentFavoritesBinding;
import com.example.galleryapp.listener.OnItemClick;
import com.example.galleryapp.model.Album;
import com.example.galleryapp.model.Picture;
import com.example.galleryapp.ui.empty.EmptyFragment;
import com.example.galleryapp.ui.showImage.PictureShowFragment;

import java.util.List;

public class FavoritesFragment extends Fragment implements OnItemClick<Picture> {
    private FragmentFavoritesBinding binding;
    private RecyclerView rvPictures;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter adapter;
    private List<Picture> pictures;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rvPictures = binding.rvPictures;
        rvPictures.hasFixedSize();
        getAllFavourite();

        layoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new PictureAdapter(R.layout.picture_item_gird, pictures, this, null);
        rvPictures.setAdapter(adapter);
        rvPictures.setLayoutManager(layoutManager);

        return root;
    }

    private void getAllFavourite() {
        QueryContract.AlbumQuery albumQuery = new AlbumQueryImplementation();
        Album album = albumQuery.getAlbumFavorite();
        QueryContract.LinkQuery linkQuery = new LinkQueryImplementation();
        pictures = linkQuery.getAllFavorites(album.getId());
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
        Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPicClicked(PictureAdapter.PictureHolder holder, int position, List<Picture> pictures) {
        PictureShowFragment browser = PictureShowFragment.newInstance(pictures, position);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
//                .addSharedElement(holder.picture, holder.picName.getText().toString())
                .replace(R.id.nav_host_fragment_content_main, browser)
                .commit();
    }
}
