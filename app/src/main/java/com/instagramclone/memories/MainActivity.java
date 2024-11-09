package com.instagramclone.memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.instagramclone.memories.fragments.ComposeFragment;
import com.instagramclone.memories.fragments.HomeFragment;
import com.instagramclone.memories.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new ComposeFragment();
        //final Fragment fragment3 = new ProfileFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            if(item.getItemId() == R.id.action_home) {
                fragment = fragment1;
            } else if (item.getItemId() == R.id.action_compose) {
                fragment = fragment2;
            } else {
                fragment = fragment2;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}