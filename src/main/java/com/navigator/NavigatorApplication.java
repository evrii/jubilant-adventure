package com.navigator;

import com.navigator.services.RouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NavigatorApplication {

	@Autowired
	RouteService routeService;

	public static void main(String[] args) {
		SpringApplication.run(NavigatorApplication.class, args);
	}

	@GetMapping("/info")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		routeService.getRoutes();
		return String.format("Hello %s!", "body");
	}
}