package com.example.straytostay.StartUp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterShelterActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;

    private EditText nameInput, addressInput, phoneInput, misionInput, nitInput, emailInput, passwordInput;
    private Button registerButton, selectImageButton;
    private ProgressBar progressBar;

    private RadioGroup typeSelector;
    private RadioButton radioRefugio, radioVeterinaria;
    private LinearLayout refugioLayout, veterinariaLayout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedType = "Refugio";

    private ImageView selectedImage;
    private Uri imageUri;

    // CheckBoxes para servicios
    private CheckBox servicePeluqueria, serviceUrgencias, serviceVacunacion, serviceEsterilizacion,
            serviceCirugias, serviceConsultas, serviceCertificados;

    // CheckBoxes para productos
    private CheckBox productJuguetes, productRopa, productAccesorios, productComidas,
            productPremios, productHelados, productGuacales, productMedicamentos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        typeSelector = findViewById(R.id.typeSelector);
        radioRefugio = findViewById(R.id.radioRefugio);
        radioVeterinaria = findViewById(R.id.radioVeterinaria);

        refugioLayout = findViewById(R.id.layoutRefugio);
        veterinariaLayout = findViewById(R.id.layoutVeterinaria);

        nameInput = findViewById(R.id.entity_name);
        addressInput = findViewById(R.id.entity_address);
        phoneInput = findViewById(R.id.entity_phone);
        misionInput = findViewById(R.id.shelter_mission);
        nitInput = findViewById(R.id.vet_nit);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        selectedImage = findViewById(R.id.selectedImage);
        selectImageButton = findViewById(R.id.selectImageButton);

        registerButton = findViewById(R.id.shelter_register_button);
        progressBar = findViewById(R.id.progress_bar);

        // Servicios
        servicePeluqueria = findViewById(R.id.service_peluqueria);
        serviceUrgencias = findViewById(R.id.service_urgencias);
        serviceVacunacion = findViewById(R.id.service_vacunacion);
        serviceEsterilizacion = findViewById(R.id.service_esterilizacion);
        serviceCirugias = findViewById(R.id.service_cirugias);
        serviceConsultas = findViewById(R.id.service_consultas);
        serviceCertificados = findViewById(R.id.service_certificados);

        // Productos
        productJuguetes = findViewById(R.id.product_juguetes);
        productRopa = findViewById(R.id.product_ropa);
        productAccesorios = findViewById(R.id.product_accesorios);
        productComidas = findViewById(R.id.product_comidas);
        productPremios = findViewById(R.id.product_premios);
        productHelados = findViewById(R.id.product_helados);
        productGuacales = findViewById(R.id.product_guacales);
        productMedicamentos = findViewById(R.id.product_medicamentos);

        typeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioRefugio) {
                selectedType = "Refugio";
                refugioLayout.setVisibility(View.VISIBLE);
                veterinariaLayout.setVisibility(View.GONE);
            } else {
                selectedType = "Veterinaria";
                refugioLayout.setVisibility(View.GONE);
                veterinariaLayout.setVisibility(View.VISIBLE);
            }
        });

        registerButton.setOnClickListener(v -> registerEntity());
    }

    private void registerEntity() {
        String name = nameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = mAuth.getCurrentUser().getUid();

                Map<String, Object> userData = new HashMap<>();
                userData.put("nombre", name);
                userData.put("direccion", address);
                userData.put("telefono", phone);
                userData.put("email", email);
                userData.put("tipo", selectedType);

                if (selectedType.equals("Refugio")) {
                    String mision = misionInput.getText().toString().trim();
                    userData.put("mision", mision);
                } else {
                    String nit = nitInput.getText().toString().trim();
                    userData.put("nit", nit);
                    userData.put("servicios", getSelectedServices());
                    userData.put("productos", getSelectedProducts());
                }

                db.collection("entidades").document(userId).set(userData)
                        .addOnSuccessListener(unused -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show();
                        });

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> getSelectedServices() {
        List<String> services = new ArrayList<>();
        if (servicePeluqueria.isChecked()) services.add("Peluquería");
        if (serviceUrgencias.isChecked()) services.add("Urgencias");
        if (serviceVacunacion.isChecked()) services.add("Vacunación");
        if (serviceEsterilizacion.isChecked()) services.add("Esterilización");
        if (serviceCirugias.isChecked()) services.add("Cirugías");
        if (serviceConsultas.isChecked()) services.add("Consultas");
        if (serviceCertificados.isChecked()) services.add("Certificados");
        return services;
    }

    private List<String> getSelectedProducts() {
        List<String> products = new ArrayList<>();
        if (productJuguetes.isChecked()) products.add("Juguetes");
        if (productRopa.isChecked()) products.add("Ropa");
        if (productAccesorios.isChecked()) products.add("Accesorios");
        if (productComidas.isChecked()) products.add("Comidas");
        if (productPremios.isChecked()) products.add("Premios");
        if (productHelados.isChecked()) products.add("Helados");
        if (productGuacales.isChecked()) products.add("Guacales");
        if (productMedicamentos.isChecked()) products.add("Medicamentos");
        return products;
    }
}
