package com.example.straytostay.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.R;
import com.example.straytostay.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, lastNameInput, phoneInput, citizenIdInput, neighborhoodInput, emailInput, passwordInput;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView nameError, lastNameError, phoneError, emailError, passwordError, citizenidError, neighborhoodError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        nameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        phoneInput = findViewById(R.id.phoneInput);
        citizenIdInput = findViewById(R.id.idInput);
        neighborhoodInput = findViewById(R.id.neighborhoodInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String citizenid = citizenIdInput.getText().toString().trim();
        String neighborhood = neighborhoodInput.getText().toString().trim();

        // Hide previous errors
        nameError.setVisibility(View.GONE);
        lastNameError.setVisibility(View.GONE);
        phoneError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);


        boolean hasError = false;

        if (TextUtils.isEmpty(name)) {
            nameError.setText("First name is required");
            nameError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameError.setText("Last name is required");
            lastNameError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            phoneError.setText("Enter a valid phone number");
            phoneError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(citizenid)) {
            citizenidError.setText("Citizen ID is required");
            citizenidError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(neighborhood)) {
            neighborhoodError.setText("Neighborhood is required");
            neighborhoodError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setText("Enter a valid email address");
            emailError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordError.setText("Password must be at least 6 characters");
            passwordError.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (hasError) return;

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        emailError.setText(errorMessage);
                        emailError.setVisibility(View.VISIBLE);
                    }
                });
    }


}
