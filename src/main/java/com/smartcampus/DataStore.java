/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Temporary in-memory database using thread-safe ConcurrentHashMaps.
 */
public class DataStore {
    
    // Rooms indexed by room ID.
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    
    // Sensors indexed by sensor ID.
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    
    // Historical sensor readings by sensor ID.
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}