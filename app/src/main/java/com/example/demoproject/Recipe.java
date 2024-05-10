package com.example.demoproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.demoproject.connection.ConnectionRequest;

public class Recipe {

    private int idMeal;
    private String strName;
    public enum Category{
        vegan, chicken, beef, fish
    }
    private String strCategory;
    private int numIng;

    private Bitmap bitmap;
    private ConnectionRequest connectionRequest;

    public Recipe(Context context){
        connectionRequest = new ConnectionRequest(context);

    }

    public String getIdMeal() {
        return String.valueOf(idMeal);
    }

    public String getName() {
        return strName;
    }

    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void setBitmap(String base64String){
        byte[] imageBytes = Base64.decode( base64String, Base64.DEFAULT );
        this.bitmap = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        Log.d("recipe", "setBitmap: "+String.valueOf(bitmap));
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

}
