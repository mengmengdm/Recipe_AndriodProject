package com.example.demoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.util.HashMap;

public class Instruction implements Uploadable{
    private int idMeal;
    private int numStep;

    private String intstruct;
    private String stepTime;
    private String timeScale;
    private Bitmap stepImg;
    private String base64String;
    private HashMap<String, String> uploadHashMap;

    public int getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public String getIntstruct() {
        return intstruct;
    }

    public void setIntstruct(String intstruct) {
        this.intstruct = intstruct;
    }

    public String getStepTime() {
        return stepTime;
    }

    public void setStepTime(String stepTime) {
        if (stepTime.equals("null") ){
            this.stepTime = "";
        }
        else{
            this.stepTime = stepTime;
        }
    }

    public String getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(String timeScale) {
        this.timeScale = timeScale;
    }

    public Bitmap getStepImg() {
        return stepImg;
    }

    public void setStepImg(String base64String) {
        this.base64String = base64String;
        byte[] imageBytes = Base64.decode( base64String, Base64.DEFAULT );
        this.stepImg = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        //Log.d("recipe", "setBitmap: "+String.valueOf(stepImg));
    }
    public int getNumStep() {
        return numStep;
    }

    public void setNumStep(int numStep) {
        this.numStep = numStep;
    }

    public String getBase64String() {
        return base64String;
    }

    @Override
    public HashMap<String, String> getHashMap() {
        uploadHashMap.put("idmeal",String.valueOf(getIdMeal()));
        uploadHashMap.put("numstp",String.valueOf(getNumStep()));
        uploadHashMap.put("strinst",String.valueOf(getIntstruct()));
        uploadHashMap.put("steptime",String.valueOf(getStepTime()));
        uploadHashMap.put("timescale",String.valueOf(getTimeScale()));
        uploadHashMap.put("stepimg",getBase64String());
        return uploadHashMap;
    }
}
