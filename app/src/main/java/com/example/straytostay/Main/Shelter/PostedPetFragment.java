package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PostedPetFragment extends Fragment {
    private FirebaseAuth mAuth;
    private MascotaAdapter adapter;

    private ArrayList<Mascota> mascotasList;
    private RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button Agregar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_postedpet_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerMascotasShelter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mascotasList = new ArrayList<>();
        // Inside onCreateView, replace your adapter initialization
        adapter = new MascotaAdapter(mascotasList, pet -> {
            Bundle bundle = new Bundle();
            bundle.putString("animalId", pet.getAid());  // Pass only the UID

            AnimalDetail fragment = new AnimalDetail();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        getEntidad();

        Agregar = view.findViewById(R.id.btnAgregarAnimal);
        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostAPet.class));

            }
        });

        return view;
    }

    private void getEntidad() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUid = auth.getCurrentUser().getUid();
        Log.d("userid",currentUid);

        db.collection("entities").document(currentUid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String entidadNombre = documentSnapshot.getString("name");
                        cargarMascotas(entidadNombre);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error buscando Entidad", Toast.LENGTH_SHORT).show();
                });

    }

    private void cargarMascotas(String entidadNombre) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mascotas")
                .whereEqualTo("refugio", entidadNombre)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mascotasList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Mascota mascota = doc.toObject(Mascota.class);
                        mascota.setAid(doc.getId()); // UID for passing to detail
                        mascotasList.add(mascota);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar mascotas", Toast.LENGTH_SHORT).show();
                });
    }

}
