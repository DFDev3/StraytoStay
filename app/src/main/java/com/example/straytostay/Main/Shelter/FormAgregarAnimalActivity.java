package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class FormAgregarAnimalActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText inputNombre, inputEdad, inputRaza, inputDescripcion;
    private Spinner spinnerTipo, spinnerEsterilizacion, spinnerSexo, spinnerTamano;
    private Button btnSeleccionarImagen, btnPublicar;
    private ImageView imagePreview;
    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String encodedImageBase64 = null;

    private CheckBox vacunaRabia, vacunaMoquillo, vacunaParvovirus, vacunaHepatitis,
            vacunaLeptospirosis, vacunaBordetella, vacunaParainfluenza, vacunaPanleucopenia, vacunaRinotraqueitis, vacunaCalicivirus, vacunaFelv, vacunaFIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_form_agregar_animal);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        inputNombre = findViewById(R.id.inputNombre);
        inputEdad = findViewById(R.id.inputEdad);
        inputRaza = findViewById(R.id.inputRaza);
        inputDescripcion = findViewById(R.id.inputDescripcion);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerEsterilizacion = findViewById(R.id.spinnerEsterilizacion);
        spinnerSexo = findViewById(R.id.spinnerSexo);
        spinnerTamano = findViewById(R.id.spinnerTamanoForm);

        imagePreview = findViewById(R.id.imagePreview);
        btnSeleccionarImagen = findViewById(R.id.btnAgregarImagen);
        btnPublicar = findViewById(R.id.btnConfirmPublish);

        String[] tiposAnimales = {"Selecciona el tipo", "Perro", "Gato"};
        String[] esterilizacionOptions = {"Selecciona la esterilización", "Esterilizado", "Sin esterilizar"};
        String[] sexoOptions = {"Selecciona el sexo", "Macho", "Hembra"};
        String[] tamanoOptions = {"Selecciona el tamaño", "Pequeño", "Mediano", "Grande"};

        spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tiposAnimales));
        spinnerTipo.setSelection(0);

        spinnerEsterilizacion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, esterilizacionOptions));
        spinnerEsterilizacion.setSelection(0);

        spinnerSexo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sexoOptions));
        spinnerSexo.setSelection(0);

        spinnerTamano.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tamanoOptions));
        spinnerTamano.setSelection(0);

        vacunaRabia = findViewById(R.id.vacuna_rabia);
        vacunaMoquillo = findViewById(R.id.vacuna_moquillo);
        vacunaParvovirus = findViewById(R.id.vacuna_parvovirus);
        vacunaHepatitis = findViewById(R.id.vacuna_hepatitis);
        vacunaLeptospirosis = findViewById(R.id.vacuna_leptospirosis);
        vacunaBordetella = findViewById(R.id.vacuna_bordetella);
        vacunaParainfluenza = findViewById(R.id.vacuna_parainfluenza);

        vacunaPanleucopenia = findViewById(R.id.vacuna_panleucopenia);
        vacunaRinotraqueitis = findViewById(R.id.vacuna_rinotraqueitis);
        vacunaCalicivirus = findViewById(R.id.vacuna_calicivirus);
        vacunaFelv = findViewById(R.id.vacuna_leucemia);
        vacunaFIV = findViewById(R.id.vacuna_fiv);

        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());

        btnPublicar.setOnClickListener(v -> guardarAnimal());
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);

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

    private void guardarAnimal() {
        String nombre = inputNombre.getText().toString();
        String edad = inputEdad.getText().toString();
        String raza = inputRaza.getText().toString();
        String descripcion = inputDescripcion.getText().toString();

        String tipo = spinnerTipo.getSelectedItem().toString();
        String esterilizado = spinnerEsterilizacion.getSelectedItem().toString();
        String sexo = spinnerSexo.getSelectedItem().toString();
        String tamano = spinnerTamano.getSelectedItem().toString();

        ArrayList<String> vacunas = new ArrayList<>();
        if (vacunaRabia.isChecked()) vacunas.add("Rabia");
        if (vacunaMoquillo.isChecked()) vacunas.add("Moquillo");
        if (vacunaParvovirus.isChecked()) vacunas.add("Parvovirus");
        if (vacunaHepatitis.isChecked()) vacunas.add("Hepatitis");
        if (vacunaLeptospirosis.isChecked()) vacunas.add("Leptospirosis");
        if (vacunaBordetella.isChecked()) vacunas.add("Bordetella");
        if (vacunaParainfluenza.isChecked()) vacunas.add("Parainfluenza");
        if (vacunaPanleucopenia.isChecked()) vacunas.add("Panleucopenia");
        if (vacunaRinotraqueitis.isChecked()) vacunas.add("Rinotraqueitis");
        if (vacunaCalicivirus.isChecked()) vacunas.add("Calicivirus");
        if (vacunaFelv.isChecked()) vacunas.add("Felv");
        if (vacunaFIV.isChecked()) vacunas.add("FIV");

        String ShelterUid = mAuth.getCurrentUser().getUid();

        final String[] refugio = {null}; // Use array to allow mutation inside inner class

        db.collection("shelters").document(ShelterUid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        refugio[0] = snapshot.getString("name");
                        // You can now use refugio[0] here or call guardarAnimal(refugio[0]);
                    } else {
                        // If not found, check "vets"
                        db.collection("vets").document(ShelterUid).get()
                                .addOnSuccessListener(vetSnapshot -> {
                                    if (vetSnapshot.exists()) {
                                        refugio[0] = vetSnapshot.getString("name");
                                        // You can now use refugio[0] here too
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("ShelterDetail", "Error fetching from vets", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ShelterDetail", "Error fetching from shelters", e);
                });


        Mascota pet = new Mascota();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("mascotas").add(pet)
                    .addOnSuccessListener(documentReference -> {
                        String uid = documentReference.getId();

                        Mascota mascota = new Mascota(uid, nombre, edad, raza, tipo, esterilizado, sexo, vacunas, tamano, descripcion,refugio[0], encodedImageBase64);

                        // Step 4: Save the full Mascota object with UID (optional step, you can update other fields if needed)
                        db.collection("mascotas")
                                .document(uid)
                                .set(mascota)  // This step will save the Mascota object with the correct uid.
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Animal guardado correctamente", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar mascota", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
        }
    }
}
