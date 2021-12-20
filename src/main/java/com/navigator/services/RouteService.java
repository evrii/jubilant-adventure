package com.navigator.services;

import java.util.ArrayList;
import java.util.List;

import com.navigator.entities.Route;
import com.navigator.entities.Stop;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class RouteService {
    RestTemplate restTemplate;
    HttpEntity <String> entity;

    @Autowired
    StopService stopService;

    public RouteService(){
		restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://api-v3.mbta.com"));
    }

    public List<Route> getRoutes(){
		String body = restTemplate.getForObject("/routes?filter[type]=0,1&fields[route]=long_name", String.class);
        JSONObject json = new JSONObject(body);
        JSONArray jsonRoutes = json.getJSONArray("data");

        List<Route> routes = convertJsonArrayToRouteList(jsonRoutes);
        populateStopsForRoutes(routes);

        return routes;
    }

    public List<Route> convertJsonArrayToRouteList(JSONArray jsonRouteArray){
        List<Route> routes = new ArrayList<Route>();

        for(int routeIndex = 0; routeIndex<jsonRouteArray.length(); routeIndex++){
            Route currentRoute = convertJsonObjectToRoute(jsonRouteArray.getJSONObject(routeIndex));
            routes.add(currentRoute);
        }

        return routes;
    }

    public Route convertJsonObjectToRoute(JSONObject jsonRoute){
        String id;
        String longName;

        id = jsonRoute.getString("id");
        longName = jsonRoute.getJSONObject("attributes").getString("long_name");
        return new Route(id, longName);
    }

    public String getRouteInventory(List<Route> routes){
        String inventory = "<b>Routes:</b><br/>";

        for (int routeIndex = 0; routeIndex < routes.size(); routeIndex++) {
            inventory += routes.get(routeIndex).getLongName();
            if(routeIndex<(routes.size()-1)){
                inventory += "<br/>";
            }
        }
        return inventory;
    }

    private void populateStopsForRoutes(List<Route> routes){
        for (Route route : routes) {
            List<Stop> routeStops = stopService.getStops(route.getId());
            route.setStops(routeStops);
        }
    }
}
