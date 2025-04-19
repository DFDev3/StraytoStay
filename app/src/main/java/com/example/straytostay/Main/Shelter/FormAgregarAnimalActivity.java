package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormAgregarAnimalActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText inputNombre, inputEdad, inputRaza, inputVacunas, inputDescripcion;
    private Spinner spinnerTipo, spinnerEsterilizacion, spinnerSexo, spinnerTamano;
    private Button btnSeleccionarImagen, btnPublicar;

    private Uri imageUri;
    private FirebaseFirestore db;
    private StorageReference storageRef;

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

            if (imageUri != null) {
                subirImagenYGuardarDatos();
            } else {
                guardarAnimalEnFirestore(null);
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
        }
    }

    private void subirImagenYGuardarDatos() {
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> guardarAnimalEnFirestore(uri.toString()))
                        .addOnFailureListener(e -> showToast("Failed to get image URL: " + e.getMessage())))
                .addOnFailureListener(e -> showToast("Error uploading image: " + e.getMessage()));
    }

    private void guardarAnimalEnFirestore(String imageUrl) {
        String nombre = inputNombre.getText().toString().trim();
        String edad = inputEdad.getText().toString().trim();
        String raza = inputRaza.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String esterilizado = spinnerEsterilizacion.getSelectedItem().toString();
        String sexo = spinnerSexo.getSelectedItem().toString();
        String vacunas = inputVacunas.getText().toString().trim();
        String tamano = spinnerTamano.getSelectedItem().toString();
        String descripcion = inputDescripcion.getText().toString().trim();


        String id = db.collection("mascotas").document().getId();

        Mascota mascota = new Mascota();
        mascota.setId(id);
        mascota.setNombre(nombre);
        mascota.setEdad(edad);
        mascota.setRaza(raza);
        mascota.setTipo(tipo);
        mascota.setEsterilizacion(esterilizado);
        mascota.setSexo(sexo);
        mascota.setVacunas(mascota.getVacunas());
        mascota.setTamano(tamano);
        mascota.setDescripcion(descripcion);
        mascota.setImagenUrl(imageUrl);

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
