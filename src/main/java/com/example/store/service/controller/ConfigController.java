package com.example.store.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class ConfigController {

	@GetMapping
	public Map<String, String> root() {
		return Map.of("status", "ok");
	}

	@GetMapping("/healthz")
	public Map<String, String> healthz() {
		return Map.of("status", "healthy");
	}
}
