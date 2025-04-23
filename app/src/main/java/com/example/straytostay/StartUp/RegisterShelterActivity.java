package com.example.straytostay.StartUp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterShelterActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;

    private EditText nameInput, addressInput, phoneInput, misionInput, nitInput, serviciosInput, productosInput, emailInput, passwordInput;
    private Button registerButton, selectImageButton;
    private ProgressBar progressBar;

    private RadioGroup typeSelector;
    private RadioButton radioRefugio, radioVeterinaria;
    private LinearLayout refugioLayout, veterinariaLayout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedType = "Refugio";

    private ImageView selectedImage;
    private Uri imageUri; // Para almacenar temporalmente la URI de la imagen seleccionada
    private String encodedImageBase64;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        typeSelector = findViewById(R.id.typeSelector);

        refugioLayout = findViewById(R.id.layoutRefugio);
        veterinariaLayout = findViewById(R.id.layoutVeterinaria);

        nameInput = findViewById(R.id.entity_name);
        addressInput = findViewById(R.id.entity_address);
        phoneInput = findViewById(R.id.entity_phone);
        misionInput = findViewById(R.id.shelter_mission);
        nitInput = findViewById(R.id.vet_nit);
        serviciosInput = findViewById(R.id.vet_services);
        productosInput = findViewById(R.id.vet_products);
        emailInput = findViewById(R.id.entity_email);
        passwordInput = findViewById(R.id.entity_password);

        selectedImage = findViewById(R.id.selectedImage);
        selectImageButton = findViewById(R.id.selectImageButton);

        registerButton = findViewById(R.id.shelter_register_button);
        progressBar = findViewById(R.id.progress_bar);

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

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        registerButton.setOnClickListener(v -> registerEntity());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            imageUri = data.getData();
            selectedImage.setImageURI(imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void registerEntity() {
        String name = nameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String nit = nitInput.getText().toString().trim();

        String mision = misionInput.getText().toString().trim();
        String servicios = serviciosInput.getText().toString().trim();
        String productos = productosInput.getText().toString().trim();

        ArrayList<String> serviciosList;

        if (!servicios.isEmpty()) {
            String[] serviciosArray = servicios.split("\\s*,\\s*"); // splits by comma and trims spaces
            serviciosList = new ArrayList<>(Arrays.asList(serviciosArray));
        } else {
            serviciosList = null;
        }

        ArrayList<String> productosList;

        if (!servicios.isEmpty()) {
            String[] productosArray = productos.split("\\s*,\\s*"); // splits by comma and trims spaces
            productosList = new ArrayList<>(Arrays.asList(productosArray));
        } else {
            productosList = null;
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Todos los campos obligatorios deben estar completos.", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = mAuth.getCurrentUser().getUid();
                Usuario entity;
                Log.d("||||||||||||||||||||", "NO ENTRÉ AL IF: " + selectedType);
                if (selectedType.equals("Refugio")) {
                    Log.d("||||||||||||||||||||", "ENTRÉ AL IF: " + selectedType);
                    Log.d("REVISION DE MISION", "A ver: " + mision);

                    entity = new Usuario(name, phone, address, email,1,uid,mision,encodedImageBase64);
                } else {
                    Log.d("||||||||||||||||||||", "ENTRÉ AL IF: " + selectedType);
                    entity = new Usuario(name, phone, address, email,1,uid,nit,serviciosList, productosList, encodedImageBase64);
                }

                db.collection("users").document(uid).set(entity).addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterShelterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterShelterActivity.this, "Error al guardar datos", Toast.LENGTH_SHORT).show();
                });

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterShelterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
