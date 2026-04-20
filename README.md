Smart Campus Sensor & Room Management API

Overview of the API design

The Smart Campus API is a RESTful web service developed using Java and JAX-RS. It is designed to manage campus facilities such as rooms, sensors, and sensor readings.
The API allows users to perform operations such as creating and retrieving rooms, assigning sensors to rooms, and recording sensor readings. It also supports filtering of sensors based on type and provides structured error handling using appropriate HTTP status codes.
The system follows REST principles by using standard HTTP methods (GET, POST, DELETE) and clear resource-based URLs. Additionally, sub-resource locators are used to represent hierarchical relationships, such as accessing readings of a specific sensor.

Build and Run Instructions

1.	Open the project in Apache NetBeans. 
2.	Make sure Apache Tomcat is configured and available in the Services tab. 
3.	Right-click the project and select Clean and Build to compile the source code and package the application. 
4.	Right-click the project again and select Run to deploy the application on Apache Tomcat. 
5.	Once the deployment is successful, open the API using the base URL below. 
http://localhost:8080/SmartCampusCW/api/v1

Sample curl Commands

1.	Get all rooms
curl -X GET
http://localhost:8080/SmartCampusCW/api/v1/rooms 

2.	Get a specific room by ID
curl -X GET
http://localhost:8080/SmartCampusCW/api/v1/rooms/LIB-301 

3.	Add a new room
curl -X POST
http://localhost:8080/SmartCampusCW/api/v1/rooms
-H "Content-Type: application/json"
-d "{"id":"SCI-101","name":"Science Lecture Room","capacity":60}" 

4.	Delete a room
curl -X DELETE
http://localhost:8080/SmartCampusCW/api/v1/rooms/ENG-201 

5.	Add a sensor to a room
curl -X POST
http://localhost:8080/SmartCampusCW/api/v1/sensors
-H "Content-Type: application/json"
-d "{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":25.5,"roomId":"LIB-301"}" 

6.	Get sensors with filtering
curl -X GET
http://localhost:8080/SmartCampusCW/api/v1/sensors?type=Temperature 

7.	Add a sensor reading
curl -X POST
http://localhost:8080/SmartCampusCW/api/v1/sensors/TEMP-001/readings
-H "Content-Type: application/json"
-d "{"id":"READ-001","timestamp":1713500000,"value":26.7}"





