package com.apps.prahadir;

public class Data {
    private final String nama;
    private boolean select;

    public Data (String nama){
        this.nama = nama;
    }

    public String GetNama(){
        return this.nama;
    }

    public boolean isSelected() {
        return select;
    }

    public void setSelected(boolean selected) {
        select = selected;
    }
}
