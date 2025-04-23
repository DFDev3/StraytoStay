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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.straytostay.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ShelterDetailFragment extends Fragment {

    private ImageView imageAnimal;
    private TextView textNombre, textPhone, textEmail, textAddress, textNit, textMision, textMisionLabel, textWebsite, textServicios, textServiciosLabel, textProductos, textProductosLabel;
    private String uid;
    private FirebaseFirestore db;

    public static ShelterDetailFragment newInstance(String uid) {
        ShelterDetailFragment fragment = new ShelterDetailFragment();
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
        textPhone = view.findViewById(R.id.tvPhone);
        textEmail = view.findViewById(R.id.tvEmail);
        textNit = view.findViewById(R.id.tvNit);

        textServiciosLabel = view.findViewById(R.id.tvServicesLabel);
        textServicios = view.findViewById(R.id.tvServices);

        textProductosLabel = view.findViewById(R.id.tvProductsLabel);
        textProductos = view.findViewById(R.id.tvProducts);

        textMision = view.findViewById(R.id.tvMision);
        textMisionLabel = view.findViewById(R.id.tvMisionLabel);



        loadShelterDetails(uid);

        return view;
    }

    private void loadShelterDetails(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Try fetching from "shelters" collection first
        db.collection("shelters").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        populateShelterUI(snapshot); // If found in shelters
                    } else {
                        // If not found, check "vets"
                        db.collection("vets").document(uid).get()
                                .addOnSuccessListener(vetSnapshot -> {
                                    if (vetSnapshot.exists()) {
                                        populateShelterUI(vetSnapshot); // Found in vets
                                    } else {
                                        Toast.makeText(getContext(), "Shelter/Vet not found.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("ShelterDetail", "Error fetching from vets", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ShelterDetail", "Error fetching from shelters", e);
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
        String phone = snapshot.getString("phone");
        String address = snapshot.getString("address");
        String mision = snapshot.getString("mission"); // Shelter only
        String servicios = snapshot.getString("servicios"); // Vet only
        String productos = snapshot.getString("productos");
        String nit = snapshot.getString("nit");
        String imageUrl = snapshot.getString("imageUrl");


        // TODO: Replace with your actual view updates
        textNombre.setText(name);
        textEmail.setText(email);
        textPhone.setText(phone);
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
}
