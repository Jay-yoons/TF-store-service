package com.example.store.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StoreWebController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "UP");
        res.put("service", "store-service");
        return ResponseEntity.ok(res);
    }
}