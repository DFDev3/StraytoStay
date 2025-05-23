package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Main.Adapters.EntityAdapter;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindPets extends Fragment {

    private Spinner spinnerTipo, spinnerTamano, spinnerEdad;
    private RecyclerView recyclerView;
    private MascotaAdapter adapter;
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

        adapter = new MascotaAdapter(listaMascotas, pet -> {
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

        configurarSpinners();

        db = FirebaseFirestore.getInstance();
        cargarMascotas();

        return view;
    }

    private void configurarSpinners() {
        // Tipo
        String[] tipos = {"", "Perro", "Gato"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tipos);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(tipoAdapter);

        // Tamaño
        String[] tamanos = {"", "Pequeño", "Mediano", "Grande"};
        ArrayAdapter<String> tamanoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tamanos);
        tamanoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTamano.setAdapter(tamanoAdapter);

        // Edad
        String[] edades = new String[11];
        edades[0] = "";
        for (int i = 1; i <= 10; i++) {
            edades[i] = i + " años";
        }
        ArrayAdapter<String> edadAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, edades);
        edadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdad.setAdapter(edadAdapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarMascotasFiltradas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerTipo.setOnItemSelectedListener(listener);
        spinnerTamano.setOnItemSelectedListener(listener);
        spinnerEdad.setOnItemSelectedListener(listener);

    }

    private void cargarMascotasFiltradas() {
        String tipo = spinnerTipo.getSelectedItem().toString();
        String tamano = spinnerTamano.getSelectedItem().toString();
        String edad = spinnerEdad.getSelectedItem().toString(); // e.g. "4 años"

        db.collection("mascotas")
                .whereEqualTo("estado", "Abierta")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaMascotas.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Mascota mascota = doc.toObject(Mascota.class);

                        // Tipo
                        if (!tipo.isEmpty() && !tipo.equals(mascota.getTipo())) continue;

                        // Tamaño
                        if (!tamano.isEmpty() && !tamano.equals(mascota.getTamano())) continue;

                        // Edad
                        if (!edad.isEmpty()) {
                            String edadMascotaStr = mascota.getEdad();
                            if (edadMascotaStr == null) continue;

                            try {
                                int edadSeleccionada = Integer.parseInt(edad.split(" ")[0]);
                                int edadMascota = Integer.parseInt(edadMascotaStr.trim());
                                if (edadMascota != edadSeleccionada) continue;
                            } catch (NumberFormatException e) {
                                continue;
                            }
                        }

                        listaMascotas.add(mascota);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Optional error handling
                });
    }




    private void cargarMascotas() {
        db.collection("mascotas")
                .whereEqualTo("estado","Abierta")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaMascotas.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Mascota mascota = doc.toObject(Mascota.class);
                        listaMascotas.add(mascota);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Puedes mostrar un Toast si quieres manejar errores
                });
    }
}
