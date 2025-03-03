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
        nameError = findViewById(R.id.name_error);
        lastNameError = findViewById(R.id.lastname_error);
        phoneError = findViewById(R.id.phone_error);
        emailError = findViewById(R.id.email_error);
        passwordError = findViewById(R.id.password_error);
        citizenidError = findViewById(R.id.citizenid_error);
        neighborhoodError = findViewById(R.id.neighborhood_error);
        nameInput = findViewById(R.id.name);
        lastNameInput = findViewById(R.id.last_name);
        phoneInput = findViewById(R.id.phone);
        citizenIdInput = findViewById(R.id.citizen_id);
        neighborhoodInput = findViewById(R.id.neighborhood);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progress_bar);

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
