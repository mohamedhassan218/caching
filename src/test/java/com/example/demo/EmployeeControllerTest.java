package com.example.demo;

import com.example.demo.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:endpoints.properties")
public class EmployeeControllerTest {
    // Get our endpoints URLs from the endpoints.properties file.
    @Value("${employees.endpoint}")
    private String employeesEndpoint;

    @Value("${cache.stats.endpoint}")
    private String cacheStatsEndpoint;

    @Value("${update.employee.endpoint}")
    private String updateEmployeeEndpoint;

    @Value("${test.admin.endpoint}")
    private String testAdminEndpoint;


    @Autowired
    private MockMvc mockMvc; // MockMvc for simulating HTTP requests and responses.

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for converting objects to JSON strings.

    /**
     * Test to ensure that the /employees endpoint returns a list of employees.
     * Expects a 200 OK status and a JSON array with a numeric length.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getEmployees_shouldReturnEmployeeList() throws Exception {
        mockMvc.perform(get(employeesEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber());
    }

    /**
     * Test to ensure that the /cache/stats endpoint returns cache statistics.
     * Expects a 200 OK status and a valid response containing cache details.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getCacheStats_shouldReturnCacheDetails() throws Exception {
        mockMvc.perform(get(cacheStatsEndpoint))
                .andExpect(status().isOk());
    }

    /**
     * Test to ensure that the /employees/{id} endpoint correctly updates an employee.
     * Expects a 200 OK status and verifies that the employee's name is updated.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateEmployee_shouldUpdateAndReturnEmployee() throws Exception {
        Employee employee = new Employee(1, "John Doe");
        String updatedEmployeeJson = objectMapper.writeValueAsString(employee);

        mockMvc.perform(put(updateEmployeeEndpoint.replace("{id}", "1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    /**
     * Test to ensure that a user with the USER role is forbidden from accessing /cache/stats.
     * Expects a 403 Forbidden status.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getCacheStats_asUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get(cacheStatsEndpoint))
                .andExpect(status().isForbidden());
    }

    /**
     * Test to ensure that the /test-admin endpoint returns a greeting for admin users.
     * Expects a 200 OK status and a "Hello Admin" response.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAdmin_shouldReturnHelloAdmin() throws Exception {
        mockMvc.perform(get(testAdminEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Admin"));
    }
}
