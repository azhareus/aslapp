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

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

public class ProfileFragment extends Fragment {

    Context context;
    ImageView ivProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(Context context) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfile = view.findViewById(R.id.ivProfilePic);

        // TODO: Change the drawable to the current user's profile pic
        Glide.with(context)
                .load(R.drawable.boy)
                .circleCrop()
                .override(300, 300)
                .into(ivProfile);

        ValueLineChart mCubicValueLineChart = (ValueLineChart) view.findViewById(R.id.cubiclinechart);
        mCubicValueLineChart.setActivateIndicatorShadow(false);
        mCubicValueLineChart.setIndicatorWidth(0);
        mCubicValueLineChart.setIndicatorTextColor(000000);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        series.addPoint(new ValueLinePoint("Mon", 2.4f));
        series.addPoint(new ValueLinePoint("Tue", 3.4f));
        series.addPoint(new ValueLinePoint("Wed", .4f));
        series.addPoint(new ValueLinePoint("Thu", 1.2f));
        series.addPoint(new ValueLinePoint("Fri", 2.6f));
        series.addPoint(new ValueLinePoint("Sat", 1.0f));
        series.addPoint(new ValueLinePoint("Sun", 3.5f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
    }
}