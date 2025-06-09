package com.gamecraft.studios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            response.put("database", "PostgreSQL Connected");
            response.put("url", conn.getMetaData().getURL());
            response.put("driver", conn.getMetaData().getDriverName());
            response.put("status", "OK");
        } catch (Exception e) {
            response.put("database", "Connection Failed");
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
        }

        return response;
    }
}