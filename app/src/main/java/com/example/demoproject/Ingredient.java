package com.example.demoproject;

import java.util.HashMap;

public class Ingredient implements Uploadable{
    private int idMeal;
    private int idIng;
    private String strIng;
    private String strAmount;
    private HashMap<String, String> uploadHashMap;

    public int getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public int getIdIng() {
        return idIng;
    }

    public void setIdIng(int idIng) {
        this.idIng = idIng;
    }

    public String getStrIng() {
        return strIng;
    }

    public void setStrIng(String strIng) {
        this.strIng = strIng;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    @Override
    public HashMap<String, String> getHashMap() {
        uploadHashMap.put("idmeal",String.valueOf(getIdMeal()));
        uploadHashMap.put("iding",String.valueOf(getIdIng()));
        uploadHashMap.put("string",String.valueOf(getStrIng()));
        uploadHashMap.put("stramount",String.valueOf(getStrAmount()));
        return uploadHashMap;
    }
}
