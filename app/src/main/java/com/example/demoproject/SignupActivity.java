package com.example.demoproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.demoproject.fragments.UserInformation;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText userName, emailAddress, password;
    TextView toLoginPage;
    Button signup;
    private static final String POST_URL = "https://studev.groept.be/api/a23pt214/insert_user_info/";
    private static final String GET_URL = "https://studev.groept.be/api/a23pt214/get_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Assignment
        userName = findViewById(R.id.signup_username);
        emailAddress = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        toLoginPage = findViewById(R.id.to_login_page);
        signup = findViewById(R.id.signup_button);

        // Set “LOG IN” Color As #5C827D
        String text = "ALREADY A USER? LOG IN";
        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#5C827D"));
        int startIndex = text.indexOf("LOG IN");
        int endIndex = startIndex + "LOG IN".length();
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        toLoginPage.setText(spannableString);

        //To Login Page
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.d("SignupActivity", "Attempting to start LoginActivity");
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Log.d("SignupActivity", "LoginActivity started");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#5C827D"));
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        toLoginPage.setText(spannableString);
        toLoginPage.setMovementMethod(LinkMovementMethod.getInstance());

        //Click to Sign Up
        signup.setOnClickListener(v -> signup());
    }

    //Show App Progress To Users
    private void snackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

    //Sign Up
    private void signup() {
        //Remove Spaces
        //Space Is Not A Valid Input
        String s_userName = userName.getText().toString().trim();
        String s_emailAddress = emailAddress.getText().toString().trim();
        String s_password = password.getText().toString().trim();

        //Check Whether Fields Are Empty
        if (TextUtils.isEmpty(s_userName) || TextUtils.isEmpty(s_emailAddress) || TextUtils.isEmpty(s_password)) {
            snackBar("Please Fill In All Fields");
        } else {
            checkUsername(s_userName);
        }
    }

    //Check If Username Exists
    private void checkUsername(String userName) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                GET_URL,
                null,
                response -> {
                    try {
                        boolean flag = false;
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);
                            if (userName.equals(curObject.getString("UserName"))) {
                                snackBar("User Name Already In Use");
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            submitInfo();
                        }
                    } catch (JSONException ignored) {
                    }
                },
                error -> snackBar("Network Error")
        );
        requestQueue.add(queueRequest);
    }

    private void submitInfo() {
        ProgressBar progressBar = findViewById(R.id.signup_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        UserInformation user = new UserInformation(userName.getText().toString(), emailAddress.getText().toString(), password.getText().toString());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                POST_URL,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    runOnUiThread(() -> {
                        snackBar("Sign Up Successful");
                        //Enter App In Two Seconds
                        findViewById(android.R.id.content).postDelayed(() -> {
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }, 2000);
                    });
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    runOnUiThread(() -> snackBar("Sign Up Unsuccessful: " + error.toString()));
                }
        ) {
            //Pass POST Parameters To Webservice
            @Override
            protected Map<String, String> getParams() {
                return user.getPostParameters();
            }
        };

        requestQueue.add(submitRequest);
    }

}