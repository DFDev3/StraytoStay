package com.example.straytostay.Main.Shelter;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.straytostay.Classes.Noticia;
import com.example.straytostay.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetail extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RadarChart radarChart;
    private ImageView profilePicture;
    private TextView tvPhone, tvAddress, tvName;
    private Button btnWinner;
    private String uid, animalId;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_user_detail, container, false);

        profilePicture = view.findViewById(R.id.userPfp);
        tvName = view.findViewById(R.id.textName);
        tvAddress = view.findViewById(R.id.textAddress);
        tvPhone = view.findViewById(R.id.textPhone);
        radarChart = view.findViewById(R.id.radarChart);
        btnWinner = view.findViewById(R.id.btn_winner);


        if (getArguments() != null) {
            uid = getArguments().getString("uid");
            animalId = getArguments().getString("aid");
        }

        loadRadarChart();
        loadDetail();

        btnWinner.setOnClickListener(v -> {
            Noticia unused = new Noticia();
            loadAnimalData(animalId, (name, imageUrl) -> {
                db.collection("noticias").add(unused)
                        .addOnSuccessListener(newsSnapshot -> {
                            String nid = newsSnapshot.getId();

                            Noticia news = new Noticia(nid, "Nuevo Integrante de la Familia! " + name + " ha encontrado su nuevo hogar!", imageUrl);
                            db.collection("noticias")
                                    .document(nid)
                                    .set(news);
                        });
            });


            Log.d("a", "a");
            new AlertDialog.Builder(requireContext())
                    .setTitle("Aprobar Adoptante?")
                    .setMessage("Se iniciará el proceso de adopción con el usuario")
                    .setPositiveButton("Ir", (dialog, which) -> {
                        db.collection("mascotas")
                                .document(animalId)
                                .update("appliedBy", Collections.singletonList(uid), "estado", "Adoptada")
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "appliedBy updated with UID"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating appliedBy", e));

                        Bundle bundle = new Bundle();
                        bundle.putString("animalId", animalId);

                        AdoptedList fragment = new AdoptedList();
                        fragment.setArguments(bundle);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    })
                    .show();
        });

        return view;
    }

    private void loadAnimalData(String animalId, AnimalDataCallback callback) {
        db.collection("mascotas").document(animalId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("nombre");
                String imageUrl = documentSnapshot.getString("imageUrl");
                callback.onDataLoaded(name, imageUrl);
            } else {
                callback.onDataLoaded(null, null); // Handle nulls safely
            }
        }).addOnFailureListener(e -> {
            callback.onDataLoaded(null, null); // On error
        });
    }





    private void loadDetail() {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        populateDetail(snapshot);

                    } else {
                        Toast.makeText(getContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("ProfileFragment", "Error loading profile", e));
    }


    private void populateDetail(DocumentSnapshot snapshot) {
        String name = snapshot.getString("name");
        String phone = snapshot.getString("phone");
        String address = snapshot.getString("address");
        String image = snapshot.getString("imageUrl");

        if (image != null && !image.isEmpty()) {
            loadImage(image);
        }

        // Set data with fallback if any field is null
        tvName.setText("Nombre del Postulante: " + (name != null ? name : "No name provided"));
        tvAddress.setText("Dirección: " + (address != null ? address : "Email not provided"));
        tvPhone.setText("Teléfono: " + (phone != null ? phone : "Phone not registered"));
    }

    private void loadImage(String base64Image) {
        // Decode the Base64 string into a Bitmap
        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            Log.e("UserProfile", "Error loading image", e);
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRadarChart() {

        db.collection("users").document(uid).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                Map<String, Object> rawScores = (Map<String, Object>) snapshot.get("scores");
                HashMap<String, Float> scores = new HashMap<>();

                if (rawScores != null) {
                    for (Map.Entry<String, Object> entry : rawScores.entrySet()) {
                        Object value = entry.getValue();
                        if (value instanceof Number) {
                            scores.put(entry.getKey(), ((Number) value).floatValue());
                        }
                    }
                }

                if (scores != null) {

                    List<RadarEntry> entries = new ArrayList<>();
                    List<String> labels = new ArrayList<>();

                    for (Map.Entry<String, Float> entry : scores.entrySet()) {
                        if (!entry.getKey().equalsIgnoreCase("total")) {  // skip total
                            entries.add(new RadarEntry(entry.getValue()));
                            labels.add(entry.getKey());
                        }
                    }

                    Log.d("RadarChart", "Processed scores: " + scores);


                    RadarDataSet dataSet = new RadarDataSet(entries, "Your Scores");
                    int cyan = Color.CYAN;  // or use a custom hex value like Color.parseColor("#00BCD4")
                    dataSet.setColor(cyan);           // Line color
                    dataSet.setFillColor(cyan);       // Fill color inside the chart
                    dataSet.setValueTextColor(Color.BLACK); // Optional: color of the values

                    dataSet.setDrawFilled(true);
                    dataSet.setLineWidth(5f);

                    RadarData data = new RadarData(dataSet);
                    data.setDrawValues(true);

                    radarChart.setData(data);
                    radarChart.getDescription().setEnabled(false);

                    XAxis xAxis = radarChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setTextSize(12f);

                    YAxis yAxis = radarChart.getYAxis();
                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(100f);  // because it's normalized
                    yAxis.setLabelCount(5, true);

                    radarChart.invalidate(); // refresh
                }
            }
        });
    }

    public interface AnimalDataCallback {
        void onDataLoaded(String name, String imageUrl);
    }



}