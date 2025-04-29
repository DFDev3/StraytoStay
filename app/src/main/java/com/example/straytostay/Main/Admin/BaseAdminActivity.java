package com.example.straytostay.Main.Admin;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.straytostay.Main.Admin.TipsFragment;
import com.example.straytostay.Main.Admin.NewsFragment;
import com.example.straytostay.Main.Admin.AdminProfileFragment;
import com.example.straytostay.R;

public class BaseAdminActivity extends AppCompatActivity {

    private ImageButton navPets, navSeguimiento, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_base); // The XML with LinearLayout and <include>s

        // Find all navbar icons;
        navPets = findViewById(R.id.nav_adopcion);
        navSeguimiento = findViewById(R.id.nav_followup);
        navProfile = findViewById(R.id.nav_profile_shelt);

        // Set default fragment (home)
        if (savedInstanceState == null) {
            loadFragment(new NewsFragment());
        }

        // Set navigation behavior
        navPets.setOnClickListener(v -> loadFragment(new NewsFragment()));
        navSeguimiento.setOnClickListener(v -> loadFragment(new TipsFragment()));
        navProfile.setOnClickListener(v -> loadFragment(new AdminProfileFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
