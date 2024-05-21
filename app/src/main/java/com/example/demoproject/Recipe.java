package com.example.demoproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.demoproject.connection.ConnectionRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Recipe implements Parcelable,Uploadable {

    private int idMeal;
    private String strName;

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idMeal);
        dest.writeString(strName);
        dest.writeString(strCategory);
        dest.writeInt(numIng);
        dest.writeInt(numInst);
        dest.writeString(base64String);
    }

    @Override
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> uploadHashMap = new HashMap<>();
        uploadHashMap.put("idmeal",String.valueOf(getIdMeal()));
        uploadHashMap.put("strname",String.valueOf(getName()));
        uploadHashMap.put("strcategory",this.strCategory);
        uploadHashMap.put("numing",String.valueOf(getNumIng()));
        uploadHashMap.put("numist",String.valueOf(getNumInst()));
        uploadHashMap.put("bitimg",getBase64String());
        return uploadHashMap;
    }

    public enum Category{
        vegan, chicken, beef, fish
    }
    private String strCategory;
    private int numIng;
    private int numInst;
    private String base64String;

    public int getNumIng() {
        return numIng;
    }

    public void setNumIng(int numIng) {
        this.numIng = numIng;
    }

    public int getNumInst() {
        return numInst;
    }

    public void setNumInst(int numInst) {
        this.numInst = numInst;
    }

    private Bitmap bitmap;
    private ConnectionRequest connectionRequest;

    public Recipe(){

    }

    protected Recipe(Parcel in){
        idMeal = in.readInt();
        strName = in.readString();
        strCategory = in.readString();
        numIng = in.readInt();
        numInst = in.readInt();
        base64String = in.readString();
    }

    public int getIdMeal() {
        return idMeal;
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
        this.base64String = base64String;
        byte[] imageBytes = Base64.decode( base64String, Base64.DEFAULT );
        this.bitmap = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        Log.d("recipe", "setBitmap: "+String.valueOf(bitmap));
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        fromImgtoBase64();
    }
    public String fromImgtoBase64(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.base64String= Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("recipe", "fromImgtoBase64: "+this.base64String);
        return this.base64String;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public String getBase64String(){return base64String;}
}
