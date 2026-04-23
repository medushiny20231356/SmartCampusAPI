/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import jakarta.ws.rs.QueryParam;
import java.util.List;

/**
 * Global sensor management endpoint.
 */
@Path("/sensors")
public class SensorResource {

    /**
     * Registers a new sensor and links it to a room.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
        // Room existence check
        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room not found: " + sensor.getRoomId());
        }
        
        // Store the sensor globally
        DataStore.sensors.put(sensor.getId(), sensor);
        
        // Associate sensor with room
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        
        // Prep reading history
        DataStore.readings.put(sensor.getId(), new ArrayList<>());
        
        return Response.status(201).entity(sensor).build();
    }

    /**
     * Returns sensors, optionally filtered by type.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> result = new ArrayList<>(DataStore.sensors.values());
        
        // Apply type filter if provided
        if (type != null && !type.isEmpty()) {
            result.removeIf(s -> !s.getType().equalsIgnoreCase(type));
        }
        
        return Response.ok(result).build();
    }

    /**
     * Handles readings for a specific sensor.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
