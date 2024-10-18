package com.adro.collecta;

class Channel {
    private String name;
    private String description;
    private int position;

    public Channel(String name, String description, int position) {
        this.name = name;
        this.description = description;
        this.position = position;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPosition() { return position; }
}

