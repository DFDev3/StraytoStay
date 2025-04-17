package com.example.straytostay.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Main.Admin.BaseAdminActivity;
import com.example.straytostay.Main.Shelter.BaseShelterActivity;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.example.straytostay.Main.Adoptante.BaseAdoptanteActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerUser, registerShelter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerUser = findViewById(R.id.signUpLink);
        registerShelter = findViewById(R.id.signUpLink2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        registerShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterShelterActivity.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is already logged in
            startActivity(new Intent(LoginActivity.this, BaseAdoptanteActivity.class));
            finish(); // prevent returning to login
        }
    }

    private void routeUserByRole(int role) {
        Intent intent;

        switch (role) {
            case 0: // Adoptante
                intent = new Intent(this, BaseAdoptanteActivity.class);
                break;
            case 1: // Shelter
                intent = new Intent(this, BaseShelterActivity.class);
                break;
            case 2: // Admin
                intent = new Intent(this, BaseAdminActivity.class);
                break;
            default:
                Toast.makeText(this, "Unknown user type.", Toast.LENGTH_SHORT).show();
                return;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = firebaseUser.getUid();

                        FirebaseFirestore.getInstance()
                                .collection("users") // or "shelters" depending on your structure
                                .document(uid)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        Long adminId = documentSnapshot.getLong("adminId");

                                        if (adminId != null) {
                                            int role = adminId.intValue();
                                            Log.e("YOOOOOOOOO", "rol: " + role);
                                            routeUserByRole(role);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Missing user role.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        String errorMessage;
                        Exception exception = task.getException();

                        if (exception != null) {
                            String errorCode = exception.getMessage();

                            if (errorCode.contains("badly formatted")) {
                                errorMessage = "Invalid email format";
                            } else if (errorCode.contains("password is invalid") || errorCode.contains("The supplied auth credential is incorrect")) {
                                errorMessage = "Incorrect password";
                            } else if (errorCode.contains("no user record corresponding")) {
                                errorMessage = "No account found with this email";
                            } else {
                                errorMessage = "Authentication failed. Please try again.";
                            }
                        } else {
                            errorMessage = "Unknown error occurred.";
                        }
                    }
                });
    }



}