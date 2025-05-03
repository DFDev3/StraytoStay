package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Entity;
import com.example.straytostay.Main.Adapters.EntityAdapter;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Entities extends Fragment {

    private RecyclerView recyclerView;
    private EntityAdapter adapter;
    private ArrayList<Entity> entitiesList;
    private FirebaseFirestore db;

    public Entities() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_entities_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerRefugios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entitiesList = new ArrayList<>();
        // Inside onCreateView, replace your adapter initialization
        adapter = new EntityAdapter(entitiesList, entity -> {
            Bundle bundle = new Bundle();
            bundle.putString("uid", entity.getUid());  // Pass only the UID

            EntityDetail fragment = new EntityDetail();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        cargarRefugios();

        return view;
    }

    private void cargarRefugios() {
        db.collection("entities")
                .whereEqualTo("verified",1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    entitiesList.clear(); // ✅ Clear list to avoid duplicates

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Entity refugio = doc.toObject(Entity.class);
                        refugio.setUid(doc.getId()); // ✅ Set the UID
                        entitiesList.add(refugio);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("RefugiosFragment", "Error al cargar refugios", e));
    }

}
