package com.navigator.entities;

import java.util.HashSet;
import java.util.Set;

public class Route{
    private String id;
    private String longName;
    private Set<Stop> stops;
    public Route(String id, String longName){
        this.id = id;
        this.longName = longName;
        this.stops = new HashSet<Stop>();
    }

    public String getId(){
        return this.id;
    }

    public String getLongName(){
        return this.longName;
    }

    public Set<Stop> getStops(){
        return this.stops;
    }

    public void setStops(Set<Stop> stops){
        this.stops = stops;
    }
}