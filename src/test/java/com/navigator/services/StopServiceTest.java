package com.navigator.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.navigator.entities.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class StopServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    StopService stopService;


    String stopsJsonString = "{" +
        "\"data\":["+ 
            "{" +
                "\"attributes\": {" +
                    "\"name\": \"First Stop\"" +
                "}," +
                "\"id\": \"123\"," +
                "\"links\": {" +
                    "\"self\": \"/stops/123\"" +
                "}," +
                "\"type\": \"stop\"" +
            "},"+
            "{" +
                "\"attributes\": {" +
                    "\"name\": \"Second Stop\"" +
                "}," +
                "\"id\": \"234\"," +
                "\"links\": {" +
                    "\"self\": \"/stops/234\"" +
                "}," +
                "\"type\": \"stop\"" +
            "},"+
            "{" +
                "\"attributes\": {" +
                    "\"name\": \"Third Stop\"" +
                "}," +
                "\"id\": \"345\"," +
                "\"links\": {" +
                    "\"self\": \"/stops/345\"" +
                "}," +
                "\"type\": \"stop\"" +
            "},"+
            "{" +
                "\"attributes\": {" +
                    "\"name\": \"Fourth Stop\"" +
                "}," +
                "\"id\": \"456\"," +
                "\"links\": {" +
                    "\"self\": \"/stops/123\"" +
                "}," +
                "\"type\": \"stop\"" +
            "}"+
        "]"+
    "}";

    JSONObject responsesJson;

    @BeforeEach
    void setup() throws JSONException{
        responsesJson = new JSONObject(stopsJsonString);
    }
    
    @Test
    void convertJsonObjectToStop() throws JSONException{
        JSONObject jsonRoute = responsesJson.getJSONArray("data").getJSONObject(0);
        Route outputRoute = stopService.convertJsonObjectToRoute(jsonRoute);
        assertEquals(outputRoute.getId(),"123");
        assertEquals(outputRoute.getName(),"First Stop");
    }

    @Test
    void testConvertJsonArrayToRouteList() throws JSONException{
        JSONArray stopsJSONArray = responsesJson.getJSONArray("data");
        List<Route> stops = stopService.convertJsonArrayToRouteList(stopsJSONArray);
        assertEquals(stops.size(),4);
        assertEquals(stops.get(0).getId(),"123");
        assertEquals(stops.get(0).getName(),"First Stop");
        assertEquals(stops.get(1).getId(),"234");
        assertEquals(stops.get(1).getName(),"Second Stop");
        assertEquals(stops.get(2).getId(),"345");
        assertEquals(stops.get(2).getName(),"Third Stop");
        assertEquals(stops.get(3).getId(),"456");
        assertEquals(stops.get(3).getName(),"Fourth Stop");
    }

    @Test
    void testThatGetRoutesReturnsTheCorrectListOfRoutes(){
        when(restTemplate.getForObject(anyString(), any())).thenReturn(stopsJsonString);
        List<Route> stops = stopService.getRoutes();
        assertEquals(stops.size(),4);
        assertEquals(stops.get(0).getId(),"123");
        assertEquals(stops.get(0).getName(),"First Stop");
        assertEquals(stops.get(1).getId(),"234");
        assertEquals(stops.get(1).getName(),"Second Stop");
        assertEquals(stops.get(2).getId(),"345");
        assertEquals(stops.get(2).getName(),"Third Stop");
        assertEquals(stops.get(3).getId(),"456");
        assertEquals(stops.get(3).getName(),"Fourth Stop");
    }
    
}
