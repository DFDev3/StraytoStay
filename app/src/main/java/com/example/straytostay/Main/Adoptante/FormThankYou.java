package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.straytostay.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormThankYou extends Fragment {
    private HashMap<String, Float> normalizedScores;


    public FormThankYou() {
        // Constructor vacío obligatorio
    }

    public static FormThankYou newInstance(HashMap<String, Float> normalizedScores) {
        FormThankYou fragment = new FormThankYou();
        Bundle args = new Bundle();
        args.putSerializable("normalizedScores", normalizedScores);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_thank_you, container, false);

        if (getArguments() != null) {
            HashMap<String, Float> normalizedScores = (HashMap<String, Float>) getArguments().getSerializable("normalizedScores");

            if (normalizedScores != null) {

                RadarChart radarChart = view.findViewById(R.id.radarChart);

                List<RadarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();

                for (Map.Entry<String, Float> entry : normalizedScores.entrySet()) {
                    if (!entry.getKey().equalsIgnoreCase("total")) {  // skip total
                        entries.add(new RadarEntry(entry.getValue()));
                        labels.add(entry.getKey());
                    }
                }

                RadarDataSet dataSet = new RadarDataSet(entries, "Your Scores");
                dataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
                dataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]);
                dataSet.setDrawFilled(true);
                dataSet.setLineWidth(2f);

                RadarData data = new RadarData(dataSet);
                data.setDrawValues(true);

                radarChart.setData(data);
                radarChart.getDescription().setEnabled(false);

                XAxis xAxis = radarChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setTextSize(12f);

                YAxis yAxis = radarChart.getYAxis();
                yAxis.setAxisMinimum(0f);
                yAxis.setAxisMaximum(1f);  // because it's normalized
                yAxis.setLabelCount(5, true);

                radarChart.invalidate(); // refresh

            } else {
                Log.e("FormThankYou", "Normalized scores are null.");
            }
        } else {
            Log.e("FormThankYou", "Arguments are null.");
        }



        return view;
    }
}
