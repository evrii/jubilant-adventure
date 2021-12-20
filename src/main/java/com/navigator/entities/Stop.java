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

    @Override
    public boolean equals(Object otherObject){
        if(otherObject == null || this.getClass() != otherObject.getClass()){
            return false;
        }
        Stop otherStop = (Stop) otherObject;
        return this.id.equals(otherStop.getId()) && this.name.equals(otherStop.getName());
    }
}