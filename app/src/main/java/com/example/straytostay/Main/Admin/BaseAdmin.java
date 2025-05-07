package com.example.straytostay.Main.Admin;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.straytostay.Classes.Helper;
import com.example.straytostay.R;

public class BaseAdmin extends AppCompatActivity {

    private ImageView navEntities, navTips, navAdminList, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_base_activity); // The XML with LinearLayout and <include>s

        // Find all navbar icons;
        navEntities = findViewById(R.id.nav_linked_entities);
        navTips = findViewById(R.id.nav_educational_content);
        navAdminList = findViewById(R.id.nav_admin_list);
        navProfile = findViewById(R.id.nav_admin_profile);

        // Set default fragment (home)
        if (savedInstanceState == null) {
            loadFragment(new EntitiesList());
        }

        // Set navigation behavior
        navEntities.setOnClickListener(v -> loadFragment(new EntitiesList()));
        navTips.setOnClickListener(v -> loadFragment(new PostTips()));
        navAdminList.setOnClickListener(v -> loadFragment(new Helper()));
        navProfile.setOnClickListener(v -> loadFragment(new AdminProfile()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_admin, fragment);
        transaction.commit();
    }
}
