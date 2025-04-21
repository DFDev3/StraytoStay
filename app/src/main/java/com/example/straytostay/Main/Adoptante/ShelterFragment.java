package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.Main.Adapters.ShelterAdapter;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShelterFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShelterAdapter adapter;
    private ArrayList<Usuario> refugiosList;
    private FirebaseFirestore db;

    public ShelterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_entities_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerRefugios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refugiosList = new ArrayList<>();
        adapter = new ShelterAdapter(refugiosList);
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        cargarRefugios();

        return view;
    }

    private void cargarRefugios() {
        db.collection("users")
                .whereEqualTo("adminId", 1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Usuario refugio = doc.toObject(Usuario.class);
                        refugiosList.add(refugio);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("RefugiosFragment", "Error al cargar refugios", e));
    }
}
