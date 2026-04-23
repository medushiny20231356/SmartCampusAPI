/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Logs incoming API requests and outgoing responses.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    
    // Standard Java logger for this class
    private static final Logger LOG = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Logs the method and URI of incoming requests.
     */
    @Override
    public void filter(ContainerRequestContext req) {
        LOG.info("→ " + req.getMethod() + " " + req.getUriInfo().getRequestUri());
    }

    /**
     * Logs the HTTP status code of outgoing responses.
     */
    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        LOG.info("← Status: " + res.getStatus());
    }
}