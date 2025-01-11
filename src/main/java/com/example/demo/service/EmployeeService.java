package com.example.demo.service;

import com.example.demo.model.Employee;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {

    private List<Employee> employees;

    @PostConstruct
    // This function will be called after the service bean is created & initialized.
    public void init() {
        // The method calls getEmployees() twice.
        // This demonstrates caching behavior by showing whether the fetchEmployeesFromSource()
        // is invoked multiple times.
        getEmployees(); // Execute fetchEmployeesFromSource() as the cache is empty.
        getEmployees(); // Retrieve the data from the cache without executing fetchEmployeesFromSource().
        this.employees = new ArrayList<>(Arrays.asList(
                new Employee(1, "sri"),
                new Employee(2, "ram"),
                new Employee(3, "raj")));
    }

    // Cacheable annotation indicates that the method result should be cached under the specified name.
    // If cache contains data for this method, method is skipped & cached result is returned.
    @Cacheable("employeesCache") // Specify the cache name
    public List<Employee> getEmployees() {
        // Simulated expensive API call or database query
        // Fetch employees from the source
        List<Employee> employees = fetchEmployeesFromSource();
        return employees;
    }

    private List<Employee> fetchEmployeesFromSource() {
        // Simulated implementation for fetching employees from source
        // This method will be invoked only if the cache is empty or invalidated
        // Replace it with your actual implementation
        // ...
        System.out.println("employees are fetched from resource");

        return employees;
    }

    // Function to update data for employee in the cache.
    @CachePut(value = "employeesCache", key = "#employee.id")
    public Employee updateEmployeeInCache(Employee employee) {
        // Simulate updating the employee in the source data.
        List<Employee> employees = fetchEmployeesFromSource();
        employees.removeIf(e -> e.getId() == employee.getId());
        employees.add(employee);

        System.out.println("Employee with ID: " + employee.getId() + " updated successfully :)");
        return employee;
    }
}
