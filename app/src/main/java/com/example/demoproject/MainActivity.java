package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DiscoverFragment discoverFragment;
    private MyRecipeFragment myRecipeFragment;
    private PostFragment postFragment;
    private ProfileFragment profileFragment;
    private TodayFragment todayFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}