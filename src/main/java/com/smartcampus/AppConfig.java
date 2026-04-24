/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import com.smartcampus.smartcampusapis.resources.JakartaEE10Resource;

/**
 * Configures the base path for SmartCampus REST API using JAX-RS.
 */
@ApplicationPath("/api/v1")
public class AppConfig extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Resources
        classes.add(DiscoveryResource.class);
        classes.add(SensorResource.class);
        classes.add(SensorRoomResource.class);
        classes.add(JakartaEE10Resource.class);
        
        // Providers (Mappers & Filters)
        classes.add(GlobalExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(RoomNotEmptyMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(LoggingFilter.class);
        
        return classes;
    }
}
