package com.example.straytostay.Main.Adoptante;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.straytostay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class AnimalDetail extends Fragment {

    private ImageView imageAnimal;
    private TextView textNombre, textEdadRaza, textSexo, textTamano,
    textContenidoDescripcion, textNombreRefugio, textVacunas;
    private DatabaseReference databaseReference;
    private String animalId;

    public static AnimalDetail newInstance(String animalId) {
        AnimalDetail fragment = new AnimalDetail();
        Bundle args = new Bundle();
        args.putString("animalId", animalId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_animal_detail, container, false);

        // Obtener ID del animal desde argumentos
        if (getArguments() != null) {
            animalId = getArguments().getString("animalId");
        }

        // Referencias UI
        imageAnimal = view.findViewById(R.id.imageAnimal);
        textNombre = view.findViewById(R.id.textNombre);
        textEdadRaza = view.findViewById(R.id.textEdadRaza);
        textSexo = view.findViewById(R.id.textSexo);
        textTamano = view.findViewById(R.id.textTamano);
        textContenidoDescripcion = view.findViewById(R.id.textContenidoDescripcion);
        textNombreRefugio = view.findViewById(R.id.textNombreRefugio);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("mascotas").child(animalId);

        cargarDatosAnimal();

        return view;
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
                    String imagenUrl = snapshot.child("imagenUrl").getValue(String.class);
                    String vacunas = snapshot.child("vacunas").getValue(String.class);

                    textNombre.setText(nombre);
                    textEdadRaza.setText(edad + " · " + raza);
                    textSexo.setText("Sexo: " + sexo);
                    textTamano.setText("Tamaño: " + tamano);
                    textContenidoDescripcion.setText(descripcion);
                    textNombreRefugio.setText(refugio);
                    textVacunas.setText(vacunas);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}