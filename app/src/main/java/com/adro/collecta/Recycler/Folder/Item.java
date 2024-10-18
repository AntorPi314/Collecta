package com.adro.collecta.Recycler.Folder;

public class Item {
    private String name;
    private String ms;

    public Item() {
    }

    public Item(String name, String ms) {
        this.name = name;
        this.ms = ms;
    }

    public String getName() {
        return name;
    }

    public String getMs() {
        return ms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }
}


