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
import com.android.volley.toolbox.Volley;
import com.example.demoproject.fragments.ProfileFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    EditText userName, password;
    TextView toSignupPage;
    Button login;
    private static final String GET_URL = "https://studev.groept.be/api/a23pt214/get_user_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Assignment
        userName = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        toSignupPage = findViewById(R.id.to_signup_page);
        login = findViewById(R.id.login_button);

        findViewById(android.R.id.content).post(this::setupClickableSpan);

        //Click to Log In
        login.setOnClickListener(v -> login());
    }

    private void setupClickableSpan(){
        // Set “SIGN UP” Color As #5C827D
        String text = "NOT A USER? SIGN UP";
        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#5C827D"));
        int startIndex = text.indexOf("SIGN UP");
        int endIndex = startIndex + "SIGN UP".length();
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        toSignupPage.setText(spannableString);

        //To Signup Page
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#5C827D"));
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        toSignupPage.setText(spannableString);
        toSignupPage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Show App Progress To Users
    private void snackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

    //Log In
    private void login() {
        //Remove Spaces
        //Space Is Not A Valid Input
        String s_userName = userName.getText().toString().trim();
        String s_password = password.getText().toString().trim();

        //Check Whether Fields Are Empty
        if (TextUtils.isEmpty(s_userName) || TextUtils.isEmpty(s_password)) {
            snackBar("Please Fill In All Fields");
        } else {
            checkPassword();
        }
    }

    //Check If Password Correct
    private void checkPassword() {
        ProgressBar progressBar = findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                GET_URL,
                null,
                response -> {
                    try {
                        //A Flag To Check If Username Exists
                        boolean userExist = false;
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);
                            String Username = curObject.getString("UserName");
                            String Emailaddress = curObject.getString("EmailAddress");
                            String hashedPassword = curObject.getString("Password");
                            String Userid = curObject.getString("Id");

                            //User Exists
                            if(userName.getText().toString().equals(Username)){
                                userExist = true;
                                //Use BCrypt Method To Check Password
                                if (BCrypt.checkpw(password.getText().toString(), hashedPassword)) {
                                    progressBar.setVisibility(View.GONE);
                                    snackBar("Log In Successful");
                                    //Enter App In Two Seconds
                                    findViewById(android.R.id.content).postDelayed(() -> {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        //Pass Data To MainActivity
                                        intent.putExtra("username", Username);
                                        intent.putExtra("emailaddress", Emailaddress);
                                        intent.putExtra("userid", Userid);
                                        startActivity(intent);
                                        finish();
                                    }, 2000);
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    snackBar("Please Enter Correct Password");
                                }
                            }
                        }
                        //User Does Not Exist
                        if(!userExist){
                            progressBar.setVisibility(View.GONE);
                            snackBar("Please Sign Up");
                        }
                    } catch (JSONException ignored) {
                    }
                },
                error -> {progressBar.setVisibility(View.GONE);snackBar("Network Error");}
        );
        requestQueue.add(queueRequest);
    }
}