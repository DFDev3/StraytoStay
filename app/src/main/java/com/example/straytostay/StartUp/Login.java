package com.example.straytostay.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Main.Admin.BaseAdmin;
import com.example.straytostay.Main.Adoptante.BaseAdoptante;
import com.example.straytostay.Main.Shelter.BaseEntity;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
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
        ImageView eyeIcon = findViewById(R.id.eyeIcon);

        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordInput.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    // Mostrar contraseña
                    passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon.setImageResource(R.drawable.ic_eye_open);
                } else {
                    // Ocultar contraseña
                    passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIcon.setImageResource(R.drawable.ic_eye_closed);
                }
                // Mantiene el cursor al final del texto
                passwordInput.setSelection(passwordInput.getText().length());
            }
        });
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
                startActivity(new Intent(Login.this, RegisterUser.class));
            }
        });

        registerShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, RegisterEntity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            checkUserRole(uid);
        }
    }

    private void checkUserRole(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check users collection
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long adminId = documentSnapshot.getLong("adminId");
                routeUserByRole(adminId != null ? adminId.intValue() : 0);
            } else {
                // If not found in "users", check "shelters"
                db.collection("entities").document(uid).get().addOnSuccessListener(shelterSnapshot -> {
                    if (shelterSnapshot.exists()) {
                        routeUserByRole(1);
                    }
                });
            }
        });
    }


    private void routeUserByRole(int role) {
        Intent intent;

        switch (role) {
            case 0: // Adoptante
                intent = new Intent(this, BaseAdoptante.class);
                break;
            case 1: // Shelter
                intent = new Intent(this, BaseEntity.class);
                break;
            case 2: // Admin
                intent = new Intent(this, BaseAdmin.class);
                break;
            default:
                Toast.makeText(this, "Unknown user type.", Toast.LENGTH_SHORT).show();
                return;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void checkUserInCollection(String uid, String collection, Runnable onNotFound) {
        FirebaseFirestore.getInstance()
                .collection(collection)
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Long adminId = doc.getLong("adminId");
                        if (adminId != null) {
                            int role = adminId.intValue();
                            Log.d("LOGIN", "Found in " + collection + " with role: " + role);
                            routeUserByRole(role);
                        } else {
                            Toast.makeText(this, "Missing role info", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        onNotFound.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LOGIN", "Error checking collection " + collection, e);
                    onNotFound.run();
                });
    }


    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = firebaseUser.getUid();

                        checkUserInCollection(uid, "users", () ->
                                checkUserInCollection(uid, "entities", () -> {
                                            Toast.makeText(this, "User data not found in any collection", Toast.LENGTH_SHORT).show();
                                        }
                                )
                        );

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