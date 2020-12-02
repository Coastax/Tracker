package com.example.tracker;

import com.example.tracker.TravelPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
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


    private static void addItem(TravelPath item) {
        ITEMS.add(item);
        ITEM_MAP.put(COUNT, item);
        item.setID(COUNT);
        COUNT++;
    }

    /*
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
     */

}