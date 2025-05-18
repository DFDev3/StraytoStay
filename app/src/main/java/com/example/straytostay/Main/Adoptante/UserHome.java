package com.example.straytostay.Main.Adoptante;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.straytostay.Classes.Noticia;
import com.example.straytostay.Classes.Recurso;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.Main.Adapters.NoticiaAdapter;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserHome extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NoticiaAdapter adapter;

    private ArrayList<Noticia> noticiasList;
    private RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_home_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerHomeNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noticiasList = new ArrayList<>();
        // Inside onCreateView, replace your adapter initialization
        adapter = new NoticiaAdapter(noticiasList, noticia -> {
        });
        recyclerView.setAdapter(adapter);

        cargarNoticias();

        return view;
    }


    private void cargarNoticias() {

        db.collection("noticias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    noticiasList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Noticia noticia = doc.toObject(Noticia.class);
                        noticia.setNid(doc.getId()); // UID for passing to detail
                        noticiasList.add(noticia);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar noticias", Toast.LENGTH_SHORT).show();
                });
    }

}
