/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 * Exception thrown when a referenced resource (e.g., sensor in a room) is missing.
 */
public class LinkedResourceNotFoundException extends RuntimeException {
    
    /**
     * Creates exception with a descriptive message.
     */
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}