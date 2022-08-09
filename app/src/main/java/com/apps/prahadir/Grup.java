package com.apps.prahadir;

public class Grup {
    private int id;
    private String name;

    public Grup(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Grup(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
