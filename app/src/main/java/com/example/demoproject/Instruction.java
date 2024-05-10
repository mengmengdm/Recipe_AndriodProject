package com.example.demoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class Instruction {
    private int idMeal;
    private String intstruct;
    private int stepTime;
    public enum TimeScale{
        second,minute,hour
    }
    private TimeScale timeScale;
    private Bitmap stepImg;

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

    public int getStepTime() {
        return stepTime;
    }

    public void setStepTime(int stepTime) {
        this.stepTime = stepTime;
    }

    public TimeScale getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(TimeScale timeScale) {
        this.timeScale = timeScale;
    }

    public Bitmap getStepImg() {
        return stepImg;
    }

    public void setStepImg(String base64String) {
        byte[] imageBytes = Base64.decode( base64String, Base64.DEFAULT );
        this.stepImg = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        //Log.d("recipe", "setBitmap: "+String.valueOf(stepImg));
    }
}
