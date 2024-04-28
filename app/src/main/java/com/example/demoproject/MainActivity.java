package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.demoproject.fragments.DiscoverFragment;
import com.example.demoproject.fragments.MyRecipeFragment;
import com.example.demoproject.fragments.PostFragment;
import com.example.demoproject.fragments.ProfileFragment;
import com.example.demoproject.fragments.TodayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private DiscoverFragment discoverFragment;
    private MyRecipeFragment myRecipeFragment;
    private PostFragment postFragment;
    private ProfileFragment profileFragment;
    private TodayFragment todayFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}