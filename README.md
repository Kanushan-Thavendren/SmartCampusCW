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

Questions & Answers

Part 1: Service Architecture & Setup

1. Project & Application Configuration
Question: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

Answer:
By default, JAX-RS resource classes follow a per-request lifecycle, meaning a new instance of the resource class is created for each incoming HTTP request. This ensures that instance variables within the resource class are not shared between requests, improving thread safety.
However, in this implementation, the data (such as rooms and sensors) is stored in static in-memory collections like ArrayList. These collections are shared across all requests and all instances of the resource classes.
Because multiple requests may access or modify these shared data structures simultaneously, there is a risk of race conditions or data inconsistency. For example, concurrent additions or deletions could lead to unexpected behavior.
To manage this, developers must ensure safe access to shared data, typically by using synchronized collections or thread-safe structures if the application were to scale. In this coursework, since the system is a simple in-memory simulation and not highly concurrent, basic collections are used, but in real-world applications, proper synchronization is essential.

2. The “Discovery” Endpoint
Question: Why is the provision of “Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

Answer:
Hypermedia, also known as HATEOAS (Hypermedia as the Engine of Application State), is a principle of REST where responses include links to related resources and actions. This allows clients to dynamically navigate the API without needing prior knowledge of all endpoints.
For example, instead of only returning data, the API could include links such as “view sensors for this room” or “add reading to this sensor.”
This approach benefits client developers by reducing dependency on static documentation. Clients can discover available operations at runtime by following links provided in responses. This makes the API more flexible and easier to evolve, as changes in endpoints can be communicated through updated links rather than requiring manual updates to client code.
Although this coursework does not fully implement HATEOAS, understanding it highlights how advanced REST APIs improve usability and maintainability.

Part 2: Room Management

3. Room Resource Implementation
Question: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

Answer:
Returning only room IDs reduces the size of the response, which improves network efficiency and reduces bandwidth usage. This is beneficial when dealing with large datasets, as smaller responses lead to faster transmission and lower latency.
However, returning only IDs requires the client to make additional requests to retrieve full details of each room. This increases the number of API calls and adds complexity on the client side.
On the other hand, returning full room objects provides complete information in a single response, making it easier for the client to process and display data without making additional requests. The trade-off is increased payload size, which may impact performance if the dataset is large.
In this implementation, full room objects are returned to simplify client interaction, as the dataset is small and manageable.

4. Room Deletion & Safety Logic
Question: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Answer:
Yes, the DELETE operation is idempotent in this implementation. Idempotency means that performing the same operation multiple times results in the same outcome as performing it once. In this API, when a DELETE request is sent for a room, if the room exists and has no associated sensors, it is successfully deleted. If the same DELETE request is sent again for the same room, the room will no longer exist, and the API will return a response indicating that the room was not found. The state of the system remains consistent after repeated requests, as the room is removed after the first successful deletion and no further changes occur on subsequent requests. 
Additionally, if the room contains sensors, the API prevents deletion and returns a 409 Conflict error, and this behavior also remains consistent across repeated requests, ensuring predictable and safe operation.

Part 3: Sensor Operations & Linking

5. Sensor Resource & Integrity
Question: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

Answer:
The @Consumes(MediaType.APPLICATION_JSON) annotation tells JAX-RS that the method only accepts request bodies in JSON format. If a client sends data using a different media type, such as text/plain or application/xml, JAX-RS will not find a suitable message body reader to convert that request into the required Java object.
As a result, the request is rejected before the method logic is executed. In practice, JAX-RS typically returns an HTTP 415 Unsupported Media Type response, indicating that the server does not accept the format sent by the client.
This is important because it ensures that the API processes only valid and expected input formats. It prevents incorrect parsing, reduces ambiguity, and enforces a consistent contract between the client and the server.

6. Filtered Retrieval & Search
Question: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

Answer:
Using @QueryParam for filtering is generally more appropriate because filtering does not identify a different resource; instead, it narrows down the results of the same resource collection. For example, /api/v1/sensors?type=CO2 still refers to the sensors collection, but requests only the subset that matches the given type. If the type were placed inside the path, such as /api/v1/sensors/type/CO2, it would imply that this represents a different resource hierarchy rather than a filtered view of the same collection, which can make the API less intuitive and less flexible.
Query parameters provide several advantages for filtering and searching. They clearly express optional conditions applied to a collection rather than defining a new resource. They also allow multiple filters to be combined easily in the future, such as filtering by both type and status. In addition, they help keep the resource path stable and meaningful, while aligning with common REST design conventions. Therefore, using @QueryParam offers a cleaner, more scalable, and more flexible approach for handling filtering and search operations in RESTful APIs.

Part 4: Deep Nesting with Sub-Resources

7. The Sub-Resource Locator Pattern
Question: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

Answer:
The Sub-Resource Locator pattern allows a parent resource to delegate the handling of nested paths to a separate class. In this implementation, the endpoint /sensors/{id}/readings is handled by a dedicated SensorReadingResource class instead of placing all the logic within the main SensorResource class. This approach improves the overall structure of the application by promoting separation of concerns, where each resource class is responsible for a specific part of the system. As a result, the code becomes easier to understand, maintain, and extend.
Furthermore, this design improves scalability, as new nested resources can be added without making a single class overly complex. It also enhances readability by avoiding large controller classes filled with multiple responsibilities. Instead, the logic is organized into smaller, focused classes, which makes debugging and future development more manageable. In contrast, defining all nested paths within one large controller would lead to tightly coupled code that is difficult to maintain and extend. Therefore, the use of sub-resource locators results in a cleaner, more modular, and maintainable API architecture.

Part 5: Advanced Error Handling, Exception Mapping & Logging

8. Dependency Validation (422 Unprocessable Entity)
Question: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

Answer:
HTTP 422 Unprocessable Entity is considered more appropriate than 404 Not Found in this scenario because the request itself is syntactically correct, but contains invalid data. In this implementation, when a client sends a valid JSON payload to create a sensor but provides a roomId that does not exist, the server is able to understand the request structure but cannot process it due to the invalid reference.
A 404 Not Found typically indicates that the requested resource itself does not exist at the given endpoint. However, in this case, the endpoint exists and the request is valid, but the issue lies within the data provided by the client. Therefore, using 422 more accurately reflects that the request could not be processed due to semantic errors in the input.

9. The Global Safety Net (500)
Question: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Answer:
Exposing internal Java stack traces to external users can pose significant security risks. Stack traces often reveal sensitive information about the internal structure of the application, such as class names, package structures, method calls, and file paths. This information can help an attacker understand how the system is designed and identify potential vulnerabilities.
Additionally, stack traces may expose details about the server environment, libraries being used, or specific error conditions. An attacker could use this information to craft targeted attacks, exploit known vulnerabilities, or perform reconnaissance on the system. By hiding stack traces and returning a generic error message through a global exception handler, the API reduces the risk of information leakage and improves overall security.

10. API Request & Response Logging Filters
Question: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Answer:
Using JAX-RS filters for logging is advantageous because it centralizes the logging logic in a single place rather than duplicating it across multiple resource methods. This reduces code duplication and ensures consistency in how requests and responses are logged.
Filters automatically intercept all incoming requests and outgoing responses, making it easier to implement logging without modifying each individual endpoint. This approach improves maintainability, as any changes to logging behavior can be made in one location. It also keeps the resource classes clean and focused on business logic, rather than mixing logging concerns with core functionality. Overall, using filters results in a more modular, scalable, and maintainable design.





