/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configures the base path for SmartCampus REST API using JAX-RS.
 */
@ApplicationPath("/api/v1")
public class AppConfig extends Application {
    // JAX-RS automatically scans for resources in this package.
}
