package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    private EditText inputNombre, inputEdad, inputRaza, inputVacunas, inputTamano, inputDescripcion;
    private Spinner spinnerTipo, spinnerEsterilizacion, spinnerSexo;
    private Button btnSeleccionarImagen, btnPublicar;
    private ImageView imagePreview;

    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_form_agregar_animal);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("mascotas");

        // Referencias UI
        inputNombre = findViewById(R.id.inputNombre);
        inputEdad = findViewById(R.id.inputEdad);
        inputRaza = findViewById(R.id.inputRaza);
        inputVacunas = findViewById(R.id.inputVacunas);
        inputTamano = findViewById(R.id.inputTamano);
        inputDescripcion = findViewById(R.id.inputDescripcion);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerEsterilizacion = findViewById(R.id.spinnerEsterilizacion);
        spinnerSexo = findViewById(R.id.spinnerSexo);

        btnSeleccionarImagen = findViewById(R.id.btnAgregarImagen);
        btnPublicar = findViewById(R.id.btnAgregarImagen);

        // Spinners
        spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Perro", "Gato"}));
        spinnerEsterilizacion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Esterilizado", "Sin esterilizar"}));
        spinnerSexo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Macho", "Hembra"}));

        // Eventos
        btnSeleccionarImagen.setOnClickListener(v -> openFileChooser());
        btnPublicar.setOnClickListener(v -> {
            if (imageUri != null) {
                subirImagenYGuardarDatos();
            } else {
                guardarAnimalEnFirestore(null);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
            imagePreview.setVisibility(View.VISIBLE);
        }
    }

    private void subirImagenYGuardarDatos() {
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> guardarAnimalEnFirestore(uri.toString())))
                .addOnFailureListener(e -> Toast.makeText(this, "Error al subir imagen: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void guardarAnimalEnFirestore(String imageUrl) {
        String nombre = inputNombre.getText().toString().trim();
        String edad = inputEdad.getText().toString().trim();
        String raza = inputRaza.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String esterilizado = spinnerEsterilizacion.getSelectedItem().toString();
        String sexo = spinnerSexo.getSelectedItem().toString();
        String vacunasTexto = inputVacunas.getText().toString().trim();
        String tamano = inputTamano.getText().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || edad.isEmpty() || raza.isEmpty() || vacunasTexto.isEmpty() || tamano.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> vacunas = Arrays.asList(vacunasTexto.split(","));

        String id = db.collection("mascotas").document().getId();
        Map<String, Object> animal = new HashMap<>();
        animal.put("id", id);
        animal.put("nombre", nombre);
        animal.put("edad", edad);
        animal.put("raza", raza);
        animal.put("tipo", tipo);
        animal.put("esterilizacion", esterilizado);
        animal.put("sexo", sexo);
        animal.put("vacunas", vacunas);
        animal.put("tamano", tamano);
        animal.put("descripcion", descripcion);
        if (imageUrl != null) {
            animal.put("imagenUrl", imageUrl);
        }

        db.collection("mascotas").document(id)
                .set(animal)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Animal registrado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
