package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindPets extends Fragment {

    private Spinner spinnerTipo, spinnerTamano, spinnerEdad;
    private RecyclerView recyclerView;
    private MascotaAdapter petAdapter;
    private List<Mascota> listaMascotas = new ArrayList<>();

    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_find_fragment, container, false);

        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        spinnerTamano = view.findViewById(R.id.spinnerTamanoSearch);
        spinnerEdad = view.findViewById(R.id.spinnerEdad);

        recyclerView = view.findViewById(R.id.recyclerMascotas);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        petAdapter = new MascotaAdapter(requireContext(),listaMascotas);
        recyclerView.setAdapter(petAdapter);

        configurarSpinners();

        db = FirebaseFirestore.getInstance();
        cargarMascotas();

        return view;
    }

    private void configurarSpinners() {
        // Tipo
        String[] tipos = {"Perro", "Gato"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tipos);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(tipoAdapter);

        // Tamaño
        String[] tamanos = {"Pequeño", "Mediano", "Grande"};
        ArrayAdapter<String> tamanoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tamanos);
        tamanoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTamano.setAdapter(tamanoAdapter);

        // Edad
        String[] edades = new String[15];
        for (int i = 0; i <= 14; i++) {
            edades[i] = i + " años";
        }
        ArrayAdapter<String> edadAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, edades);
        edadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdad.setAdapter(edadAdapter);
    }

    private void cargarMascotas() {
        db.collection("mascotas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaMascotas.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Mascota mascota = doc.toObject(Mascota.class);
                        listaMascotas.add(mascota);
                    }
                    petAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Puedes mostrar un Toast si quieres manejar errores
                });
    }
}
