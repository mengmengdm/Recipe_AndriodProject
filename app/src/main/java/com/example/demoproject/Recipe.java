package com.example.demoproject;

public class Recipe {

    public String name;
    public String imgUrl;
    public Recipe(String name){
        this.name = name;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }
}
