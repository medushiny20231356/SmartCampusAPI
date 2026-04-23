/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 * Thrown when an action is attempted on an unavailable sensor (e.g., MAINTENANCE).
 */
public class SensorUnavailableException extends RuntimeException {
    
    /**
     * Creates exception with unavailability reason.
     */
    public SensorUnavailableException(String message) {
        super(message);
    }
}
