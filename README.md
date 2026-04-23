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

## Curl Commands

### 1. Discovery Endpoint — Get API Metadata
```bash
curl -X GET http://localhost:8080/SmartCampusAPIs/api/v1
```
**Response (200 OK):**
```json
{
  "contact": "admin@smartcampus.ac.uk",
  "name": "Smart Campus Sensor & Room Management API",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  },
  "version": "1.0"
}
```

---

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
## Report — Answers to Specification Questions

### Part 1.1 — JAX-RS Resource Lifecycle
In JAX-RS, by default a new instance of a resource class is created for every incoming HTTP request. This is known as the per-request lifecycle. This means that instance variables inside resource classes like SensorRoomResource, SensorResource, and SensorReadingResource cannot be used to store shared data, because each request gets a fresh object and any data stored in instance variables would be lost immediately after the request completes.
To handle this, my implementation stores all shared data in the DataStore class using public static final fields specifically ConcurrentHashMap for rooms, sensors, and readings. Because these are static, they exist at the class level and are shared across every request regardless of how many resource instances are created. ConcurrentHashMap was chosen over a regular HashMap because it is thread-safe, meaning multiple simultaneous requests can read and write to the maps without causing race conditions or data corruption.


### Part 1.2 — HATEOAS

HATEOAS (Hypermedia as the Engine of Application State) is considered a hallmark of advanced RESTful design because it makes the API self-navigating. In the DiscoveryResource, the GET /api/v1 endpoint returns not just metadata like version and contact, but also a resources map that provides direct links to /api/v1/rooms and /api/v1/sensors. This means a client hitting the API for the first time can immediately discover all available resource collections without reading any external documentation.
Compared to static documentation, this approach benefits client developers because the API can evolve URLs can change or new resources can be added  and clients that follow the links dynamically will automatically adapt, rather than breaking because they had a URL hardcoded from outdated documentation.


### Part 2.1 — IDs vs Full Room Objects

In the getAllRooms() method in SensorRoomResource, the GET /api/v1/rooms endpoint returns full room objects by calling new ArrayList<>(DataStore.rooms.values()). This means the complete room data including id, name, capacity, and sensorIds list is returned in a single response.
If only IDs were returned instead, the response payload would be much smaller, reducing network bandwidth consumption. However, the client would then need to make a separate GET /{roomId} request for every single room to retrieve its details, which my implementation also supports via getRoom(@PathParam("roomId") String roomId). For a campus system managing thousands of rooms, this would result in thousands of additional API calls, increasing latency and server load significantly.
Returning full objects as my implementation does is better suited for clients like dashboards that need complete room information immediately in one call. Returning only IDs would suit scenarios where the client only needs to reference or count rooms without processing their full details, saving bandwidth in those cases.


### Part 2.2 — DELETE Idempotency

In the deleteRoom() method inside SensorRoomResource, the DELETE /{roomId} operation is idempotent in terms of server state, though the HTTP response code differs between calls.
On the first DELETE request for a room like LIB-301, the code calls DataStore.rooms.get(roomId) if the room exists and its sensorIds list is empty, it executes DataStore.rooms.remove(roomId) and returns 204 No Content. If the room still has sensors assigned, it throws RoomNotEmptyException returning 409 Conflict, blocking the deletion entirely.
If the same DELETE request is sent a second time, DataStore.rooms.get(roomId) returns null because the room was already removed. The code immediately hits if (room == null) return Response.status(404).build() and returns 404 Not Found without touching the DataStore at all.
Although the response codes differ (204 on first call, 404 on subsequent calls), the server state remains identical after both calls the room simply does not exist in the DataStore either way. This satisfies the REST definition of idempotency: no matter how many times the same DELETE request is repeated, the end state of the server is the same. No duplicate deletions or data corruption can occur.


### Part 3.1 — @Consumes Mismatch

In my createSensor() method in SensorResource, I have annotated the POST endpoint with @Consumes(MediaType.APPLICATION_JSON). This tells the JAX-RS runtime (Jersey) to only accept requests where the Content-Type header is set to application/json.
If a client sends a request with Content-Type: text/plain or Content-Type: application/xml, JAX-RS will automatically reject the request before it even reaches my createSensor() method. The runtime returns an HTTP 415 Unsupported Media Type response immediately. This means my roomId validation logic DataStore.rooms.containsKey(sensor.getRoomId()) is never executed, and no data is written to DataStore.sensors.
This is an important protection because without @Consumes, Jersey would attempt to deserialize the plain text or XML body into a Sensor object using the Jackson JSON deserializer, which would fail and throw an unhandled parsing exception. By declaring @Consumes(MediaType.APPLICATION_JSON) explicitly, the API enforces strict input validation at the framework level before any application logic runs.


### Part 3.2 — @QueryParam vs Path Parameter

