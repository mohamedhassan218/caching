package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    // Endpoint to Get Cache Statistics.
    // Returns cache details such as keys, values, and other metadata as a JSON response.
    @GetMapping("/cache/stats")
    public Object getCacheStats() {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("employeesCache").getNativeCache();
        return cache.asMap();
    }

    @GetMapping("/test-admin")
    public String testAdmin() {
        return "Hello Admin";
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        // Ensure provided ID matches the employee's ID.
        if (id != updatedEmployee.getId())
            throw new IllegalArgumentException("IDs must match from the path and the request body ...");

        return employeeService.updateEmployeeInCache(updatedEmployee);
    }

    // To adds:
    // 1- Role-based cache access.          [Done]
    // 2- Data Update mechanism.            [Done]
    // 3- Unit tests to test this results.  [Done]
}
