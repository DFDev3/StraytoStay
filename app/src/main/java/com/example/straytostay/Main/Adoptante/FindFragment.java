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

import com.example.straytostay.R;

public class FindFragment extends Fragment {

    private Spinner spinnerTipo, spinnerTamano, spinnerEdad;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflar el layout principal del fragmento
        View view = inflater.inflate(R.layout.adop_find_fragment, container, false);

        // Buscar los spinners desde el layout incluido
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        spinnerTamano = view.findViewById(R.id.spinnerTamano);
        spinnerEdad = view.findViewById(R.id.spinnerEdad);

        configurarSpinners();

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
}
