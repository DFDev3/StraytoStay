package com.example.straytostay.Main.Admin;

import android.content.Intent;
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

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.Main.Adapters.AdminAdapter;
import com.example.straytostay.Main.Adapters.EntityAdapter;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.Main.Adoptante.EntityDetail;
import com.example.straytostay.Main.Shelter.PostAPet;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;



public class AdminList extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Usuario> adminList;
    private Button Agregar;

    private AdminAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_admin_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerAdminList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adminList = new ArrayList<>();
        // Inside onCreateView, replace your adapter initialization
        adapter = new AdminAdapter(adminList, admin -> {
            Bundle bundle = new Bundle();
            bundle.putString("uid", admin.getUid());  // Pass only the UID

            AdminProfile fragment = new AdminProfile();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);


        Agregar = view.findViewById(R.id.btnAgregarAnimal);
        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostAPet.class));

            }
        });
        return view;
    }

    private void cargarAdmins(String adminNombre) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("adminId", 2)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    adminList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Usuario admin = doc.toObject(Usuario.class);
                        admin.setUid(doc.getId()); // UID for passing to detail
                        adminList.add(admin);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar administradores", Toast.LENGTH_SHORT).show();
                });
    }
}
