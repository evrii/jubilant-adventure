package com.navigator.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
public class RouteServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    RouteService routeService;


    String routesJsonString = "{" +
        "\"data\":["+ 
            "{" +
                "\"attributes\": {" +
                    "\"long_name\": \"Purple Line\"" +
                "}," +
                "\"id\": \"Purple\"," +
                "\"links\": {" +
                    "\"self\": \"/routes/Purple\"" +
                "}," +
                "\"relationships\": {" +
                    "\"line\": {" +
                        "\"data\": {" +
                            "\"id\": \"line-Purple\"," +
                            "\"type\": \"line\"" +
                        "}" +
                    "}" +
                "}," +
                "\"type\": \"route\"" +
            "},"+
            "{" +
                "\"attributes\": {" +
                    "\"long_name\": \"Pink Line\"" +
                "}," +
                "\"id\": \"Pink\"," +
                "\"links\": {" +
                    "\"self\": \"/routes/Pink\"" +
                "}," +
                "\"relationships\": {" +
                    "\"line\": {" +
                        "\"data\": {" +
                            "\"id\": \"line-Pink\"," +
                            "\"type\": \"line\"" +
                        "}" +
                    "}" +
                "}," +
                "\"type\": \"route\"" +
            "},"+
            "{" +
                "\"attributes\": {" +
                    "\"long_name\": \"Olive Line\"" +
                "}," +
                "\"id\": \"Olive\"," +
                "\"links\": {" +
                    "\"self\": \"/routes/Olive\"" +
                "}," +
                "\"relationships\": {" +
                    "\"line\": {" +
                        "\"data\": {" +
                            "\"id\": \"line-Olive\"," +
                            "\"type\": \"line\"" +
                        "}" +
                    "}" +
                "}," +
                "\"type\": \"route\"" +
            "}"+
        "]"+
    "}";

    JSONObject responsesJson;

    @BeforeEach
    void setup() throws JSONException{
        responsesJson = new JSONObject(routesJsonString);
    }

    
    @Test
    void testConvertJsonObjectToRoute() throws JSONException{
        JSONObject jsonRoute = responsesJson.getJSONArray("data").getJSONObject(0);
        Route outputRoute = routeService.convertJsonObjectToRoute(jsonRoute);
        assertEquals(outputRoute.getId(),"Purple");
        assertEquals(outputRoute.getLongName(),"Purple Line");
    }

    @Test
    void testConvertJsonArrayToRouteList() throws JSONException{
        JSONArray routesJSONArray = responsesJson.getJSONArray("data");
        List<Route> routes = routeService.convertJsonArrayToRouteList(routesJSONArray);
        assertEquals(routes.size(),3);
        assertEquals(routes.get(0).getId(),"Purple");
        assertEquals(routes.get(0).getLongName(),"Purple Line");
        assertEquals(routes.get(1).getId(),"Pink");
        assertEquals(routes.get(1).getLongName(),"Pink Line");
        assertEquals(routes.get(2).getId(),"Olive");
        assertEquals(routes.get(2).getLongName(),"Olive Line");
    }

    // @Test
    // void testThatGetRoutesReturnsTheCorrectListOfRoutes(){
    //     when(restTemplate.getForObject(anyString(), any())).thenReturn(routesJsonString);
    //     List<Route> routes = routeService.getRoutes();
    //     assertEquals(routes.size(),3);
    //     assertEquals(routes.get(0).getId(),"Purple");
    //     assertEquals(routes.get(0).getLongName(),"Purple Line");
    //     assertEquals(routes.get(1).getId(),"Pink");
    //     assertEquals(routes.get(1).getLongName(),"Pink Line");
    //     assertEquals(routes.get(2).getId(),"Olive");
    //     assertEquals(routes.get(2).getLongName(),"Olive Line");
    // }

    @Test
    void testGetRouteInventory(){
        List<Route> routes = new ArrayList<Route>();
        routes.add(new Route("Red", "Red Line"));
        routes.add(new Route("Blue", "Blue Line"));
        routes.add(new Route("Yellow", "Yellow Line"));
        String routeInventory = routeService.getRouteInventory(routes);
        String expectedRouteInventory = "<b>Routes:</b><br/>Red Line<br/>Blue Line<br/>Yellow Line";
        assertEquals(routeInventory,expectedRouteInventory);
    }
}
