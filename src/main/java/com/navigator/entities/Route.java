package com.navigator.entities;

public class Route{
    private String id;
    private String longName;
    public Route(String id, String longName){
        this.id = id;
        this.longName = longName;
    }

    public String getId(){
        return this.id;
    }

    public String getLongName(){
        return this.longName;
    }
}