package com.example.galleryapp.ui.hidden.locked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.galleryapp.databinding.FragmentLockedBinding;

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
    }
}
