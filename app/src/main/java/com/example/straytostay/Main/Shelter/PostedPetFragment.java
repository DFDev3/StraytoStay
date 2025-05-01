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
import com.example.straytostay.Classes.Shelter;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.Main.Adapters.ShelterAdapter;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.Main.Adoptante.ShelterDetailFragment;
import com.example.straytostay.Main.Shelter.FormAgregarAnimalActivity;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_postedpet_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerMascotasShelter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mascotasList = new ArrayList<>();
        // Inside onCreateView, replace your adapter initialization
        adapter = new MascotaAdapter(requireContext(), mascotasList);
        recyclerView.setAdapter(adapter);

        getRefugio();

        Agregar = view.findViewById(R.id.btnAgregarAnimal);
        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FormAgregarAnimalActivity.class));

            }
        });

        return view;
    }

    private void getRefugio() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUid = auth.getCurrentUser().getUid();
        Log.d("userid",currentUid);

        db.collection("shelters").document(currentUid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String refugioNombre = documentSnapshot.getString("name");
                        cargarMascotas(refugioNombre);
                    } else {
                        // Not in Shelters? Try Vets
                        db.collection("vets").document(currentUid).get()
                                .addOnSuccessListener(vetSnapshot -> {
                                    if (vetSnapshot.exists()) {
                                        String refugioNombre = vetSnapshot.getString("name");
                                        cargarMascotas(refugioNombre);
                                    } else {
                                        Toast.makeText(getContext(), "No se encontró el usuario en Shelters ni Vets", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error buscando en Vets", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error buscando en Shelters", Toast.LENGTH_SHORT).show();
                });

    }

    private void cargarMascotas(String refugioNombre) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mascotas")
                .whereEqualTo("refugio", refugioNombre)
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
