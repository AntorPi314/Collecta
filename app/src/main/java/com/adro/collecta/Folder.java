package com.adro.collecta;

class Folder {
    private String name;
    private int position;

    public Folder(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() { return name; }
    public int getPosition() { return position; }
}
