package com.example.straytostay.StartUp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Main.Adoptante.BaseActivity;
import com.example.straytostay.Main.Adoptante.HomeFragment;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterShelterActivity  extends AppCompatActivity {
    private EditText nameInput, addressInput, phoneInput, permisoIcaInput, nitInput, emailInput, passwordInput;
    private RadioGroup profitGroup, vetsGroup;
    private RadioButton selectedProfit, selectedVets;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shelter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize inputs
        nameInput = findViewById(R.id.shelter_name);
        addressInput = findViewById(R.id.shelter_address);
        phoneInput = findViewById(R.id.shelter_phone);
        permisoIcaInput = findViewById(R.id.shelter_permiso_ica);
        nitInput = findViewById(R.id.shelter_nit);
        emailInput = findViewById(R.id.shelter_email);
        passwordInput = findViewById(R.id.shelter_password);
        profitGroup = findViewById(R.id.shelter_profit_group);
        vetsGroup = findViewById(R.id.shelter_vets_group);
        registerButton = findViewById(R.id.shelter_register_button);
        progressBar = findViewById(R.id.progress_bar);

        registerButton.setOnClickListener(v -> registerShelter());
    }

    private void registerShelter() {
        String name = nameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String permisoICA = permisoIcaInput.getText().toString().trim();
        String nit = nitInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(permisoICA) || TextUtils.isEmpty(nit) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        if (profitGroup.getCheckedRadioButtonId() == -1 || vetsGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select Profit & Vets option", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isProfit = ((RadioButton) findViewById(profitGroup.getCheckedRadioButtonId())).getText().toString().equals("Profit");
        boolean hasVets = ((RadioButton) findViewById(vetsGroup.getCheckedRadioButtonId())).getText().toString().equals("Yes");

        progressBar.setVisibility(View.VISIBLE);

        // Register shelter in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String shelterId = mAuth.getCurrentUser().getUid();

                        // Store shelter data in Firestore with adminID = 2
                        Map<String, Object> shelterData = new HashMap<>();
                        shelterData.put("adminID", 1);
                        shelterData.put("name", name);
                        shelterData.put("address", address);
                        shelterData.put("phone", phone);
                        shelterData.put("permisoICA", permisoICA);
                        shelterData.put("NIT", nit);
                        shelterData.put("email", email);
                        shelterData.put("isProfit", isProfit);
                        shelterData.put("hasVets", hasVets);


                        db.collection("shelters").document(shelterId)
                                .set(shelterData)
                                .addOnSuccessListener(aVoid -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterShelterActivity.this, "Shelter Registered Successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterShelterActivity.this, BaseActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterShelterActivity.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterShelterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
