package com.example.galleryapp.ui.locked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.galleryapp.R;
import com.example.galleryapp.databinding.FragmentLockedBinding;
import com.example.galleryapp.ui.empty.EmptyFragment;

public class LockedFragment extends Fragment {
    FragmentLockedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLockedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // do something here

        return root;
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
}
