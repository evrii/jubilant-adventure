package com.navigator.services;

import java.util.ArrayList;
import java.util.List;

import com.navigator.entities.Route;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class RouteService {

    RestTemplate restTemplate;
    HttpEntity <String> entity;


    public RouteService(WebClient.Builder wBuilder){
		restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://api-v3.mbta.com"));
    }

    public void getRoutes(){
		String body = restTemplate.getForObject("/routes?filter[type]=0,1&fields[route]=long_name", String.class);
        JSONObject json = new JSONObject(body);

        JSONArray jsonRoutes = json.getJSONArray("data");
        
        System.out.println("Say hello Route service! " + jsonRoutes.getJSONObject(0).toString());
        List<Route> routes = convertJsonArrayToRouteList(jsonRoutes);
        for(int routeIndex = 0; routeIndex<routes.size(); routeIndex++){
            System.out.println("What is my id? " + routes.get(routeIndex).getId());
            System.out.println("What is my longName? " + routes.get(routeIndex).getLongName());
        }

    }

    public List<Route> convertJsonArrayToRouteList(JSONArray jsonRouteArray){
        /*TODO do check on array and then set length to variable. */
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


}
