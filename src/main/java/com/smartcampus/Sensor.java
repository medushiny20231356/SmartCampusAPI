/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 * Data model for a Smart Campus sensor.
 */
public class Sensor {
    
    private String id; // Unique sensor ID
    
    private String type; // Type (e.g., "TEMPERATURE")
    
    private String status; // ACTIVE, MAINTENANCE, or OFFLINE
    
    private double currentValue; // Last recorded reading
    
    private String roomId; // ID of the room where installed

    /**
     * Standard getters and setters for JSON serialization.
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
