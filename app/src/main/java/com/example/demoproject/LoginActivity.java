package com.example.demoproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText emailAddress, password;
    TextView toSignupPage;

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
        emailAddress = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        toSignupPage = findViewById(R.id.to_signup_page);

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
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#5C827D"));
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        toSignupPage.setText(spannableString);
        toSignupPage.setMovementMethod(LinkMovementMethod.getInstance());
    }
}