package com.example.straytostay.StartUp;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Main.Adoptante.UserHome;
import com.example.straytostay.R;
import com.example.straytostay.Classes.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterUser extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;

    private EditText nameInput, lastNameInput, phoneInput, citizenIdInput, addressInput, emailInput, passwordInput, confirmPasswordInput;

    private TextView loginLink;
    private Button registerButton, selectImageButton;
    private ImageView selectedImage;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String encodedImageBase64;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.firstNameInput);
        phoneInput = findViewById(R.id.phoneInput);
        citizenIdInput = findViewById(R.id.idInput);
        addressInput = findViewById(R.id.addressInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView eyeIcon = findViewById(R.id.eyeIcon);

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
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView eyeIconConfirm = findViewById(R.id.eyeIconConfirm);

        eyeIconConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmPasswordInput.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    // Mostrar contraseña
                    confirmPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIconConfirm.setImageResource(R.drawable.ic_eye_open);
                } else {
                    // Ocultar contraseña
                    confirmPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIconConfirm.setImageResource(R.drawable.ic_eye_closed);
                }
                // Mantiene el cursor al final del texto
                confirmPasswordInput.setSelection(confirmPasswordInput.getText().length());
            }
        });

        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        selectedImage = findViewById(R.id.adop_selectedImage);
        selectImageButton = findViewById(R.id.adop_selectImageButton);

        registerButton.setOnClickListener(v -> registerUser());

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUser.this, Login.class));
            }
        });
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

    private void registerUser() {
        // Get input values
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String citizenId = citizenIdInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();


        // Register with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // Create Usuario object with default adminID = 0
                        Usuario user = new Usuario(name, phone, citizenId, address, email,0,uid,encodedImageBase64,0);

                        db.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterUser.this, UserHome.class));
                                    finish();
                                });
                    }
                });
    }
}
