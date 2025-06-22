# Microservice-Architecture

## 🧠 Project Overview

This project is a complete microservices-based system built with Spring Boot and Spring Cloud. It includes real-world features like service-to-service communication, centralized configuration, security, and error handling.

### ✅ Key Features

- Microservices architecture using Spring Boot
- Service discovery with Eureka
- API Gateway with Spring Cloud Gateway
- Centralized configuration using Spring Cloud Config Server
- Inter-service communication using Feign Client and RestTemplate
- Circuit Breaker and Resilience4j integration
- MongoDB for data persistence
- OAuth2-based authentication using Okta
- CRUD operations with validation and exception handling

### 🧩 Microservices Included

1. **User Service** – Manages user data and profiles
2. **Hotel Service** – Handles hotel-related information
3. **Rating Service** – Stores and fetches ratings for hotels
4. **API Gateway** – Entry point for all services with routing and filters
5. **Service Registry** – Eureka server for service discovery
6. **Config Server** – Provides centralized configuration to all services

---

### 🚀 Getting Started

**Requirements:**
- Java 17+
- Maven
- MongoDB
- An Okta developer account

**Startup Order:**

1. Config Server  
2. Service Registry (Eureka)  
3. API Gateway  
4. User, Hotel, and Rating services  

---

## 🧩 Microservices Details

---

### 1. 🧑‍💼 User Service (`PORT: 8081`)

**Base URL:** `http://localhost:8081/`

**Description:**  
Handles user-related operations like creating, updating, and retrieving user data. It also communicates with the Rating and Hotel services to fetch full user details including their ratings and reviewed hotels.

**Technologies Used:**  
- Spring Boot  
- H2 Database  
- Feign Client (for inter-service calls)  
- OAuth2 Authorization (via Okta)

**Endpoints:**
- `GET    {base-url}/users`                    – Get all users
  ![image](https://github.com/user-attachments/assets/483a10dc-3f2d-4b97-b78f-f1f44c799fe1)
  
- `GET    {base-url}/users/{userId}`           – Get user by ID (with ratings and hotels)
  ![image](https://github.com/user-attachments/assets/fd7ee3c7-13c4-4aa6-be93-d6e06dd265fa)
  
- `POST   {base-url}/users`                    – Create a new user  
  ![image](https://github.com/user-attachments/assets/b9e02ef1-c12a-403a-a662-a87d821fdffe)


**Dependencies:**  
- Rating Service  
- Hotel Service

---

### 2. 🏨 Hotel Service (`PORT: 8082`) 
Note: If you want to restrict direct access to Hotel Service APIs at the controller level, you should use the @PreAuthorize annotation for method-level security.

**Base URL:** `http://localhost:8082`

**Description:**  
Manages hotel information, including hotel name, location, and details.

**Technologies Used:**  
- Spring Boot  
- H2 Database

**Endpoints:**
- `GET    {base-url}/hotels`                   – Get all hotels
  ![image](https://github.com/user-attachments/assets/304aef32-4ad8-46e0-ad7b-7b36332f5dfc)
 
- `GET    {base-url}/hotels/{hotelId}`         – Get hotel by ID
  ( I have used @PreAuthorize hence Directly we cannot access this API. If you want you can access via simply removing @PreAuthorize )
  
- `POST   {base-url}/hotels`                   – Create a new hotel  
  ![image](https://github.com/user-attachments/assets/547edce4-cef3-4f11-bcdf-fd8a56e9d982)


**Dependencies:**  
- None (accessed by Rating and User Services)

---

### 3. 🌟 Rating Service (`PORT: 8083`)
Note: If you want to restrict direct access to Rating Service APIs at the controller level, you should use the @PreAuthorize annotation for method-level security.
**Base URL:** `http://localhost:8083`

**Description:**  
Stores and provides ratings given by users for different hotels.

**Technologies Used:**  
- Spring Boot  
- MongoDB

**Endpoints:**
- `GET    {base-url}/ratings`                            – Get all ratings
  ![image](https://github.com/user-attachments/assets/6013544b-f0a2-4e6a-aecf-f5cb6d3cd1d4)

- `GET    {base-url}/ratings/users/{userId}`             – Get ratings by user ID
  ![image](https://github.com/user-attachments/assets/bd8d7f82-a4ac-469f-861a-1c0603852d59)

- `GET    {base-url}/ratings/hotels/{hotelId}`           – Get ratings for a hotel
  ![image](https://github.com/user-attachments/assets/90216d86-4240-4c69-902d-cfdf9282a45a)
  
- `POST   {base-url}/ratings`                            – Create a new rating
  ![image](https://github.com/user-attachments/assets/560950d3-c812-464b-b4d0-060d7af268d6)
  

**Dependencies:**  
- Hotel Service (for enriched data)

---

### 4. 🌐 API Gateway (`PORT: 8084`)

**Base URL:** `http://localhost:8084`

**Description:**  
Acts as a single entry point for all client requests. It routes requests to the appropriate microservice and applies global filters and authentication.

**Technologies Used:**  
- Spring Cloud Gateway  
- OAuth2 with Okta

**Routes Configured:**
- `{base-url}/users/**`   → routes to User Service
  ![image](https://github.com/user-attachments/assets/a52491c1-f9ae-4b34-a2e0-219f9374380e)

- `{base-url}/hotels/**`  → routes to Hotel Service
  ![image](https://github.com/user-attachments/assets/7486a278-5377-486b-a958-ae4688696785)
  
- `{base-url}/ratings/**` → routes to Rating Service
  ![image](https://github.com/user-attachments/assets/1c0ebb47-799c-488a-90d0-0d03d4cf9507)

**Features:**
- Path rewriting  
- Logging filters  
- OAuth2 Authentication

---

### 5. 📡 Service Registry (`PORT: 8761`)

**Base URL:** `http://localhost:8761`

**Description:**  
Service Discovery server using Eureka. All microservices register here to discover each other dynamically.

**Technologies Used:**  
- Spring Cloud Netflix Eureka

**Dashboard:**  
[http://localhost:8761](http://localhost:8761)

![image](https://github.com/user-attachments/assets/4374fde7-0b17-4633-9e3a-9622141e7ca0)


---

### 6. ⚙️ Config Server (`PORT: 8085`)

**Base URL:** `http://localhost:8085`

**Description:**  
Provides centralized and externalized configuration to all microservices from a remote Git repository.

**Technologies Used:**  
- Spring Cloud Config Server

**Configuration Source:**  
A Git repository containing `application.yml` or `application.properties` files for each microservice.

![image](https://github.com/user-attachments/assets/2cd14c4c-600a-471f-adfb-74967e66a3b1)

---

## Credits
This project was created by Yash Mehta 🚀




