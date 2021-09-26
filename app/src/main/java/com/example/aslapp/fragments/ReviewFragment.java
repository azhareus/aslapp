package com.example.aslapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aslapp.R;
import com.example.aslapp.ReviewItems.Review;
import com.example.aslapp.ReviewItems.ReviewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    List<Review> reviews;
    Context context;
    HomeFragment homeFragment;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    public static ReviewFragment newInstance(Context context, HomeFragment homeFragment) {
        ReviewFragment fragment = new ReviewFragment();
        fragment.context = context;
        fragment.homeFragment = homeFragment;
        return fragment;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {

        RecyclerView rvQuotes = view.findViewById(R.id.rvReview);
        reviews = new ArrayList<>();
        reviews.add(new Review(R.drawable.when, "When"));
        reviews.add(new Review(R.drawable.thankyou, "Thank you"));
        reviews.add(new Review(R.drawable.apple, "Apple"));
        reviews.add(new Review(R.drawable.iloveyou, "I love you"));
        reviews.add(new Review(R.drawable.where, "Where"));

        //Create the adapter
        ReviewAdapter adapter = new ReviewAdapter(getContext(), reviews);
        //Set the adapter on the recycler view
        rvQuotes.setAdapter(adapter);
        //Set a Layout Manager on the recycler view
        rvQuotes.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    }