package com.example.demoproject.connection;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ConnectionRequest {
    private RequestQueue requestQueue;
    public ConnectionRequest(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void makeGetRequest(String url, final MyRequestCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 请求成功时调用回调函数
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 请求失败时调用回调函数
                callback.onError(error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    public interface MyRequestCallback {
        void onSuccess(String response);
        void onError(String error);
    }
}
