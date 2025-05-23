package com.example.straytostay.Main.Shelter;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.straytostay.R;

public class BaseEntity extends AppCompatActivity {

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
            loadFragment(new PostedPetFragment());
        }

        // Set navigation behavior
        navPets.setOnClickListener(v -> loadFragment(new PostedPetFragment()));
        navSeguimiento.setOnClickListener(v -> loadFragment(new AdoptedList()));
        navProfile.setOnClickListener(v -> loadFragment(new EntityProfile()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
