package com.example.tracker;

public class SavePathEvent {
    TravelPath pathToSave;

    public SavePathEvent(TravelPath path){
     this.pathToSave = path;
    }

    public TravelPath getPathToSave(){
        return this.pathToSave;
    }
}
