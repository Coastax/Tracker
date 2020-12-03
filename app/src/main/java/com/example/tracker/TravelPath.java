package com.example.tracker;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Vector;

public class TravelPath implements Serializable {

    protected Vector<Location> locationList = new Vector<Location>();
    protected int size = 0;
    protected float travelledDistance = 0;
    protected String savedName = "Saved Path";
    protected int ID = 0;
    protected float currentSpeed = 0;


    public float getTravelledDistance(){
        return this.travelledDistance;
    }
    public float getSize(){
        return this.size;
    }
    public Vector<Location> getLocationList(){
        return this.locationList;
    }
    public String getSavedName() {return this.savedName; }
    public int getID() {return this.ID;}

    public void addLocation(Location location) {
        this.locationList.add(location);
        this.incrementSize(1);
        if(this.size > 1){
            float delta = location.distanceTo(locationList.get(this.size - 2));
            this.incrementTravelledDistance(delta);
        }
    }

    public void clearLocation(){
        this.locationList.clear();
        this.size = 0;
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

    public void setID(int nID) {
        this.ID = nID;
    }

    public void setSavedName(String nSavedName) {
        this.savedName = nSavedName;
    }

    //TODO: save the path to a file
    public void saveCurrentPath(String filename){


    }

    public Location getStartPoint() {
        return this.locationList.get(0);
    }

    public Location getEndPoint() {
        return this.locationList.get(this.size-1);
    }
}
