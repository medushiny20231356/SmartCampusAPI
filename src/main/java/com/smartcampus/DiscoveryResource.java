
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Root endpoint for API discovery and metadata.
 */
@Path("/")
public class DiscoveryResource {

    /**
     * Returns API discovery information.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> info = new HashMap<>();
        
        // API metadata
        info.put("version", "1.0");
        info.put("name", "Smart Campus Sensor & Room Management API");
        info.put("contact", "admin@smartcampus.ac.uk");
        
        // Resource links
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        info.put("resources", resources);
        
        return Response.ok(info).build();
    }
}
