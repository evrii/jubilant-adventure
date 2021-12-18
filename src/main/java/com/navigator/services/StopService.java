package com.navigator.services;

import java.util.ArrayList;
import java.util.List;

import com.navigator.entities.Stop;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class StopService {
    RestTemplate restTemplate;
    HttpEntity <String> entity;


    public StopService(){
		restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://api-v3.mbta.com"));
    }

    public List<Stop> getStops(){
		String body = restTemplate.getForObject("/stops?filter[route_type]=0,1&fields[stop]=name", String.class);
        JSONObject json = new JSONObject(body);
        JSONArray jsonStops = json.getJSONArray("data");

        List<Stop> stops = convertJsonArrayToStopList(jsonStops);

        return stops;

    }

    public List<Stop> convertJsonArrayToStopList(JSONArray jsonStopArray){
        List<Stop> stops = new ArrayList<Stop>();

        for(int stopIndex = 0; stopIndex<jsonStopArray.length(); stopIndex++){
            Stop currentStop = convertJsonObjectToStop(jsonStopArray.getJSONObject(stopIndex));
            stops.add(currentStop);
        }

        return stops;
    }

    public Stop convertJsonObjectToStop(JSONObject jsonStop){
        String id;
        String longName;

        id = jsonStop.getString("id");
        longName = jsonStop.getJSONObject("attributes").getString("name");
        return new Stop(id, longName);
    }

}
