# Smart Campus Sensor & Room Management API

## Overview

This is a RESTful API built using **JAX-RS (Jersey 3.1.3)** deployed on **Apache Tomcat 10**
as a Maven Web Application. It manages Rooms and Sensors across a university Smart Campus,
providing endpoints to create, retrieve, and delete rooms and sensors, post sensor readings,
and maintain a full reading history per sensor.

### Technology Stack
- Java 11
- JAX-RS via Jersey 3.1.3
- Apache Tomcat 10
- Maven (WAR packaging)
- In-memory storage using ConcurrentHashMap (no database)

### API Base URL
```
http://localhost:8080/SmartCampusAPIs/api/v1
```

### Resource Structure
| Resource | Path |
|---|---|
| Discovery | GET /api/v1 |
| Rooms | /api/v1/rooms |
| Sensors | /api/v1/sensors |
| Readings | /api/v1/sensors/{sensorId}/readings |

---

## How to Build and Run

### Prerequisites
- JDK 11 or 17 installed
- Apache Maven installed
- Apache Tomcat 10 installed
- NetBeans IDE (recommended) or any IDE

### Step 1 — Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/SmartCampusAPIs.git
cd SmartCampusAPIs
```

### Step 2 — Build the Project
```bash
mvn clean package
```
This generates `SmartCampusAPIs.war` inside the `target/` folder.

### Step 3 — Deploy to Tomcat
Copy the WAR file to Tomcat's webapps folder:
```bash
cp target/SmartCampusAPIs.war /opt/tomcat/webapps/
```

### Step 4 — Start Tomcat
```bash
/opt/tomcat/bin/startup.sh
```

### Step 5 — Verify the Server is Running
Open your browser or Postman and visit:
```
http://localhost:8080/SmartCampusAPIs/api/v1
```
You should see a JSON response with API metadata.

### Alternative — Run Directly from NetBeans
1. Open the project in NetBeans
2. Right-click the project → **Clean and Build**
3. Right-click the project → **Run**
4. NetBeans will deploy to Tomcat automatically

---

## Sample curl Commands

### 1. Discovery Endpoint — Get API Metadata
```bash
curl -X GET http://localhost:8080/SmartCampusAPIs/api/v1
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/SmartCampusAPIs/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'
```

### 3. Get All Rooms
```bash
curl -X GET http://localhost:8080/SmartCampusAPIs/api/v1/rooms
```

### 4. Get a Specific Room by ID
```bash
curl -X GET http://localhost:8080/SmartCampusAPIs/api/v1/rooms/LIB-301
```

### 5. Create a Sensor (linked to a Room)
```bash
curl -X POST http://localhost:8080/SmartCampusAPIs/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","roomId":"LIB-301"}'
```

### 6. Get All Sensors Filtered by Type
```bash
curl -X GET "http://localhost:8080/SmartCampusAPIs/api/v1/sensors?type=Temperature"
```

### 7. Post a Sensor Reading
```bash
curl -X POST http://localhost:8080/SmartCampusAPIs/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":22.5}'
```

### 8. Get All Readings for a Sensor
```bash
curl -X GET http://localhost:8080/SmartCampusAPIs/api/v1/sensors/TEMP-001/readings
```

### 9. Delete a Room (fails if sensors exist — returns 409)
```bash
curl -X DELETE http://localhost:8080/SmartCampusAPIs/api/v1/rooms/LIB-301
```

---

### Part 5.5 — Filters vs Manual Logging
*(paste your answer here)*
