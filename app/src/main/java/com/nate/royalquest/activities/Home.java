package com.nate.royalquest.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nate.royalquest.R;
import com.nate.royalquest.fragments.HomeFragment;
import com.nate.royalquest.fragments.LeaderboardFragment;
import com.nate.royalquest.fragments.LearnFragment;
import com.nate.royalquest.fragments.ProfileFragment;

public class Home extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        initViews();
        topAppBar.setTitle("Home");
        replaceFragment(new HomeFragment());

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if (itemID == R.id.home) {
                    replaceFragment(new HomeFragment());
                    topAppBar.setTitle("Home");
                    return true;
                } else if (itemID == R.id.leaderboard) {
                    replaceFragment(new LeaderboardFragment());
                    topAppBar.setTitle("Leaderboard");
                } else if (itemID == R.id.learn) {
                    replaceFragment(new LearnFragment());
                    topAppBar.setTitle("Learn");
                } else if (itemID == R.id.profile) {
                    replaceFragment(new ProfileFragment());
                    topAppBar.setTitle("Profile");
                }
                return true;
            }
            });
        }


    private void initViews(){
        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }
    
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}