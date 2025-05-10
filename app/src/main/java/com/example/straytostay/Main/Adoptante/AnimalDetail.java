package com.example.straytostay.Main.Adoptante;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AnimalDetail extends Fragment {

    private ImageView imageAnimal;
    private TextView textNombre, textEdadRaza, textSexo, textTamano,
            textContenidoDescripcion, textNombreRefugio, textEsterilizado, textVacunas, verEntidad;
    private String animalId;
    private FirebaseFirestore db;
    private Button btnAplicarAdopcion; // Definir el botón
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static AnimalDetail newInstance(String animalId) {
        AnimalDetail fragment = new AnimalDetail();
        Bundle args = new Bundle();
        args.putString("animalId", animalId);
        fragment.setArguments(args);
        return fragment;
    }

    private String entityUid;

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
        verEntidad = view.findViewById(R.id.verEntidad);

        cargarDatosAnimal();

        db.collection("mascotas").document(animalId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) return;

                    List<String> appliedByList = (List<String>) documentSnapshot.get("appliedBy");
                    boolean hasApplied = appliedByList != null && appliedByList.contains(uid);

                    if (hasApplied) {
                        // User already applied
                        btnAplicarAdopcion.setText("APLICACIÓN ENVIADA");
                        btnAplicarAdopcion.setEnabled(false);
                        btnAplicarAdopcion.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_border_blue));
                        btnAplicarAdopcion.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue));
                        btnAplicarAdopcion.setOnClickListener(v ->
                                Toast.makeText(requireContext(), "Ya aplicaste a esta mascota.", Toast.LENGTH_SHORT).show()
                        );
                    } else {
                        // Check if user filled the form (has 'scores')
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(snapshot -> {
                                    if (!snapshot.exists()) return;

                                    if (snapshot.get("scores") == null) {
                                        // Show dialog to go fill the form
                                        btnAplicarAdopcion.setOnClickListener(v -> {
                                            new AlertDialog.Builder(requireContext())
                                                    .setTitle("Formulario requerido")
                                                    .setMessage("Debes completar el formulario antes de aplicar. ¿Deseas hacerlo ahora?")
                                                    .setPositiveButton("Ir", (dialog, which) -> {
                                                        ProfileFragment profileFragment = new ProfileFragment();
                                                        requireActivity().getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.fragment_container, profileFragment)
                                                                .addToBackStack(null)
                                                                .commit();
                                                    })
                                                    .setNegativeButton("Cancelar", null)
                                                    .show();
                                        });
                                    } else {
                                        // User is eligible to apply
                                        btnAplicarAdopcion.setEnabled(true);
                                        btnAplicarAdopcion.setOnClickListener(v -> {
                                            db.collection("mascotas").document(animalId)
                                                    .update("appliedBy", FieldValue.arrayUnion(uid))
                                                    .addOnSuccessListener(unused -> {
                                                        Toast.makeText(requireContext(), "Has aplicado exitosamente.", Toast.LENGTH_SHORT).show();
                                                        btnAplicarAdopcion.setText("APLICACIÓN ENVIADA");
                                                        btnAplicarAdopcion.setEnabled(false);
                                                        btnAplicarAdopcion.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_border_blue));
                                                        btnAplicarAdopcion.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue));
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("Firestore", "Error al aplicar", e);
                                                        Toast.makeText(requireContext(), "Ocurrió un error al aplicar.", Toast.LENGTH_SHORT).show();
                                                    });
                                        });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error obteniendo usuario", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error obteniendo mascota", e);
                });


        verEntidad.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("uid", entityUid);  // Now uses the field

            EntityDetail fragment = new EntityDetail();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
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
                    String imagenUrl = document.getString("imageUrl");  // Base64 string for the image

                    db.collection("entities")
                            .whereEqualTo("name", refugio)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot theDocument = queryDocumentSnapshots.getDocuments().get(0);
                                    entityUid = theDocument.getId(); // UID of the entity
                                    Log.d("EntityUID", "UID: " + entityUid);
                                    // Use entityUid as needed
                                } else {
                                    Log.d("EntityUID", "No entity found with that name.");
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("EntityUID", "Error fetching entity UID", e);
                            });
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
