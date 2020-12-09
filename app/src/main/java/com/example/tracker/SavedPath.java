package com.example.tracker;

import android.content.Context;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import com.example.tracker.TravelPath;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
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
        String path = context.getFilesDir() + "/SavedPath/";
        String filename = ITEMS.get(pos).getSavedFileName();
        File file = new File(path + filename);
        if(file.exists()) {
            file.delete();
        }
        else
            Log.w("Delete Path", "File does not exist");
        ITEMS.remove(pos);
        ITEM_MAP.remove(pos);
    }

    public static void loadSavedPath(){

        String path = context.getFilesDir() + "/SavedPath/";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            Log.d("Files", "Size: "+ files.length);
            Gson gson = new Gson();

            for (int i = 0; i < files.length; i++)
            {
                //Log.d("Files", "FileName:" + files[i].getName());
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
                    TravelPath pathToLoad = gson.fromJson(bufferedReader, TravelPath.class);
                    //Log.d("Loaded Path", pathToLoad.getSavedName());
                    SavedPath.addItem(pathToLoad);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void setContext(Context context) {
        SavedPath.context = context;
    }
}