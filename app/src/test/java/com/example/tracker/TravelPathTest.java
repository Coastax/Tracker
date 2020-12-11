package com.example.tracker;

import android.location.Location;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;



class TravelPathTest {

    @Test
    void getTravelledDistance() {
        TravelPath t= new TravelPath();
        t.travelledDistance = 0;
        assertEquals(0, t.getTravelledDistance());
    }

    @Test
    void getSize() {
        TravelPath t= new TravelPath();
        t.size = 0;
        assertEquals(0, t.getSize());
    }

    @Test
    void getLocationList() {
        TravelPath t= new TravelPath();
        t.locationList = new ArrayList<Location>();
        assertEquals(t.locationList, t.getLocationList());
    }

    @Test
    void getSavedName() {
        TravelPath t= new TravelPath();
        t.savedName = "save_name" ;
        assertEquals("save_name", t.getSavedName());
    }

    @Test
    void getSavedFileName() {
        TravelPath t= new TravelPath();
        t.savedFileName = "savefile_name" ;
        assertEquals("savefile_name", t.getSavedFileName());
    }

    @Test
    void getSpeed() {
        TravelPath t= new TravelPath();
        t.speed = 50 ;
        assertEquals(50, t.getSpeed());
    }

    @Test
    void ended() {
        TravelPath t= new TravelPath();
        t.ended = false ;
        assertEquals(false, t.ended());
    }

    @Test
    void getStartTime() {
        TravelPath t= new TravelPath();
        t.startTime = "50" ;
        assertEquals("50", t.getStartTime());
    }

    @Test
    void getEndTime() {
        TravelPath t= new TravelPath();
        t.setEndTime("50")   ;
        assertEquals("50", t.getEndTime());
    }

    @Test
    void getStartPoint() {
        TravelPath t= new TravelPath();
        t.locationList = new ArrayList<Location>();
        assertEquals(t.getStartPoint(), t.getStartPoint());
    }

    @Test
    void getEndPoint() {
        TravelPath t= new TravelPath();
        t.endPoint = null  ;
        assertEquals(null, t.getEndPoint());

    }

    @Test
    void addLocation(Location location) {
        //TravelPath t= new TravelPath();
        //t.size = 0;
        //assertEquals(0, t.size);
    }

    @Test
    void clearPath() {
        TravelPath t= new TravelPath();
        t.clearPath();
        assertEquals(0, t.size);
        assertEquals(0, t.speed);
        assertEquals(0, t.travelledDistance);
        assertEquals(0, t.elapsedTime);
        assertEquals("Saved Path", t.savedName);
    }

    @Test
    void incrementSize() {
        TravelPath t= new TravelPath();
        t.incrementSize(50);   ;
        assertEquals(t.size, 50);
    }

    @Test
    void decrementSize() {
        TravelPath t= new TravelPath();
        t.decrementSize(0);   ;
        assertEquals(t.size, 0);
    }

    @Test
    void incrementTravelledDistance() {
        TravelPath t= new TravelPath();
        t.incrementTravelledDistance(5);   ;
        assertEquals(t.travelledDistance, 5);
    }

    @Test
    void decrementTravelledDistance() {
        TravelPath t= new TravelPath();
        t.incrementTravelledDistance(0);   ;
        assertEquals(t.travelledDistance, 0);
    }

    @Test
    void setStartPoint() {
        TravelPath t= new TravelPath();
        t.locationList = new ArrayList<Location>();
        assertEquals(t.getStartPoint(), t.getStartPoint());
    }

    @Test
    void setEndPoint() {
        TravelPath t= new TravelPath();
        t.endPoint = null  ;
        assertEquals(null, t.getEndPoint());

    }

    @Test
    void setSavedName() {
        TravelPath t= new TravelPath();
        t.setSavedName("Roy");
        assertEquals(t.savedName, "Roy");
    }

    @Test
    void setSavedFileName() {
        TravelPath t= new TravelPath();
        t.setSavedName("Roy_save");
        assertEquals(t.savedName, "Roy_save");
    }

    @Test
    void setEndTime() {
        TravelPath t= new TravelPath();
        t.setEndTime("50")   ;
        assertEquals("50", t.getEndTime());
    }

    @Test
    void setStartTime() {
        TravelPath t= new TravelPath();
        t.setStartTime("50");
        assertEquals("50", t.getStartTime());
    }

    @Test
    void describeContents() {
    }

    @Test
    void writeToParcel() {
    }
}