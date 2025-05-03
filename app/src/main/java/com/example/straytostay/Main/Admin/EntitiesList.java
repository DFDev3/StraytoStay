package com.example.straytostay.Main.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Entity;
import com.example.straytostay.Main.Adapters.EntityAdapter;
import com.example.straytostay.Main.Adoptante.EntityDetail;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EntitiesList extends Fragment {
    private Button btnToggle;
    private RecyclerView recyclerView;
    private EntityAdapter adapter;
    private ArrayList<Entity> entityList;
    private FirebaseFirestore db;
    private boolean atVerified;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_entities_list_fragment, container, false);

        btnToggle = view.findViewById(R.id.btn_toggle_entidades);
        recyclerView = view.findViewById(R.id.recyclerEntitiesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        entityList = new ArrayList<>();
        adapter = new EntityAdapter(entityList, entity -> {
            Bundle bundle = new Bundle();
            bundle.putString("uid", entity.getUid());  // Pass only the UID

            EntityDetail fragment = new EntityDetail();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_admin, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        cargarEntidades();

        btnToggle.setOnClickListener(v -> {
            atVerified = !atVerified;
            cargarEntidades();
            Log.d("AtVerified?", "bool - " + atVerified);
        });
        return view;
    }

    private void cargarEntidades() {
        int display;

        if (atVerified){
            display = 1;
            btnToggle.setText("VER ENTIDADES PENDIENTES");
        } else {
            display = 0;
            btnToggle.setText("VER ENTIDADES VERIFICADAS");
        }

        db.collection("entities")
                .whereEqualTo("verified", display)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    entityList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Entity entidad = doc.toObject(Entity.class);
                        entidad.setUid(doc.getId()); // Optionally save document ID
                        entityList.add(entidad);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("EntitiesList", "Error al cargar entidades", e));
    }

}





