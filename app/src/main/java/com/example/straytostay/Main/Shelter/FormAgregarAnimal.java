package com.example.straytostay.Main.Shelter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.straytostay.R;

public class AgregarAnimalActivity extends AppCompatActivity {

    private EditText inputNombre, inputEdad, inputRaza, inputVacunas, inputTamano, inputDescripcion;
    private Spinner spinnerTipo, spinnerEsterilizacion, spinnerSexo;
    private Button btnAgregarImagen;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_formulario_agregar_animal); // Asegúrate del nombre

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("animales");

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
        btnAgregarImagen = findViewById(R.id.btnAgregarImagen);

        // Configurar spinners
        ArrayAdapter<CharSequence> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Perro", "Gato"});
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterEsterilizacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Esterilizado", "Sin esterilizar"});
        adapterEsterilizacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEsterilizacion.setAdapter(adapterEsterilizacion);

        ArrayAdapter<CharSequence> adapterSexo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Macho", "Hembra"});
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapterSexo);

        // Acción del botón
        btnAgregarImagen.setOnClickListener(v -> guardarAnimal());
    }

    private void guardarAnimal() {
        String nombre = inputNombre.getText().toString().trim();
        String edad = inputEdad.getText().toString().trim();
        String raza = inputRaza.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String esterilizado = spinnerEsterilizacion.getSelectedItem().toString();
        String sexo = spinnerSexo.getSelectedItem().toString();
        String vacunas = inputVacunas.getText().toString().trim();
        String tamano = inputTamano.getText().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty() || edad.isEmpty() || raza.isEmpty() || vacunas.isEmpty() || tamano.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto
        String id = databaseReference.push().getKey();
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

        // Guardar en Firebase
        databaseReference.child(id).setValue(animal).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Animal registrado correctamente", Toast.LENGTH_SHORT).show();
            finish(); // O limpia los campos si prefieres
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}

