/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps SensorUnavailableException to an HTTP 403 Forbidden response.
 */
@Provider
public class SensorUnavailableExceptionMapper 
        implements ExceptionMapper<SensorUnavailableException> {
    
    /**
     * Converts the exception into a 403 JSON response.
     */
    @Override
    public Response toResponse(SensorUnavailableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "FORBIDDEN");
        error.put("message", ex.getMessage());
        
        return Response.status(403)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}