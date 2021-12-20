package com.navigator.entities;

public class Stop{
    private String id;
    private String name;
    public Stop(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }
}