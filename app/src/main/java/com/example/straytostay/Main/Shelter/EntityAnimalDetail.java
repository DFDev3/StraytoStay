package com.example.straytostay.Main.Shelter;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.Main.Adapters.UserAdapter;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityAnimalDetail extends Fragment {

    private RecyclerView recyclerView;
    private ImageView imageAnimal;
    private Button btnToggleEstudio;
    private TextView textNombre, textEdadRaza, textSexo, textTamano,
    textContenidoDescripcion, textNombreRefugio, textEsterilizado, textVacunas;
    private UserAdapter adapter;
    private FirebaseFirestore db;
    private String animalId, shelterUid;
    private List<Usuario> appliersList = new ArrayList<>();
    private boolean isOpen;
    public static EntityAnimalDetail newInstance(String animalId, String ShelterUid) {
        EntityAnimalDetail fragment = new EntityAnimalDetail();
        Bundle args = new Bundle();
        args.putString("animalId", animalId);
        args.putString("ShelterUid", ShelterUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_animal_detail, container, false);

        db = FirebaseFirestore.getInstance();
        // Obtener ID del animal desde los argumentos
        if (getArguments() != null) {
            animalId = getArguments().getString("animalId");
            shelterUid = getArguments().getString("ShelterUid");
            Log.e("||||||||||||||||","animalID: " + animalId);
        }

        // Referencias UI
        imageAnimal = view.findViewById(R.id.imageAnimal);
        textNombre = view.findViewById(R.id.textNombre);
        textEdadRaza = view.findViewById(R.id.textEdadRaza);
        textSexo = view.findViewById(R.id.textSexo);
        textTamano = view.findViewById(R.id.textTamano);
        textContenidoDescripcion = view.findViewById(R.id.textContenidoDescripcion);
        textVacunas = view.findViewById(R.id.textVacunas);
        btnToggleEstudio = view.findViewById(R.id.btnCerrarEstudio);

        recyclerView = view.findViewById(R.id.recyclerAppliers);

        adapter = new UserAdapter(appliersList, user -> {
            Bundle bundle = new Bundle();
            bundle.putString("uid", user.getUid());  // Pass only the UID
            bundle.putString("aid", animalId);

            UserDetail fragment = new UserDetail();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        cargarDatosAnimal();

        // Call this when initializing the view
        fetchEstado(animalId, isOpenResult -> {
            isOpen = isOpenResult;
            showApplication();

            btnToggleEstudio.setOnClickListener(v -> {
                isOpen = !isOpen;
                showApplication();

                // Update Firestore ONLY here
                FirebaseFirestore.getInstance()
                        .collection("mascotas")
                        .document(animalId)
                        .update("estado", isOpen ? "Abierta" : "Cerrada")
                        .addOnSuccessListener(unused -> Log.d("ToggleEstado", "Estado updated"))
                        .addOnFailureListener(e -> Log.e("ToggleEstado", "Error updating estado", e));
            });

        });

        showApplication();

        return view;
    }

    public interface EstadoCallback {
        void onEstadoLoaded(boolean isOpen);
    }

    private void fetchEstado(String animalId, EstadoCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mascotas").document(animalId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        callback.onEstadoLoaded(false); // default to false
                        return;
                    }

                    String estado = documentSnapshot.getString("estado");
                    boolean isOpen = estado != null && estado.equals("Abierta");
                    callback.onEstadoLoaded(isOpen);
                })
                .addOnFailureListener(e -> {
                    Log.e("fetchEstado", "Error fetching estado", e);
                    callback.onEstadoLoaded(false);
                });
    }

    private void showApplication() {
        if (isOpen) {
            btnToggleEstudio.setText("CERRAR POSTULACIONES");
        } else {
            btnToggleEstudio.setText("ABRIR POSTULACIONES");
        }
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
                    String descripcion = document.getString("descripcion");
                    String imagenUrl = document.getString("imageUrl");  // Base64 string for the image

                    List<String> vacunasList = (List<String>) document.get("vacunas");

                    // Set text fields
                    textNombre.setText(nombre);
                    textEdadRaza.setText(edad + " años · " + raza);
                    textSexo.setText("Sexo: " + sexo);
                    textTamano.setText("Tamaño: " + tamano);
                    textContenidoDescripcion.setText(descripcion);

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

                    loadPostulantes(document);

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
            Log.e("EntityAnimalDetail", "Error loading image", e);
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPostulantes(DocumentSnapshot pet){

        db.collection("mascotas").document(animalId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> appliedBy = (List<String>) documentSnapshot.get("appliedBy");

                        if (appliedBy != null && !appliedBy.isEmpty()) {
                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                            for (String uid : appliedBy) {
                                tasks.add(db.collection("users").document(uid).get());
                            }

                            Tasks.whenAllSuccess(tasks)
                                    .addOnSuccessListener(results -> {
                                        appliersList.clear();

                                        for (Object result : results) {
                                            if (result instanceof DocumentSnapshot) {
                                                DocumentSnapshot userDoc = (DocumentSnapshot) result;

                                                // Convert to Usuario object
                                                Usuario user = new Usuario();
                                                user.setUid(userDoc.getId());
                                                user.setName(userDoc.getString("name"));
                                                user.setPhone(userDoc.getString("phone"));
                                                user.setAddress(userDoc.getString("address"));
                                                user.setImageUrl(userDoc.getString("imageUrl"));

                                                appliersList.add(user);
                                            }
                                        }

                                        adapter.notifyDataSetChanged();
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching pet or users", e));
    }
}
