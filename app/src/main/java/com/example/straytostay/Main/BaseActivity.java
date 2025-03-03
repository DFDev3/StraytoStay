package com.example.straytostay;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.straytostay.Main.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView greetingText;
    private ImageButton logoutButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        greetingText = findViewById(R.id.greeting_text);
        logoutButton = findViewById(R.id.logout_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set greeting message dynamically
        if (user != null) {
            String userName = user.getDisplayName();
            if (userName == null || userName.isEmpty()) {
                userName = user.getEmail();  // Fallback to email
            }
            greetingText.setText("Hi, " + userName);
        }

        // Fix Logout Button Click Handling
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(BaseActivity.this, com.example.straytostay.StartUp.LoginActivity.class);
            startActivity(intent);
            finish();  // Close BaseActivity to prevent going back
        });

        // Bottom Navigation Click Handling
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Load Home Fragment by default if none is set
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        boolean isForward = true;

        if (item.getItemId() == R.id.nav_home) {
            selectedFragment = new HomeFragment();
            isForward = false;
        } else if (item.getItemId() == R.id.nav_find) {
            selectedFragment = new FindFragment();
            isForward = true;
        } else if (item.getItemId() == R.id.nav_tips) {
            selectedFragment = new TipsFragment();
            isForward = true;
        } else if (item.getItemId() == R.id.nav_shelter) {
            selectedFragment = new ShelterFragment();
            isForward = true;
        } else if (item.getItemId() == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
            isForward = true;
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment, isForward);
            return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment, boolean isForward) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        isForward ? R.anim.slide_in : R.anim.slide_in_reverse,
                        isForward ? R.anim.slide_out : R.anim.slide_out_reverse
                )
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
