package com.apps.prahadir;

public class Grup {
    private final String nama;
    private final String owner;
    private final String id;

    public Grup(String nama, String owner, String id){
        this.nama = nama;
        this.owner = owner;
        this.id = id;
    }

    public String GetNama(){
        return this.nama;
    }
    public String GetOwner(){
        return this.owner;
    }
    public String GetID(){
        return this.id;
    }
}
