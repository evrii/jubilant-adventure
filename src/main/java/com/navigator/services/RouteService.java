package com.navigator.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    public String getFewestStops(List<Route> routes){
        int fewestStops = Integer.MAX_VALUE;
        String smallestRoutes = "";

        for (Route route : routes) {
            int routeStops = route.getStops().size();

            if(routeStops < fewestStops){
                fewestStops = routeStops;
                smallestRoutes = route.getLongName();
            } else if(routeStops == fewestStops){
                smallestRoutes += ", " + route.getLongName(); 
            }
        }

        return String.format("<b>Fewest Stops</b><br/>%d stops - %s", fewestStops, smallestRoutes);
    }

    public String getMostStops(List<Route> routes){
        int mostStops = Integer.MIN_VALUE;
        String biggestRoutes = "";

        for (Route route : routes) {
            int routeStops = route.getStops().size();

            if(routeStops > mostStops){
                mostStops = routeStops;
                biggestRoutes = route.getLongName();
            } else if(routeStops == mostStops){
                biggestRoutes += ", " + route.getLongName(); 
            }
        }

        return String.format("<b>Most Stops</b><br/>%d stops - %s", mostStops, biggestRoutes);
    }

    public String getStopsWithMultipleRoutes(List<Route> routes){
        HashMap<Stop,List<Route>> stopIntersections = new HashMap<Stop,List<Route>>();
        String output = "";
        for (Route currentRoute : routes) {
            List<Stop> currentStops = new ArrayList<Stop>(currentRoute.getStops());
            for(Stop currentStop : currentStops){
                if(stopIntersections.containsKey(currentStop)){
                    stopIntersections.get(currentStop).add(currentRoute);
                } else{
                    ArrayList<Route> stopRoutes = new ArrayList<Route>();
                    stopRoutes.add(currentRoute);
                    stopIntersections.put(currentStop, stopRoutes);
                }
            }
        }

        Set<Stop> stops = stopIntersections.keySet();
        for (Stop currentStop : stops) {
            List<Route> intersectingRoutes = stopIntersections.get(currentStop);
            if(intersectingRoutes.size() > 1){
                output += currentStop.getName() + " - ";
                for (int routeIndex = 0; routeIndex < intersectingRoutes.size(); routeIndex++) {
                    Route currentRoute = intersectingRoutes.get(routeIndex);
                    output += currentRoute.getLongName();
                    if(routeIndex < intersectingRoutes.size() -1){
                        output += ", ";
                    }
                }
                output += "<br/>";
            }
        }

        
        return String.format("<b>Stops connectiong two or more routes:</b><br/>%s", output);
    }

    public HashSet<Route> getIntersectingRoutes(List<Route> routes, Route currentRoute, HashSet<Route> excludedRoutes){
        HashSet<Route> intersectingRoutes = new HashSet<Route>();
        for (Route route : routes) {
            if(!route.equals(currentRoute) && !excludedRoutes.contains(route)){
                HashSet<Stop> intersectingStops = new HashSet<Stop>(route.getStops());
                intersectingStops.retainAll(currentRoute.getStops());
                if(intersectingStops.size()>0){
                    intersectingRoutes.add(route);
                }
            }
        }

        return intersectingRoutes;
    }

    public HashSet<Route> getRoutesContainingStop(List<Route> routes, String stopName){
        HashSet<Route> routesContainingStop = new HashSet<Route>();
        for (Route currentRoute : routes) {
            Set<Stop> currentStops = currentRoute.getStops();
            for (Stop currentStop : currentStops) {
                if(currentStop.getName().toUpperCase().contains(stopName.toUpperCase())){
                    routesContainingStop.add(currentRoute);
                }
            }
        }

        return routesContainingStop;
    }

    public List<Route> getPath(List<Route> routes, Route startingRoute, Route endingRoute, List<Route> currentRoute){
        List<Route> route = new ArrayList<Route>();
        route.addAll(currentRoute);
        route.add(startingRoute);

        if(startingRoute.equals(endingRoute)){
            return route;
        } else{
            HashSet<Stop> intersectingStops = new HashSet<Stop>(startingRoute.getStops());
            intersectingStops.retainAll(endingRoute.getStops());
            if(intersectingStops.size()>0){
                route.add(endingRoute);
                return route;
            } else{
                HashSet<Route> excludedRoutes = new HashSet<Route>(currentRoute);
                excludedRoutes.add(startingRoute);
                HashSet<Route> intersectingRoutes = getIntersectingRoutes(routes, startingRoute, excludedRoutes);
                if(intersectingRoutes.size() == 0){
                    return null;
                }

                Iterator<Route> intersectionRoutesIterator = intersectingRoutes.iterator();
                boolean foundPath = false;

                List<Route> proposedRoute = null;                
                while(intersectionRoutesIterator.hasNext() && !foundPath){
                    Route nextRoute = intersectionRoutesIterator.next();
                    proposedRoute = getPath(routes, nextRoute, endingRoute, route);
                    foundPath = proposedRoute != null;
                }
                return proposedRoute;

            }
        }
    }

    public String getItinerary(List<Route> routes, String startingStopName, String endingStopName){
        List<Route> path = new ArrayList<Route>();
        HashSet<Route> routesContainingStartingStop = getRoutesContainingStop(routes, startingStopName);
        HashSet<Route> routesContainingEndingStop = getRoutesContainingStop(routes, endingStopName);
        if(routesContainingStartingStop.size()>0 && routesContainingEndingStop.size()>0){

            HashSet<Route> commoRoutes = new HashSet<Route>(routesContainingStartingStop);
            commoRoutes.retainAll(routesContainingEndingStop);

            //If the stops are on the same path, set path to that single route, otherwise search recursively (getPath) for a connection.
            if(commoRoutes.size() > 0){
                path.add(commoRoutes.iterator().next());
            } else{
                Route startingRoute = routesContainingStartingStop.iterator().next();
                Route endingRoute = routesContainingEndingStop.iterator().next();
                path = getPath(routes, startingRoute, endingRoute, new ArrayList<Route>());
            }

        }

        String pathString = "";

        for (int pathIndex = 0; pathIndex < path.size(); pathIndex++) {
            Route currentRoute = path.get(pathIndex);
            pathString += currentRoute.getLongName();
            if(pathIndex < path.size()-1){
                pathString += ", ";
            }
        }

        return String.format("%s to %s -> %s", startingStopName, endingStopName, pathString);
    }

    private void populateStopsForRoutes(List<Route> routes){
        for (Route route : routes) {
            HashSet<Stop> routeStops = new HashSet<Stop>(stopService.getStops(route.getId()));
            route.setStops(routeStops);
        }
    }
}
