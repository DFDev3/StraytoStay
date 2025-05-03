package com.example.straytostay.StartUp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Classes.Entity;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterEntity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;

    private EditText nameInput, addressInput, mainPhoneInput, websiteInput, misionInput, nitInput, serviciosInput, productosInput, emailInput, passwordInput;
    private Button registerButton, selectImageButton;
    private ProgressBar progressBar;
    private LinearLayout phoneContainer;
    private ImageButton addPhoneBtn;

    private RadioGroup typeSelector;
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
        mainPhoneInput = findViewById(R.id.entity_phone);
        misionInput = findViewById(R.id.shelter_mission);
        nitInput = findViewById(R.id.vet_nit);
        serviciosInput = findViewById(R.id.vet_services);
        productosInput = findViewById(R.id.vet_products);
        emailInput = findViewById(R.id.entity_email);
        passwordInput = findViewById(R.id.entity_password);
        websiteInput = findViewById(R.id.entity_website);

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

        phoneContainer = findViewById(R.id.phoneContainer); // Agrega esto si usas varios teléfonos dinámicamente
        addPhoneBtn = findViewById(R.id.addPhoneBtn);
        addPhoneBtn.setOnClickListener(v -> addPhoneField());
    }
    private void addPhoneField() {
        // Crear contenedor horizontal
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setPadding(0, 8, 0, 8);

        // Crear nuevo EditText
        EditText newPhone = new EditText(this);
        newPhone.setHint("Otro teléfono");
        newPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        newPhone.setLayoutParams(params);
        newPhone.setBackgroundResource(R.drawable.edittext_background);
        newPhone.setPadding(20, 20, 20, 20);

        // Botón para eliminar
        ImageButton removeBtn = new ImageButton(this);
        removeBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        removeBtn.setBackground(null);
        removeBtn.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        removeBtn.setOnClickListener(v -> phoneContainer.removeView(layout));

        layout.addView(newPhone);
        layout.addView(removeBtn);

        phoneContainer.addView(layout);

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
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String website = websiteInput.getText().toString().trim();

        String mision = misionInput.getText().toString().trim();

        String nit = nitInput.getText().toString().trim();
        String servicios = serviciosInput.getText().toString().trim();
        String productos = productosInput.getText().toString().trim();

        String mainPhone = mainPhoneInput.getText().toString().trim();

        ArrayList<String> phoneList = new ArrayList<String>();

        phoneList.add(mainPhone);

        for (int i = 0; i < phoneContainer.getChildCount(); i++) {
            View child = phoneContainer.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) child;
                for (int j = 0; j < row.getChildCount(); j++) {
                    View rowChild = row.getChildAt(j);
                    if (rowChild instanceof EditText) {
                        String phone = ((EditText) rowChild).getText().toString().trim();
                        if (!phone.isEmpty()) {
                            phoneList.add(phone);
                        }
                    }
                }
            }
        }



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

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Todos los campos obligatorios deben estar completos.", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = mAuth.getCurrentUser().getUid();

                if (selectedType.equals("Refugio")) {
                    Entity entity = new Entity(0,uid,1,encodedImageBase64,name,phoneList,address,email,mision,website);
                    db.collection("entities").document(uid).set(entity).addOnSuccessListener(aVoid -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterEntity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterEntity.this, "Error al guardar datos", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Entity entity = new Entity(0,uid,1,encodedImageBase64,nit,name,phoneList,address,email,serviciosList,productosList,website);
                    db.collection("entities").document(uid).set(entity).addOnSuccessListener(aVoid -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterEntity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterEntity.this, "Error al guardar datos", Toast.LENGTH_SHORT).show();
                    });
                }

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterEntity.this, "Error al registrar entidad", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
