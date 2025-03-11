package com.example.straytostay.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.BaseActivity;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerUser, registerShelter, emailError, passwordError;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.email);
        emailError = findViewById(R.id.email_error);
        passwordInput = findViewById(R.id.password);
        passwordError = findViewById(R.id.password_error);
        loginButton = findViewById(R.id.login_button);
        registerUser = findViewById(R.id.register_user);
        registerShelter = findViewById(R.id.register_shelter);
        progressBar = findViewById(R.id.progress_bar);

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

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);

        if (TextUtils.isEmpty(email)) {
            emailError.setText("Email is required");
            emailError.setVisibility(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordError.setText("Password is required");
            passwordError.setVisibility(View.VISIBLE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, BaseActivity.class));

                        finish();
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

                        passwordError.setText(errorMessage);
                        passwordError.setVisibility(View.VISIBLE);
                    }
                });
    }



}