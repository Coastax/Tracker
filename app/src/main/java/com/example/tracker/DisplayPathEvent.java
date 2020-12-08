package com.example.tracker;

public class DisplayPathEvent {
    TravelPath pathToDisplay;

    public DisplayPathEvent(TravelPath path){
        this.pathToDisplay = path;
    }

    public TravelPath getPathToDisplay(){
        return this.pathToDisplay;
    }
}