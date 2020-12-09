package com.example.tracker;

import android.content.Context;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import static java.lang.String.valueOf;

public class TravelPath implements Serializable, Parcelable {

    protected ArrayList<Location> locationList = new ArrayList<Location>();
    protected int size = 0;
    protected float travelledDistance = 0;
    protected String savedName = "Saved Path";
    protected String savedFileName = "Saved Path_Timestamp";
    protected boolean ended = false;

    protected float speed = 0;
    protected String startTime = "--";
    protected String endTime = "--";

    protected float elapsedTime = 0;

    protected Location startPoint = null;
    protected Location endPoint = null;


    public float getTravelledDistance(){
        return this.travelledDistance;
    }

    public int getSize(){
        return this.size;
    }

    public ArrayList<Location> getLocationList(){
        return this.locationList;
    }

    public String getSavedName() {
        return this.savedName;
    }

    public String getSavedFileName() {return this.savedFileName; }

    public float getSpeed() {
        return this.speed;
    }

    public boolean ended(){return this.ended; }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    public Location getStartPoint() {
        return this.startPoint;
    }

    public Location getEndPoint() {
        return this.endPoint;
    }

    public void addLocation(Location location) {
        if (this.size == 0) {
            this.locationList.add(location);
            this.startPoint = location;
            this.incrementSize(1);
        }

        else if(this.size > 0) {
            //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            float delta = location.distanceTo(locationList.get(this.size - 1));
            if (delta >= 0.5){
                this.locationList.add(location);
                this.incrementSize(1);
                this.incrementTravelledDistance(delta);
                this.speed = delta * 2;
            }
            else
                this.speed = 0;
        }
    }

    public void clearPath(){
        this.locationList.clear();
        this.startPoint = null;
        this.endPoint = null;
        this.ended = false;
        this.travelledDistance = 0;
        this.size = 0;
        this.startTime = "--";
        this.endTime = "--";
        this.savedName = "Saved Path";
        this.savedFileName = "Saved Path_Timestamp";
        this.elapsedTime = 0;
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

    private void setSpeed() {
        if (elapsedTime == 0)
            this.speed = this.getTravelledDistance();
        else
            this.speed = this.getTravelledDistance()/this.elapsedTime;
    }

    public void setStartPoint(Location startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint() {
        this.ended = true;
        if(this.size < 1)
            this.endPoint = this.startPoint;
        else
            this.endPoint = this.locationList.get(this.size-1);
        this.setElapsedTime();
    }

    private void setElapsedTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
        try {
            Date start = simpleDateFormat.parse(this.getStartTime());
            Date end = simpleDateFormat.parse(this.getEndTime());
            this.elapsedTime = (end.getTime() - start.getTime())/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setSpeed();
    }

    public void setSavedName(String nSavedName) {
        this.savedName = nSavedName;
    }

    public void setSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.locationList);
        dest.writeInt(this.size);
        dest.writeFloat(this.travelledDistance);
        dest.writeString(this.savedName);
        dest.writeFloat(this.speed);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        //dest.writeParcelable(this.mContext, flags);
    }

    public TravelPath() {
    }

    protected TravelPath(Parcel in) {
        this.locationList = in.createTypedArrayList(Location.CREATOR);
        this.size = in.readInt();
        this.travelledDistance = in.readFloat();
        this.savedName = in.readString();
        this.speed = in.readFloat();
        this.startTime = in.readString();
        this.endTime = in.readString();
        //this.mContext = in.readParcelable(Context.class.getClassLoader());
    }

    public static final Parcelable.Creator<TravelPath> CREATOR = new Parcelable.Creator<TravelPath>() {
        @Override
        public TravelPath createFromParcel(Parcel source) {
            return new TravelPath(source);
        }

        @Override
        public TravelPath[] newArray(int size) {
            return new TravelPath[size];
        }
    };
}
