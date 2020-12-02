package com.example.tracker;

import android.location.Location;

import java.util.Vector;

public class TravelPath {

    protected Vector<Location> locationList = new Vector<Location>();
    protected int size = 0;
    protected float travelledDistance = 0;
    protected String savedName = "Saved Path";
    protected int ID = 0;


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

    public void saveCurrentPath(String filename){


    }
}
