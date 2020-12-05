package com.example.tracker;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class TravelPath implements Serializable {

    protected Vector<Location> locationList = new Vector<Location>();
    protected int size = 0;
    protected float travelledDistance = 0;
    protected String savedName = "Saved Path";
    protected int ID = 0;
    protected float speed = 0;
    protected String startTime = "--";
    private  String  endTime = "--";
    private Context mContext;


    public float getTravelledDistance(){
        return this.travelledDistance;
    }

    public float getSize(){
        return this.size;
    }

    public Vector<Location> getLocationList(){
        return this.locationList;
    }

    public String getSavedName() {
        return this.savedName;
    }

    public int getID() {
        return this.ID;
    }

    public float getSpeed() {
        return this.speed;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    public Location getStartPoint() {
        return this.locationList.get(0);
    }

    public Location getEndPoint() {
        if(this.size != 0) {
            return this.locationList.get(this.size - 1);
        }
        else
            return null;
    }

    public void addLocation(Location location) {
        if (this.size == 0) {
            this.locationList.add(location);
            this.incrementSize(1);
        }

        else if(this.size > 0) {
            //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            float delta = location.distanceTo(locationList.get(this.size - 1));
            if (delta >= 0.1){
                this.locationList.add(location);
                this.incrementSize(1);
                this.incrementTravelledDistance(delta);
            }
            this.speed = delta / 1;
        }
    }

    public void clearPath(){
        this.locationList.clear();
        this.travelledDistance = 0;
        this.size = 0;
        this.startTime = "--";
        this.endTime = "--";
        this.speed = 0;
    }
    public void incrementSize(int amount) {
        this.size = this.size + amount;
    }

    public void decrementSize(int amount) {
        this.size = this.size - amount;
    }

    public void incrementTravelledDistance(float delta) {
        this.travelledDistance = this.travelledDistance + delta;
    }

    public void decrementTravelledDistance(float delta) {
        this.travelledDistance = this.travelledDistance - delta;
    }

    public void setSpeed(float nSpeed) {this.speed = nSpeed;}

    public void setID(int nID) {
        this.ID = nID;
    }

    public void setSavedName(String nSavedName) {
        this.savedName = nSavedName;
    }

    public void saveCurrentPath(String filename) throws Exception {
        FileOutputStream out = mContext.openFileOutput(filename, mContext.MODE_PRIVATE);
        out.write(filename.getBytes());
        out.close();
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
