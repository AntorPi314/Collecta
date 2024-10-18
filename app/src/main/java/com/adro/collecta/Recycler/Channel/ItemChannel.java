package com.adro.collecta.Recycler.Channel;

public class ItemChannel {
    private String name;
    private String des;
    private String imageLink;
    private String titleColor;
    private String desColor;
    private String backgroundColor;
    private String type;
    private String userID;

    public ItemChannel() {
    }

    public ItemChannel(String name, String des, String imageLink, String titleColor, String desColor, String backgroundColor, String type, String userID) {
        this.name = name;
        this.des = des;
        this.imageLink = imageLink;
        this.titleColor = titleColor;
        this.desColor = desColor;
        this.backgroundColor = backgroundColor;
        this.type = type;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public String getDesColor() {
        return desColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getType() {
        return type;
    }

    public String getUserID() {
        return userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public void setDesColor(String desColor) {
        this.desColor = desColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}




