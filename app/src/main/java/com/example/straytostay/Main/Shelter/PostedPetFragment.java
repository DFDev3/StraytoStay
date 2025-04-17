package com.example.straytostay.Main.Shelter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.straytostay.R;

public class PostedPetFragment extends Fragment {

    private Button Agregar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_postedpet_fragment, container, false);

        Agregar = view.findViewById(R.id.btnAgregarAnimal);


        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FormAgregarAnimalActivity.class));

            }
        });

        return view;
    }
}
