package com.example.straytostay.Main.Adoptante;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.straytostay.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AnimalDetail extends Fragment {

    private ImageView imageAnimal;
    private TextView textNombre, textEdadRaza, textSexo, textTamano,
            textContenidoDescripcion, textNombreRefugio, textEsterilizado, textVacunas;
    private String animalId;
    private FirebaseFirestore db;
    private Button btnAplicarAdopcion; // Definir el botón
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

        db = FirebaseFirestore.getInstance();
        // Obtener ID del animal desde los argumentos
        if (getArguments() != null) {
            animalId = getArguments().getString("animalId");
            Log.e("||||||||||||||||","animalID: " + animalId);
        }

        // Referencias UI
        imageAnimal = view.findViewById(R.id.imageAnimal);
        textNombre = view.findViewById(R.id.textNombre);
        textEdadRaza = view.findViewById(R.id.textEdadRaza);
        textSexo = view.findViewById(R.id.textSexo);
        textTamano = view.findViewById(R.id.textTamano);
        textEsterilizado = view.findViewById(R.id.textEsterilizado);
        textContenidoDescripcion = view.findViewById(R.id.textContenidoDescripcion);
        textNombreRefugio = view.findViewById(R.id.textNombreRefugio);
        textVacunas = view.findViewById(R.id.textVacunas);
        btnAplicarAdopcion = view.findViewById(R.id.btnAplicarAdopcion);

        cargarDatosAnimal();

        btnAplicarAdopcion.setOnClickListener(v -> {
            // Redirigir al Activity adop_activity_form
            Intent intent = new Intent(getActivity(), Form.class);
            intent.putExtra("animalId", animalId); // Pasar el ID del animal si es necesario
            startActivity(intent);
        });
        return view;
    }

    private void cargarDatosAnimal() {
        DocumentReference docRef = db.collection("mascotas").document(animalId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nombre = document.getString("nombre");
                    String edad = document.getString("edad");
                    String raza = document.getString("raza");
                    String sexo = document.getString("sexo");
                    String tamano = document.getString("tamano");
                    String esterilizacion = document.getString("esterilizacion");
                    String descripcion = document.getString("descripcion");
                    String refugio = document.getString("refugio");
                    String imagenUrl = document.getString("imagenUrl");  // Base64 string for the image

                    List<String> vacunasList = (List<String>) document.get("vacunas");

                    // Set text fields
                    textNombre.setText(nombre);
                    textEdadRaza.setText(edad + " años · " + raza);
                    textSexo.setText("Sexo: " + sexo);
                    textTamano.setText("Tamaño: " + tamano);
                    textEsterilizado.setText(esterilizacion);
                    textContenidoDescripcion.setText(descripcion);
                    textNombreRefugio.setText(refugio);

                    // Display the image if available
                    if (imagenUrl != null && !imagenUrl.isEmpty()) {
                        loadImage(imagenUrl);
                    }
                    if (vacunasList != null && !vacunasList.isEmpty()) {
                        String vacunasTexto = String.join(" • ", vacunasList);
                        textVacunas.setText(vacunasTexto);

                    } else {
                        textVacunas.setText("No hay información sobre vacunas.");
                    }
                } else {
                    Log.e("Firestore", "No such document!");
                }
            } else {
                Log.e("Firestore", "Failed to get document: ", task.getException());
            }
        });
    }

    private void loadImage(String base64Image) {
        // Decode the Base64 string into a Bitmap
        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageAnimal.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            Log.e("AnimalDetail", "Error loading image", e);
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }
}
