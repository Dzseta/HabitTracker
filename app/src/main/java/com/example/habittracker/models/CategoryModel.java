package com.example.habittracker.models;

public class CategoryModel {

    // variables
    private String icon;
    private String name;
    private String color;

    public CategoryModel(String icon, String name, String color) {
        this.icon = icon;
        this.name = name;
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
