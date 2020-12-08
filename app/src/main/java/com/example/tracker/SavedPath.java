package com.example.tracker;

import android.content.Context;
import android.util.Log;

import com.example.tracker.TravelPath;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedPath {

    /**
     * An array of TravelPath.
     */
    public static final List<TravelPath> ITEMS = new ArrayList<TravelPath>();

    /**
     * A map of TravelPath
     */
    public static final Map<Integer, TravelPath> ITEM_MAP = new HashMap<Integer, TravelPath>();

    private static int COUNT = 0;

    private static Context context;


    public static void addItem(TravelPath item) {
        ITEMS.add(item);
        ITEM_MAP.put(COUNT, item);
    }
    public static void removeItem(int pos){
        ITEMS.remove(pos);
        ITEM_MAP.remove(pos);
    }

    //TODO: read the files and load the saved paths
    public static void loadSavedPath(){

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("*.js");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        }
        catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


    }

}