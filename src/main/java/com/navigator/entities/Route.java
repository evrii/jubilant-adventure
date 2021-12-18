package com.navigator.entities;

import java.util.ArrayList;
import java.util.List;

public class Route{
    private String id;
    private String longName;
    private List<Stop> stops;
    public Route(String id, String longName){
        this.id = id;
        this.longName = longName;
        this.stops = new ArrayList<Stop>();
    }

    public String getId(){
        return this.id;
    }

    public String getLongName(){
        return this.longName;
    }

    public List<Stop> getStops(){
        return this.stops;
    }
}