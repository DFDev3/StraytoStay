package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.straytostay.R;


public class DetalleAnimalActivity extends AppCompatActivity {

    private ImageView imageAnimal;
    private TextView textNombre, textEdadRaza, textSexo, textTamano,
            textContenidoDescripcion, textNombreRefugio;
    private LinearLayout layoutVacunas;

    private DatabaseReference databaseReference;
    private String animalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_animal); // Asegúrate de usar el layout correcto

        // Recibir ID desde Intent
        animalId = getIntent().getStringExtra("animalId");

        // Inicializar vistas
        imageAnimal = findViewById(R.id.imageAnimal);
        textNombre = findViewById(R.id.textNombre);
        textEdadRaza = findViewById(R.id.textEdadRaza);
        textSexo = findViewById(R.id.textSexo);
        textTamano = findViewById(R.id.textTamano);
        textContenidoDescripcion = findViewById(R.id.textContenidoDescripcion);
        textNombreRefugio = findViewById(R.id.textNombreRefugio);
        layoutVacunas = findViewById(R.id.layoutVacunas);

        // Obtener referencia Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("animales").child(animalId);

        // Cargar datos
        cargarDatosAnimal();
    }

    private void cargarDatosAnimal() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String edad = snapshot.child("edad").getValue(String.class);
                    String raza = snapshot.child("raza").getValue(String.class);
                    String sexo = snapshot.child("sexo").getValue(String.class);
                    String tamano = snapshot.child("tamano").getValue(String.class);
                    String descripcion = snapshot.child("descripcion").getValue(String.class);
                    String refugio = snapshot.child("refugio").getValue(String.class);
                    String imagenUrl = snapshot.child("imagenUrl").getValue(String.class); // opcional

                    List<String> vacunas = new ArrayList<>();
                    for (DataSnapshot v : snapshot.child("vacunas").getChildren()) {
                        vacunas.add(v.getValue(String.class));
                    }

                    // Setear datos
                    textNombre.setText(nombre);
                    textEdadRaza.setText(edad + " · " + raza);
                    textSexo.setText("Sexo: " + sexo);
                    textTamano.setText("Tamaño: " + tamano);
                    textContenidoDescripcion.setText(descripcion);
                    textNombreRefugio.setText(refugio);

                    // Cargar vacunas como lista
                    layoutVacunas.removeAllViews();
                    for (String vacuna : vacunas) {
                        TextView vText = new TextView(DetalleAnimalActivity.this);
                        vText.setText("- " + vacuna);
                        vText.setTextColor(Color.DKGRAY);
                        vText.setTextSize(15);
                        layoutVacunas.addView(vText);
                    }

                    // Si usas imagen
                    if (imagenUrl != null && !imagenUrl.isEmpty()) {
                        Glide.with(DetalleAnimalActivity.this)
                                .load(imagenUrl)
                                .centerCrop()
                                .into(imageAnimal);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalleAnimalActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
