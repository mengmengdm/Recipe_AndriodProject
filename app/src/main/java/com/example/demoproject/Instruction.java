package com.example.demoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Instruction implements Uploadable, Parcelable {
    private int idMeal= 0;
    private int numStep= 0;
    private String intstruct = "";
    private String stepTime= "";
    private String timeScale= "";
    private Bitmap stepImg ;
    private String base64String= "";
    private HashMap<String, String> uploadHashMap;

    public Instruction() {
        uploadHashMap = new HashMap<>();
    }

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
    public String fromImgtoBase64(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.stepImg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.base64String= Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("instruction", "fromImgtoBase64: "+this.base64String);
        return this.base64String;
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

    public void setStepImg(Bitmap stepImg) {
        this.stepImg = stepImg;
        fromImgtoBase64();
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
    protected Instruction(Parcel in) {
        idMeal = in.readInt();
        numStep = in.readInt();
        intstruct = in.readString();
        stepTime = in.readString();
        timeScale = in.readString();
        base64String = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idMeal);
        dest.writeInt(numStep);
        dest.writeString(intstruct);
        dest.writeString(stepTime);
        dest.writeString(timeScale);
        dest.writeString(base64String);
    }
    public static final Creator<Instruction> CREATOR = new Creator<Instruction>() {
        @Override
        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        @Override
        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };
}
