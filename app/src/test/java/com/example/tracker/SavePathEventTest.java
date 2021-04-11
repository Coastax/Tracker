package com.example.tracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavePathEventTest {

    @Test
    void getPathToSave() {
        TravelPath y = null;
        SavePathEvent t= new SavePathEvent(y);
        assertEquals(y, t.getPathToSave());

    }
}