In my getSensors() method in SensorResource, I implemented filtering using @QueryParam("type"). This means a client can call GET /api/v1/sensors?type=CO2 and my code executes result.removeIf(s -> !s.getType().equalsIgnoreCase(type)) to filter the results. If no type parameter is provided, type is null and the full list is returned unchanged.
This is superior to an alternative path-based design like /api/v1/sensors/type/CO2 for several reasons. First, query parameters are optional by nature my single getSensors() method handles both filtered and unfiltered requests without needing separate methods or paths. With path-based filtering, I would need a completely separate endpoint just for filtered results.
Second, /api/v1/sensors/type/CO2 would look like a nested resource in the URL hierarchy, implying type is a sub-resource of sensors rather than a filter on the collection, which is semantically incorrect. Third, query parameters are easily combinable for example, future filters like ?type=CO2 & status=ACTIVE can be added to my existing getSensors() method simply by adding more @QueryParam parameters, whereas path-based filtering would require defining a new URL pattern for every combination.


### Part 4.1 — Sub-Resource Locator Pattern

In my SensorResource class, the sub-resource locator method is implemented as:
“@Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }”
This method has no HTTP verb annotation (@GET, @POST) — it simply delegates all requests to /{sensorId}/readings to a dedicated SensorReadingResource instance, passing the sensorId through the constructor.
The key architectural benefit is separation of concerns. SensorResource only handles sensor-level operations (creating sensors and filtering them), while SensorReadingResource exclusively manages reading history for a specific sensor. Each class has a single, clearly defined responsibility.
If I had defined every path in one massive class such as handling GET /sensors, POST /sensors, GET /sensors/{id}/readings, and POST /sensors/{id}/readings all together the class would grow very large and become difficult to maintain, test, and debug. In a large campus API managing thousands of sensors, this would quickly become unmanageable. By delegating to SensorReadingResource, I can modify reading logic independently without touching sensor logic, and the code remains readable and modular.


### Part 5.2 — HTTP 422 vs 404

In my LinkedResourceNotFoundExceptionMapper, when a client POSTs a new sensor with a roomId that does not exist, my SensorResource throws LinkedResourceNotFoundException("Room not found: " + sensor.getRoomId()) and the mapper returns HTTP 422 Unprocessable Entity.
HTTP 404 would be semantically incorrect here because 404 means the requested URL endpoint was not found  but in this case, the endpoint POST /api/v1/sensors exists and was found perfectly. The problem is not with the URL but with the content inside the valid JSON payload specifically, the roomId field references a room that does not exist in DataStore.rooms.
HTTP 422 is more accurate because it communicates that the server understood the request structure and the JSON was valid, but was unable to process it due to a failed business rule the referenced room does not exist. This gives the client a clearer signal that they need to fix the data in their request body, not the URL they are calling.


### Part 5.4 — Stack Trace Security Risks

My GlobalExceptionMapper implements ExceptionMapper<Throwable>, acting as a catch-all safety net. Instead of allowing raw Java exceptions to propagate to the client, it intercepts every unexpected error such as NullPointerException or IndexOutOfBoundsException and returns a controlled JSON response with only "error": "INTERNAL_SERVER_ERROR" and "message": "An unexpected error occurred.".
From a cybersecurity standpoint, exposing raw Java stack traces would be extremely dangerous. An attacker could gather the following specific information from a stack trace: the exact class and package names of my application such as com.smartcampus.SensorResource, revealing the internal architecture; the exact file names and line numbers where the error occurred, making it trivial to target specific vulnerable code paths; the Jersey and Jakarta version numbers being used, allowing attackers to look up known CVEs for those exact versions; method signatures and call chains that reveal how the application processes data internally; and DataStore structure details that could expose how in-memory data is organized and accessed. By returning only a generic message, my GlobalExceptionMapper ensures none of this sensitive information is ever leaked to external API consumers.


### Part 5.5 — Filters vs Manual Logging

My LoggingFilter class implements both ContainerRequestFilter and ContainerResponseFilter in a single class annotated with @Provider. The request filter logs req.getMethod() and req.getUriInfo().getRequestUri() for every incoming request, and the response filter logs res.getStatus() for every outgoing response all automatically without touching any resource class.
This is far superior to manually inserting Logger.info() statements inside every resource method because it follows the DRY (Don't Repeat Yourself) principle. My API has multiple resource classes SensorRoomResource, SensorResource, and SensorReadingResource each with multiple methods. Manually adding logging to every single method would mean duplicating the same logging code across many places, making the codebase harder to maintain. If the logging format ever needed to change, every resource method would need to be updated individually.
With my LoggingFilter, logging is a cross-cutting concern handled in one place. It applies automatically to every request and response across the entire API without modifying any resource class, making the code cleaner, more maintainable, and ensuring no endpoint is accidentally left unlogged













