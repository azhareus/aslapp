package com.example.aslapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aslapp.R;

public class HomeFragment extends Fragment {

    Context context;
    ImageView ivProfilePic;
    ImageView ivLevel;
    ImageView ivReview;
    ImageView ivTest;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Context context) {
        HomeFragment fragment = new HomeFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        ivLevel = view.findViewById(R.id.ivLevel);
        ivReview = view.findViewById(R.id.ivReview);
        ivTest = view.findViewById(R.id.ivTest);

        Glide.with(context)
                .load(R.drawable.ic_baseline_home_24)
                .override(60,70)
                .into(ivLevel);

        Glide.with(context)
                .load(R.drawable.ic_baseline_camera_24)
                .override(60,70)
                .into(ivReview);

        Glide.with(context)
                .load(R.drawable.ic_baseline_person_24)
                .override(60,70)
                .into(ivTest);

        Glide.with(context)
                .load(R.drawable.profilepic)
                .circleCrop()
                .override(175,175)
                .into(ivProfilePic);
    }
}