package com.example.straytostay.Main.Adoptante;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Main.Adapters.MascotaAdapter;
import com.example.straytostay.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EntityDetail extends Fragment {

    private ImageView imageAnimal;
    private TextView textNombre, textPhone, textEmail, textAddress, textNit, textMision, textMisionLabel, textWebsite, textServicios, textServiciosLabel, textProductos, textProductosLabel;
    private LinearLayout phoneListContainer;
    private String uid;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private MascotaAdapter petAdapter;
    private List<Mascota> mascotasList = new ArrayList<>();
    private String RefugioName;
    public static EntityDetail newInstance(String uid) {
        EntityDetail fragment = new EntityDetail();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_shelter_detail, container, false);

        db = FirebaseFirestore.getInstance();
        // Obtener ID del animal desde los argumentos
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
            Log.e("||||||||||||||||","uid: " + uid);
        }

        // Referencias UI
        imageAnimal = view.findViewById(R.id.imageShelter);
        textNombre = view.findViewById(R.id.tvName);
        textAddress = view.findViewById(R.id.tvAddress);
        textWebsite = view.findViewById(R.id.tvWebsite);
        textEmail = view.findViewById(R.id.tvEmail);
        textNit = view.findViewById(R.id.tvNit);

        phoneListContainer = view.findViewById(R.id.detailphoneContainer);

        textServiciosLabel = view.findViewById(R.id.tvServicesLabel);
        textServicios = view.findViewById(R.id.tvServices);

        textProductosLabel = view.findViewById(R.id.tvProductsLabel);
        textProductos = view.findViewById(R.id.tvProducts);

        textMision = view.findViewById(R.id.tvMision);
        textMisionLabel = view.findViewById(R.id.tvMisionLabel);

        recyclerView = view.findViewById(R.id.recyclerMascotasShelterDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        petAdapter = new MascotaAdapter(requireContext(),mascotasList);
        recyclerView.setAdapter(petAdapter);



        loadShelterDetails(uid);

        return view;
    }

    private void loadShelterDetails(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Try fetching from "shelters" collection first
        db.collection("entities").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        RefugioName = snapshot.getString("name");
                        populateShelterUI(snapshot); // If found in shelters
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ShelterDetail", "Error fetching from entities", e);
                });
    }

    private void populateShelterUI(DocumentSnapshot snapshot) {


        textServicios.setVisibility(View.GONE);
        textServiciosLabel.setVisibility(View.GONE);
        textProductos.setVisibility(View.GONE);
        textProductosLabel.setVisibility(View.GONE);
        textMision.setVisibility(View.GONE);
        textMisionLabel.setVisibility(View.GONE);
        textNit.setVisibility(View.GONE);

        String name = snapshot.getString("name");
        String email = snapshot.getString("email");
        String address = snapshot.getString("address");
        String mision = snapshot.getString("mission"); // Shelter only
        String servicios = snapshot.getString("servicios"); // Vet only
        String productos = snapshot.getString("productos");
        String nit = snapshot.getString("nit");
        String imageUrl = snapshot.getString("imageUrl");
        List<String> phoneList = (List<String>) snapshot.get("phoneList");


        textNombre.setText(name);
        textEmail.setText(email);
        textAddress.setText(address);
        if (mision != null) {
            textMisionLabel.setVisibility(View.VISIBLE);
            textMision.setVisibility(View.VISIBLE);
            textMision.setText(mision);
        }

        if (servicios != null) {
            textServiciosLabel.setVisibility(View.VISIBLE);
            textServicios.setVisibility(View.VISIBLE);
            textProductosLabel.setVisibility(View.VISIBLE);
            textProductos.setVisibility(View.VISIBLE);
            textNit.setVisibility(View.VISIBLE);

            textServicios.setText(servicios);
            textProductos.setText(productos);
            textNit.setText("NIT - " + nit);
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            loadImage(imageUrl);
        }
        displayPhoneList(phoneList);
        cargarMascotas(RefugioName);
    }





    private void loadImage(String base64Image) {
        // Decode the Base64 string into a Bitmap
        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageAnimal.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            Log.e("ShelterDetailFragment", "Error loading image", e);
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPhoneList(List<String> phoneList) {
        phoneListContainer.removeAllViews(); // Clear previous views if any}
        Log.d("phoneList", phoneList.toString());

        for (String phone : phoneList) {
            TextView phoneView = new TextView(requireContext());
            phoneView.setText(phone);
            phoneView.setTextSize(16);
            phoneView.setPadding(8, 10, 8, 8);
            phoneView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black)); // Optional
            phoneView.setBackgroundResource(R.drawable.edittext_background); // Optional, if you want style

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 12, 0, 0);  // Top margin of 12dp (convert to px if needed)

            phoneView.setLayoutParams(params);
            phoneListContainer.addView(phoneView);
        }
    }


    private void cargarMascotas(String refugioNombre) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mascotas")
                .whereEqualTo("refugio", refugioNombre)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mascotasList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Mascota mascota = doc.toObject(Mascota.class);
                        mascota.setAid(doc.getId()); // UID for passing to detail
                        mascotasList.add(mascota);
                    }
                    petAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar mascotas", Toast.LENGTH_SHORT).show();
                });
    }

}


