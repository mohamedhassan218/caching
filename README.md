# Employee Service with Caffeine Cache Enhancements

This project is a Spring Boot application that demonstrates a simple employee management service with caching implemented using Caffeine. The repository has been enhanced with new features to improve functionality and security as a way to implement what we learn.


## Features

### 1. Role-Based Cache Access
- **Description**: Access to cache statistics and related endpoints is **restricted** based on user roles.
- **Implementation**: Utilized Spring Security to define roles (`ADMIN` and `USER`) and restrict access to cache-related endpoints only to users with the `ADMIN` role.

### 2. Data Update Mechanism
- **Description**: Allows updating specific entries in the cache when the source data changes.
- **Implementation**: Introduced a new endpoint that uses Spring’s `@CachePut` annotation to update cache entries without invalidating the entire cache.

### 3. Integration Testing for Core Functionality
- **Description**: Implement unit tests to ensure the results of caching, security, and data update mechanisms.
- **Implementation**: Created end-to-end tests using `MockMvc` to simulate requests and validate responses for all key endpoints.


## Endpoints

### `/employees`
- **Method**: `GET`
- **Objective**: Fetches the list of employees.
- **Access**: Open to all **authenticated** users.

### `/employees/{id}`
- **Method**: `PUT`
- **Objective**: Updates a specific employee and also updates the cache.
- **Access**: Open to all **authenticated** users.

### `/cache/stats`
- **Method**: `GET`
- **Objective**: Provides statistics and details about the cache.
- **Access**: Restricted to authenticated users's with **`ADMIN`** role.


## Security

- **Roles**: 
  - `ADMIN`: Has full access, including cache statistics.
  - `USER`: Limited access, cannot view cache statistics.
- **Authentication**: Basic authentication is used for simplicity in this demonstration.


## Testing

End-to-end test is implemented to test our endpoints and check the results and status codes.


## Contributing

Fork the repository, make your enhancements, and open a pull request for review.
