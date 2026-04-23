/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages campus rooms.
 */
@Path("/rooms")
public class SensorRoomResource {

    /**
     * Lists all registered rooms.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        return Response.ok(new ArrayList<>(DataStore.rooms.values())).build();
    }

    /**
     * Adds a new room. Checks for unique IDs.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        // ID existence check
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(400)
                    .entity("{\"error\":\"Room ID is required\"}")
                    .build();
        }
        
        // ID uniqueness check
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(409)
                    .entity("{\"error\":\"Room already exists\"}")
                    .build();
        }
        
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    /**
     * Returns details for a specific room.
     */
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) return Response.status(404).build();
        return Response.ok(room).build();
    }

    /**
     * Deletes a room if it has no sensors.
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        
        // Check if room exists
        if (room == null) return Response.status(404).build();
        
        // No deletion if room has active sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room has active sensors. Remove them first.");
        }
        
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}
