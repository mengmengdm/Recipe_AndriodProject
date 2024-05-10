package com.example.demoproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.demoproject.connection.ConnectionRequest;

public class Recipe {

    private int idMeal;
    private int idReal;
    private String strName;
    private String imgMeal;
    private String strCategory;
    private String strArea;
    private Bitmap bitmap;
    private ConnectionRequest connectionRequest;
    private Bitmap returnBitmap;
    public Recipe(Context context){
        connectionRequest = new ConnectionRequest(context);

    }
    public Recipe(int idReal){
        this.idReal = idReal;
    }
    public String getImgUrl() {
        return imgMeal;
    }

    public String getIdMeal() {
        return String.valueOf(idMeal);
    }

    public String getIdReal() {
        return String.valueOf(idReal);
    }

    public String getName() {
        return strName;
    }

    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public void setIdReal(int idReal) {
        this.idReal = idReal;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public void setImgMeal(String imgMeal) {
        imgMeal = imgMeal.replace("|", "/");
        this.imgMeal = imgMeal;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }
    public void setBitmap(){
        connectionRequest.imageGetRequest(imgMeal,
                new ConnectionRequest.MyRequestCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap response) {
                        returnBitmap = response;
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("bitmap", "onError: "+error );
                    }
                });
    }
    public void setBitmap(String base64String){
        byte[] imageBytes = Base64.decode( base64String, Base64.DEFAULT );
        this.bitmap = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        Log.d("recipe", "setBitmap: "+String.valueOf(bitmap));
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }
}
