package com.example.demoproject;
/*some useful information and apis
https://studev.groept.be/api/a23PT214
username: a23PT214
password: secret

apis:
https://studev.groept.be/api/a23PT214/test

gitlab page(andriod):
https://gitlab.kuleuven.be/
ssh：git@gitlab.kuleuven.be:groep-t/courses/programming-techniques/2324/ap-214.git
no password

github page(python for databse):
https://github.com/mengmengdm/javafinal
*/
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