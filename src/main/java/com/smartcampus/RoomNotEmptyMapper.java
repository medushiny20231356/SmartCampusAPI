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
 * Maps RoomNotEmptyException to an HTTP 409 Conflict response.
 */
@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    
    /**
     * Converts the exception into a JSON response.
     */
    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "CONFLICT");
        error.put("message", ex.getMessage());
        
        return Response.status(409)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
