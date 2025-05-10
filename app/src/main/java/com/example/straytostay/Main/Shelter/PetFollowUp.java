package com.example.straytostay.Main.Shelter;

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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PetFollowUp extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String animalId;

    private ImageView imgPet, imgUser;
    private TextView txtPetName, txtPetDescription;
    private TextView txtAdoptanteName, txtAdoptanteEmail;
    private RadioGroup radioGroupFranja;
    private Button btnAgendarCita;
    private RadarChart radarChart;
    private DatePicker datePicker;
    private String userUid;

    public PetFollowUp() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_follow_up, container, false);

        if (getArguments() != null) {
            animalId = getArguments().getString("animalId");
        }

        imgPet = view.findViewById(R.id.imgPet);
        imgUser = view.findViewById(R.id.imgUser);
        txtPetName = view.findViewById(R.id.txtPetName);
        txtPetDescription = view.findViewById(R.id.txtPetDescription);
        txtAdoptanteName = view.findViewById(R.id.txtAdoptanteName);
        txtAdoptanteEmail = view.findViewById(R.id.txtAdoptanteEmail);
        radarChart = view.findViewById(R.id.radarChart);
        btnAgendarCita = view.findViewById(R.id.btnAgendarCita);
        datePicker = view.findViewById(R.id.datePicker);
        radioGroupFranja = view.findViewById(R.id.radioGroupFranja);

        loadData();

        btnAgendarCita.setOnClickListener(v -> {
            // 1. Get selected date
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth(); // Note: 0-based!
            int year = datePicker.getYear();

            // Format date (e.g., 09/05/2025)
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year);

            // 2. Get selected time slot
            int selectedId = radioGroupFranja.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getContext(), "Selecciona una franja horaria", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadio = view.findViewById(selectedId);
            String franja = selectedRadio.getText().toString();

            // 3. Create appointment data
            Map<String, Object> cita = new HashMap<>();
            cita.put("fecha", selectedDate);
            cita.put("franja", franja);
            cita.put("animalId", animalId); // optional: link it to mascota
            cita.put("uid", userUid); // optional: user ID

            // 4. Save to Firestore
            FirebaseFirestore.getInstance()
                    .collection("citas")
                    .add(cita)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(getContext(), "Cita agendada", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error al guardar la cita", e);
                        Toast.makeText(getContext(), "Error al agendar", Toast.LENGTH_SHORT).show();
                    });
        });

        return view;

    }

    private void loadData() {
        db.collection("mascotas").document(animalId).get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) return;

            String name = snapshot.getString("nombre");
            String description = snapshot.getString("descripcion");
            List<String> appliedBy = (List<String>) snapshot.get("appliedBy");

            txtPetName.setText(name);
            txtPetDescription.setText(description);

            // Assuming only one adoptante left in the array after status = Adoptada
            if (appliedBy != null && !appliedBy.isEmpty()) {
                userUid = appliedBy.get(0);
                db.collection("users").document(userUid).get().addOnSuccessListener(userSnap -> {
                    if (!userSnap.exists()) return;
                    txtAdoptanteName.setText(userSnap.getString("nombre"));
                    txtAdoptanteEmail.setText(userSnap.getString("email"));
                    loadImages(snapshot,userSnap);
                    loadRadarChart();

                });
            }
        });
    }

    private void loadImages(DocumentSnapshot pet, DocumentSnapshot user){
        String petImage = pet.getString("imageUrl");
        String userImage = user.getString("imageUrl");
        Log.d("URLS", petImage);
        Log.d("URLS", userImage);

        byte[] petBytes = Base64.decode(petImage, Base64.DEFAULT);
        Bitmap petbitmap = BitmapFactory.decodeByteArray(petBytes, 0, petBytes.length);
        imgPet.setImageBitmap(petbitmap);

        byte[] userBytes = Base64.decode(userImage, Base64.DEFAULT);
        Bitmap userbitmap = BitmapFactory.decodeByteArray(userBytes, 0, userBytes.length);
        imgUser.setImageBitmap(userbitmap);

    }

    private void loadRadarChart() {

        db.collection("users").document(userUid).get().addOnSuccessListener(snapshot -> {
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
}
