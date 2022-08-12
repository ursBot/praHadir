package com.apps.prahadir;

public class Data {
    private final String nama;
    private boolean check;

    public Data(String nama, boolean check){
        this.nama = nama;
        this.check = check;
    }

    public String GetNama(){
        return this.nama;
    }
    public boolean GetSelected(){
        return this.check;
    }
    public void SetSelected(boolean selected){
        check = selected;
    }
}
