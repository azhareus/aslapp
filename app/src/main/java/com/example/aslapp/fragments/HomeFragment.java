package com.example.aslapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aslapp.HomeActivity;
import com.example.aslapp.R;

public class HomeFragment extends Fragment {

    Context context;
    ImageView ivProfilePic;
    ImageView ivLevel;
    ImageView ivReview;
    ImageView ivTest;
    ImageView ivLogo;
    CardView cvLearn;
    CardView cvReview;
    LearnFragment learnFragment;
    ReviewFragment reviewFragment;
    CardView cvLevel;
    LevelsFragment levelsFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Context context) {
        HomeFragment fragment = new HomeFragment();
        fragment.context = context;
        fragment.learnFragment = LearnFragment.newInstance(context, fragment);
        fragment.reviewFragment = ReviewFragment.newInstance(context, fragment);
        fragment.levelsFragment = LevelsFragment.newInstance(context);
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
        ivLogo = view.findViewById(R.id.ivLogo);
        cvLearn = view.findViewById(R.id.cardTest);
        cvReview = view.findViewById(R.id.cardReview);
        cvLevel = view.findViewById(R.id.cardLevel);

        // Set Logo
        Glide.with(context)
                .load(R.drawable.logo)
                .circleCrop()
                .override(250,250)
                .into(ivLogo);

        Glide.with(context)
                .load(R.drawable.boy)
                .circleCrop()
                .override(175,175)
                .into(ivProfilePic);

        cvLearn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContainer, learnFragment, "tag")
                    .addToBackStack(null)
                    .commit();
            HomeActivity.bottomNavigationView.setVisibility(View.GONE);
        });
        cvReview.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContainer, reviewFragment, "tag")
                    .addToBackStack(null)
                    .commit();
            HomeActivity.bottomNavigationView.setVisibility(View.GONE);
        cvLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.flContainer, levelsFragment, "tag")
                        .addToBackStack(null)
                        .commit();
                HomeActivity.bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }
}