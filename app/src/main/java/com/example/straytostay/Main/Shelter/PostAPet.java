package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PostAPet extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText inputNombre, inputEdad, inputRaza, inputDescripcion, inputVacuna;
    private Spinner spinnerTipo, spinnerEsterilizacion, spinnerSexo, spinnerTamano;
    private Button btnSeleccionarImagen, btnPublicar;
    private ImageButton addVacunaBtn;
    private ImageView imagePreview;
    private LinearLayout vacunaContainer;
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

        inputVacuna = findViewById(R.id.animal_vacuna);
        vacunaContainer = findViewById(R.id.vacunaContainer); // Agrega esto si usas varios teléfonos dinámicamente
        addVacunaBtn = findViewById(R.id.addVacunaBtn);
        addVacunaBtn.setOnClickListener(v -> addVacunaField());

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

    private void addVacunaField() {
        // Crear contenedor horizontal
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setPadding(0, 8, 0, 8);

        // Crear nuevo EditText
        EditText newPhone = new EditText(this);
        newPhone.setHint("Otra vacna");
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
        removeBtn.setOnClickListener(v -> vacunaContainer.removeView(layout));

        layout.addView(newPhone);
        layout.addView(removeBtn);

        vacunaContainer.addView(layout);

    }

    private void guardarAnimal() {
        String nombre = inputNombre.getText().toString();
        String edad = inputEdad.getText().toString();
        String raza = inputRaza.getText().toString();
        String descripcion = inputDescripcion.getText().toString();
        String mainVacuna = inputVacuna.getText().toString();

        String tipo = spinnerTipo.getSelectedItem().toString();
        String esterilizado = spinnerEsterilizacion.getSelectedItem().toString();
        String sexo = spinnerSexo.getSelectedItem().toString();
        String tamano = spinnerTamano.getSelectedItem().toString();

        ArrayList<String> vacunaList = new ArrayList<String>();

        vacunaList.add(mainVacuna);

        for (int i = 0; i < vacunaContainer.getChildCount(); i++) {
            View child = vacunaContainer.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) child;
                for (int j = 0; j < row.getChildCount(); j++) {
                    View rowChild = row.getChildAt(j);
                    if (rowChild instanceof EditText) {
                        String vacuna = ((EditText) rowChild).getText().toString().trim();
                        if (!vacuna.isEmpty()) {
                            vacunaList.add(vacuna);
                        }
                    }
                }
            }
        }

        String ShelterUid = mAuth.getCurrentUser().getUid();

        final String[] refugio = {null}; // Use array to allow mutation inside inner class

        db.collection("entities").document(ShelterUid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        refugio[0] = snapshot.getString("name");
                        // You can now use refugio[0] here or call guardarAnimal(refugio[0]);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ShelterDetail", "Error fetching from entities", e);
                });


        Mascota pet = new Mascota();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("mascotas").add(pet)
                    .addOnSuccessListener(documentReference -> {
                        String aid = documentReference.getId();

                        Mascota mascota = new Mascota(aid, nombre, edad, raza, tipo, esterilizado, sexo, vacunaList, tamano, descripcion, refugio[0], encodedImageBase64);

                        db.collection("mascotas")
                                .document(aid)
                                .set(mascota)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Animal guardado correctamente", Toast.LENGTH_SHORT).show();
                                    finish(); // 👈 Close the activity after success
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error al guardar mascota", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
        }
    }
}