package com.example.tracker.ui.main;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tracker.TravelPath;

public class InfoViewModel extends ViewModel {
    private MutableLiveData<TravelPath> displayPath = new MutableLiveData<>();
    private MutableLiveData<Float> travelledDistance = new MutableLiveData<>();
    private MutableLiveData<Float> speed = new MutableLiveData<>();
    private MutableLiveData<String> startTime = new MutableLiveData<>();
    private MutableLiveData<String> stopTime = new MutableLiveData<>();

    public void setDisplayPath (TravelPath path) {

        this.displayPath.setValue(path);
        this.travelledDistance.setValue(path.getTravelledDistance());
        this.speed.setValue(path.getSpeed());
        this.startTime.setValue(path.getStartTime());
        this.stopTime.setValue(path.getEndTime());
        //Log.i("ViewModel","Path set");
        //Log.i("ViewModel", "Distance: " + travelledDistance.getValue());
    }

    public MutableLiveData<Float> getTravelledDistance() {
        return this.travelledDistance;
    }

    public MutableLiveData<Float> getSpeed(){
        return this.speed;
    }

    public MutableLiveData<TravelPath> getDisplayPath(){
        return this.displayPath;
    }

    public MutableLiveData<String> getStartTime() {
        return startTime;
    }

    public MutableLiveData<String> getStopTime() {
        return stopTime;
    }
}