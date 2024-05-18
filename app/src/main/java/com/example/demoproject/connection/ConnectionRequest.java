package com.example.demoproject.connection;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ConnectionRequest {
    private RequestQueue requestQueue;
    public ConnectionRequest(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void stringGetRequest(String url, final MyRequestCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // callback for success
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // callback for error
                callback.onError(error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
    public void jsonGetRequest(String url, final MyRequestCallback callback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    public void imageGetRequest(String url, final MyRequestCallback callback) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        callback.onSuccess(response);
                    }
                },
                0,
                0,
                CENTER_INSIDE,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                });
        requestQueue.add(imageRequest);
}
    public void uploadPostRequest(String url,HashMap<String, String> params,final MyRequestCallback callback){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                }) {
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
    public interface MyRequestCallback<T> {
        void onSuccess(T response);
        void onError(String error);
    }
}
