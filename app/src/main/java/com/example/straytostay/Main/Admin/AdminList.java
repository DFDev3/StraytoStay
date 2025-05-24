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

import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.Main.Adapters.UserAdapter;
import com.example.straytostay.Main.Shelter.PostAPet;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;



public class AdminList extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Usuario> userList;
    private Button Agregar;

    private UserAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_admin_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerAdminList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        // Inside onCreateView, replace your adapter initialization
        adapter = new UserAdapter(userList, user -> {
            Bundle bundle = new Bundle();
            bundle.putString("uid", user.getUid());  // Pass only the UID

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
        cargarAdmins();
        return view;
    }

    private void cargarAdmins() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("adminId", 2)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Usuario admin = doc.toObject(Usuario.class);
                        admin.setUid(doc.getId()); // UID for passing to detail
                        userList.add(admin);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar administradores", Toast.LENGTH_SHORT).show();
                });
    }
}
