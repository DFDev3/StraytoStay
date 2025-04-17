package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.straytostay.R;

public class BaseAdoptanteActivity extends AppCompatActivity {

    private ImageView navHome, navPets, navEntities, navResources, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adop_base_activity); // The XML with LinearLayout and <include>s

        // Find all navbar icons
        navHome = findViewById(R.id.nav_home);
        navPets = findViewById(R.id.nav_pets);
        navEntities = findViewById(R.id.nav_entities);
        navResources = findViewById(R.id.nav_resources);
        navProfile = findViewById(R.id.nav_profile_adop);

        // Set default fragment (home)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Set navigation behavior
        navHome.setOnClickListener(v -> loadFragment(new HomeFragment()));
        navPets.setOnClickListener(v -> loadFragment(new FindFragment()));
        navEntities.setOnClickListener(v -> loadFragment(new ShelterFragment()));
        navResources.setOnClickListener(v -> loadFragment(new TipsFragment()));
        navProfile.setOnClickListener(v -> loadFragment(new ProfileFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
