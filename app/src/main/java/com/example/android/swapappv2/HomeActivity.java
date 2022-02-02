package com.example.android.swapappv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    static BottomNavigationView mBottomNavigationView;
    static String sCurrentUser; // current user logged in
    static String sSelectedUser; // selected user to view (ex. viewing profile page of item owner)
    static String sCurrentNotification; // notification currently being viewed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        // When HomeActivity is started, it opens ExploreFragment by default --> problem when rotated b/c HomeActivity will restart
        // and show ExploreFragment regardless of the menu tab the user is on

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, ExploreFragment.newInstance("", ""));
        fragmentTransaction.commit();

        View view = mBottomNavigationView.findViewById(R.id.navigation_explore);
        view.performClick();

        mBottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_post) {
                    Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(intent);
                } else {
                    switch (item.getItemId()) {
                        case R.id.navigation_explore:
                            selectedFragment = ExploreFragment.newInstance("", "");
                            break;
                        case R.id.navigation_notification:
                            selectedFragment = NotificationsFragment.newInstance("", "");
                            break;
                        case R.id.navigation_profile:
                            sSelectedUser = sCurrentUser;
                            selectedFragment = ProfileFragment.newInstance("", "");
                            break;
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, selectedFragment);
                    // transaction.commit();
                    transaction.commitAllowingStateLoss();
                }
                return true;
            }
        });
    }
}