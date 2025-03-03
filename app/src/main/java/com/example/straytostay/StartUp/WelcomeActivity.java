package com.example.straytostay.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.widget.ImageButton;

import com.example.straytostay.R;

public class WelcomeActivity extends AppCompatActivity {

    private final Handler hideHandler = new Handler();
    private final Runnable hideRunnable = this::hideSystemUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        hideSystemUI();
        setContentView(R.layout.activity_welcome);
        Button btnStart = findViewById(R.id.btnStart);
        ImageButton btnInsta = findViewById(R.id.btnWelcomeInsta);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnInsta.setOnClickListener(v -> openInstagramProfile());
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void openInstagramProfile() {
        String username = "denis.fedi";
        Uri uri = Uri.parse("http://instagram.com/_u/" + username);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent); // Try opening in the Instagram app
        } catch (ActivityNotFoundException e) {
            // If Instagram app is not installed, open in browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + username)));
        }
    }


    public void openInstagramProfile(View view) {
        String username = "instagram_username"; // Replace with the actual Instagram username
        Uri uri = Uri.parse("http://instagram.com/_u/" + username);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent); // Try opening in the Instagram app
        } catch (ActivityNotFoundException e) {
            // If Instagram app is not installed, open in browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + username)));
        }
    }
}
