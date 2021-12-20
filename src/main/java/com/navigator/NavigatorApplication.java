package com.navigator;

import java.util.List;

import javax.annotation.PostConstruct;

import com.navigator.entities.Route;
import com.navigator.services.RouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NavigatorApplication {

	@Autowired
	RouteService routeService;

	List<Route> routes;

	public static void main(String[] args) {
		SpringApplication.run(NavigatorApplication.class, args);
		
		
	}

	@PostConstruct
	public void setup(){
		routes = routeService.getRoutes();
	}

	@GetMapping("/1")
	public String questionOne() {
		String inventory = routeService.getRouteInventory(routes);
		return inventory;
	}

	@GetMapping("/2")
	public String questionTwo() {
		String leastStops = routeService.getFewestStops(routes);
		String mostStops = routeService.getMostStops(routes);
		String intersections = routeService.getIntersections(routes);
		return String.format("%s<br/><br/>%s<br/><br/>%s",leastStops,mostStops,intersections);
	}
}