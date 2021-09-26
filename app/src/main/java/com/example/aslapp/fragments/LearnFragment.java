package com.example.aslapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aslapp.HomeActivity;
import com.example.aslapp.R;

import java.util.ArrayList;
import java.util.List;

public class LearnFragment extends Fragment {

    int question = 0;
    Context context;
    Button btSubmit;
    HomeFragment homeFragment;
    List<List<String>> questions;
    ProgressBar progressBar;
    ImageView ivQuestion;
    TextView tvProgress;
    RadioGroup group;
    RadioButton q1;
    RadioButton q2;
    RadioButton q3;
    RadioButton q4;

    public LearnFragment() {
        // Required empty public constructor
    }


    public static LearnFragment newInstance(Context context, HomeFragment homeFragment) {
        LearnFragment fragment = new LearnFragment();
        fragment.context = context;
        fragment.homeFragment = homeFragment;
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
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questions = new ArrayList<>();
        createQuestions();
        tvProgress = view.findViewById(R.id.tvProgress);
        group = view.findViewById(R.id.radioQuestions);
        q1 = view.findViewById(R.id.r1);
        q2 = view.findViewById(R.id.r2);
        q3 = view.findViewById(R.id.r3);
        q4 = view.findViewById(R.id.r4);
        ivQuestion = view.findViewById(R.id.ivQuestion);
        progressBar = view.findViewById(R.id.progressBar2);

        btSubmit = view.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                tvProgress.setText("50% completed");
                progressBar.setProgress(progressBar.getProgress() + 10, true);

                if(question == 1) {
                    if(group.getCheckedRadioButtonId() == R.id.r2){
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                    }
                    question = 0;
                    getActivity().getSupportFragmentManager().popBackStack();
                }else if(question < 1){
                    if(group.getCheckedRadioButtonId() == R.id.r1){
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                    }
                    changeQuestion(question);
                    question++;
                }else{
                    question = 0;
                    changeQuestion(question);
                }
            }
        });
    }

    private void createQuestions() {
        List<String> options = new ArrayList<>();
        options.add("Who");
        options.add("Why");
        options.add("When");
        options.add("How");
        questions.add(options);

        options.clear();
        options.add("Them");
        options.add("When");
        options.add("No");
        options.add("Sure");
        questions.add(options);
    }

    public void changeQuestion(int questionNumber){
        List<String> currentQuestion = questions.get(questionNumber);

        if(questionNumber == 0){
            Drawable image = getResources().getDrawable(R.drawable.when);
            Glide.with(context)
                    .load(image)
                    .into(ivQuestion);
        }else{
            Drawable image = getResources().getDrawable(R.drawable.where);
            Glide.with(context)
                    .load(image)
                    .into(ivQuestion);
        }

        group.clearCheck();
        q1.setText(currentQuestion.get(0));
        q2.setText(currentQuestion.get(1));
        q3.setText(currentQuestion.get(2));
        q4.setText(currentQuestion.get(3));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HomeActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }
}