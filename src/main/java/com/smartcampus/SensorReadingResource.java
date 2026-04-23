/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Sub-resource handling readings for a specific sensor.
 */
public class SensorReadingResource {
    
    // The ID of the sensor this resource is currently focused on
    private final String sensorId;

    /**
     * Initializes resource with sensor context.
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Returns all readings for this sensor.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        List<SensorReading> list = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(list).build();
    }

    /**
     * Records a new reading. Fails if sensor is in MAINTENANCE.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        
        // Basic safety check
        if (sensor == null) return Response.status(404).build();
        
        // No readings allowed during maintenance
        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in MAINTENANCE mode.");
        }
        
        // System-managed fields
        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());
        
        // Store it in our in-memory history
        DataStore.readings.get(sensorId).add(reading);
        
        // Update the sensor's current value
        sensor.setCurrentValue(reading.getValue());
        
        return Response.status(201).entity(reading).build();
    }
}
