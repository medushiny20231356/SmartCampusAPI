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
 * Catch-all error handler that returns a JSON response instead of a stack trace.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    /**
     * Converts unhandled errors to a 500 JSON response.
     */
    @Override
    public Response toResponse(Throwable ex) {
        // Log the error internally (should add actual logging here in a real app)
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "INTERNAL_SERVER_ERROR");
        error.put("message", "An unexpected error occurred on the server.");
        
        return Response.status(500)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}