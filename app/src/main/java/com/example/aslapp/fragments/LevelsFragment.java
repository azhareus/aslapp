package com.example.aslapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.aslapp.HomeActivity;
import com.example.aslapp.R;
import com.example.aslapp.YoutubeActivity;

import java.util.HashMap;

public class LevelsFragment extends Fragment {

    public static int MODULES = 6;
    public static String [] videos;
    public static HashMap<String, String> position = new HashMap<>();
    Context context;

    public LevelsFragment() {
        // Required empty public constructor
    }

    public static LevelsFragment newInstance(Context context) {
        LevelsFragment fragment = new LevelsFragment();
        fragment.makeMapping();
        fragment.context = context;
        return fragment;
    }

    public void makeMapping(){
        position.put("Alphabet", "Niyz8wHXZX4");
        position.put("Salutations", "P3zh6AYUOg");
        position.put("Questions", "KRo-x2uoHUg");
        position.put("Places", "Nw9ABhSbvc");
        position.put("Emotions", "r465_ijzhA");
        position.put("Sentence Structures", "m4hoduUyL7E");
        videos = new String [MODULES];
        videos[0] = "Alphabet";
        videos[1] = "Salutations";
        videos[2] = "Questions";
        videos[3] = "Places";
        videos[4] = "Emotions";
        videos[5] = "Sentence Structures";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_levels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayout grid = (GridLayout) view.findViewById(R.id.gridLayout);
        int childCount = grid.getChildCount();

        for (int i= 0; i < childCount; i++){
            CardView container = (CardView) grid.getChildAt(i);
            int finalI = i;
            container.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent i = new Intent(context, YoutubeActivity.class);
                    i.putExtra("video", position.get(videos[finalI]));
                    context.startActivity(i);
                    HomeActivity.bottomNavigationView.setVisibility(View.GONE);
                }
            });
        }
    }

}