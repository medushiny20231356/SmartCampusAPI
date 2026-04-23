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
 * Maps LinkedResourceNotFoundException to a 422 Unprocessable Entity response.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper 
        implements ExceptionMapper<LinkedResourceNotFoundException> {
    
    /**
     * Transforms the exception into a 422 JSON response.
     */
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "UNPROCESSABLE_ENTITY");
        error.put("message", ex.getMessage());
        
        return Response.status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}