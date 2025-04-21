package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormAgregarAnimalActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText inputNombre, inputEdad, inputRaza, inputVacunas, inputDescripcion;
    private Spinner spinnerTipo, spinnerEsterilizacion, spinnerSexo, spinnerTamano;
    private Button btnSeleccionarImagen, btnPublicar;
    private ImageView imagePreview;
    private Uri imageUri;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private String encodedImageBase64 = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_form_agregar_animal);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // UI references
        inputNombre = findViewById(R.id.inputNombre);
        inputEdad = findViewById(R.id.inputEdad);
        inputRaza = findViewById(R.id.inputRaza);
        inputVacunas = findViewById(R.id.inputVacunas);
        inputDescripcion = findViewById(R.id.inputDescripcion);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerEsterilizacion = findViewById(R.id.spinnerEsterilizacion);
        spinnerSexo = findViewById(R.id.spinnerSexo);
        spinnerTamano = findViewById(R.id.spinnerTamanoForm);

        imagePreview = findViewById(R.id.imagePreview);
        btnSeleccionarImagen = findViewById(R.id.btnAgregarImagen);
        btnPublicar = findViewById(R.id.btnConfirmPublish);

        // Spinner adapters
        spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Perro", "Gato"}));
        spinnerEsterilizacion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Esterilizado", "Sin esterilizar"}));
        spinnerSexo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Macho", "Hembra"}));
        spinnerTamano.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Pequeño", "Mediano", "Grande"}));

        // Listeners
        btnSeleccionarImagen.setOnClickListener(v -> openFileChooser());

        btnPublicar.setOnClickListener(v -> {

            if (!validateFields()) return;

            if (encodedImageBase64 != null) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                    return;
                }

                String shelterUID = currentUser.getUid();

                // Fetch shelter's name from Firestore
                db.collection("users").document(shelterUID).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombreRefugio = documentSnapshot.getString("name"); // or "nombreRefugio", based on your structure
                        guardarAnimalEnFirestore(encodedImageBase64, nombreRefugio);
                    }
                });

            } else {
                Toast.makeText(FormAgregarAnimalActivity.this, "Por favor selecciona una imagen primero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(imageUri);


            // Convert the image to base64
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 400, 400, true);  // Resize
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);  // Compress
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarAnimalEnFirestore(String encodedImage, String refugio) {
        String nombre = inputNombre.getText().toString().trim();
        String edad = inputEdad.getText().toString().trim();
        String raza = inputRaza.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String esterilizado = spinnerEsterilizacion.getSelectedItem().toString();
        String sexo = spinnerSexo.getSelectedItem().toString();
        String tamano = spinnerTamano.getSelectedItem().toString();
        String descripcion = inputDescripcion.getText().toString().trim();

        // Assuming you're getting the vacunas string from an EditText (e.g., etVacunas)
        String vacunas = inputVacunas.getText().toString().trim();

// Split the input by commas and remove extra spaces
        String[] vacunasArray = vacunas.split(",");
        ArrayList<String> vacunasList = new ArrayList<>();

// Trim spaces and add each vaccine to the list
        for (String vacuna : vacunasArray) {
            vacunasList.add(vacuna.trim());
        }

        String id = db.collection("mascotas").document().getId();

        Mascota mascota = new Mascota();
        mascota.setId(id);
        mascota.setNombre(nombre);
        mascota.setEdad(edad);
        mascota.setRaza(raza);
        mascota.setTipo(tipo);
        mascota.setEsterilizacion(esterilizado);
        mascota.setSexo(sexo);
        mascota.setVacunas(vacunasList);
        mascota.setTamano(tamano);
        mascota.setDescripcion(descripcion);
        mascota.setImagenUrl(encodedImage);
        mascota.setRefugio(refugio);



        db.collection("mascotas").document(id)
                .set(mascota)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Animal registrado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> showToast("Error al registrar: " + e.getMessage()));
    }

    private boolean validateFields() {
        if (inputNombre.getText().toString().trim().isEmpty()
                || inputEdad.getText().toString().trim().isEmpty()
                || inputRaza.getText().toString().trim().isEmpty()
                || inputVacunas.getText().toString().trim().isEmpty()
                || spinnerTamano.getSelectedItem() == null
                || inputDescripcion.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
