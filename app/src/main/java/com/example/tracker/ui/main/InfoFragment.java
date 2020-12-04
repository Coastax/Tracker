package com.example.tracker.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracker.R;
import com.example.tracker.TravelPath;



public class InfoFragment extends Fragment {

    private InfoViewModel mViewModel;
    private TextView distanceView;
    private TextView speedView;
    private TextView startTimeView;
    private TextView stopTimeView;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //mViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mViewModel = ViewModelProviders.of(requireActivity()).get(InfoViewModel.class);
        mViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        distanceView = view.findViewById(R.id.distanceFld);
        speedView = view.findViewById(R.id.speedFld);
        startTimeView = view.findViewById(R.id.startTimeFld);
        stopTimeView = view.findViewById(R.id.stopTimeFld);

        mViewModel.getTravelledDistance().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float distance) {
                //Log.i("ViewModel","Distance changed");
                //Log.i("ViewModel", "Distance: " + distance);
                distanceView.setText(String.format("%.2f m", distance));
            }
        });

        mViewModel.getSpeed().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float speed) {
                //Log.i("ViewModel","Speed changed");
                // Log.i("ViewModel", "Speed: " + speed);
                speedView.setText(String.format("%.1f m/s", speed));
            }
        });

        mViewModel.getStartTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String startTime) {
                startTimeView.setText(startTime);
            }
        });

        mViewModel.getStopTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String stopTime) {
                stopTimeView.setText(stopTime);
            }
        });



    }

}