package com.example.straytostay.Main.Adoptante;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Recurso;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.Main.Adapters.RecursoAdapter;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdoptionTips extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecursoAdapter librosAdapter, revistasAdapter, videosAdapter;

    private ArrayList<Recurso> librosList, revistasList, videosList;
    private RecyclerView recyclerRevistas, recyclerVideos, recyclerLibros;
    private LinearLayout toggleLibros, toggleRevistas, toggleVideos;
    private boolean atLibros, atRevistas, atVideos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_tips_fragment, container, false);
        librosList = new ArrayList<>();
        revistasList = new ArrayList<>();
        videosList = new ArrayList<>();

        toggleLibros = view.findViewById(R.id.header_libros);
        toggleRevistas = view.findViewById(R.id.header_revistas);
        toggleVideos = view.findViewById(R.id.header_videos);

        recyclerLibros = view.findViewById(R.id.recyclerLibros);
        recyclerRevistas = view.findViewById(R.id.recyclerRevistas);
        recyclerVideos = view.findViewById(R.id.recyclerVideos);

        librosAdapter = new RecursoAdapter(librosList, recurso -> openLink(recurso.getLink()));
        revistasAdapter = new RecursoAdapter(revistasList, recurso -> openLink(recurso.getLink()));
        videosAdapter = new RecursoAdapter(videosList, recurso -> openLink(recurso.getLink()));

        recyclerLibros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRevistas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerLibros.setAdapter(librosAdapter);
        recyclerRevistas.setAdapter(revistasAdapter);
        recyclerVideos.setAdapter(videosAdapter);


        recyclerLibros.setVisibility(View.GONE);
        recyclerRevistas.setVisibility(View.GONE);
        recyclerVideos.setVisibility(View.GONE);

        toggleLibros.setOnClickListener(v -> {
            cargarLibros();
            atLibros = !atLibros;
            if (atLibros){
                recyclerLibros.setVisibility(View.VISIBLE);
            } else {
                recyclerLibros.setVisibility(View.GONE);
            }
        });

        toggleRevistas.setOnClickListener(v -> {
            cargarRevistas();
            atRevistas = !atRevistas;
            if (atRevistas){
                recyclerRevistas.setVisibility(View.VISIBLE);
            } else {
                recyclerRevistas.setVisibility(View.GONE);
            }
        });

        toggleVideos.setOnClickListener(v -> {
            cargarVideos();
            atVideos = !atVideos;
            if (atVideos){
                recyclerVideos.setVisibility(View.VISIBLE);
            } else {
                recyclerVideos.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void openLink(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No app found to open this link.", Toast.LENGTH_SHORT).show();
        }
    }



    private void cargarLibros() {

        db.collection("recursos")
                .whereEqualTo("tipo","Libro")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    librosList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Recurso libro = doc.toObject(Recurso.class);
                        libro.setRid(doc.getId()); // UID for passing to detail
                        librosList.add(libro);
                    }
                    librosAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar libros", Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarRevistas() {

        db.collection("recursos")
                .whereEqualTo("tipo","Revista")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    revistasList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Recurso revista = doc.toObject(Recurso.class);
                        revista.setRid(doc.getId()); // UID for passing to detail
                        revistasList.add(revista);
                    }
                    revistasAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar revistas", Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarVideos() {

        db.collection("recursos")
                .whereEqualTo("tipo","Video")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    videosList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Recurso video = doc.toObject(Recurso.class);
                        video.setRid(doc.getId()); // UID for passing to detail
                        videosList.add(video);
                    }
                    videosAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar videos", Toast.LENGTH_SHORT).show();
                });
    }

}
