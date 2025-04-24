package com.example.straytostay.Main.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Shelter;
import com.example.straytostay.R;

import java.util.List;

public class ShelterAdapter extends RecyclerView.Adapter<ShelterAdapter.ShelterViewHolder> {

    private List<Shelter> shelterList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Shelter shelter);
    }

    public ShelterAdapter(List<Shelter> shelterList, OnItemClickListener listener) {
        this.shelterList = shelterList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShelterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_card_shelter, parent, false);
        return  new ShelterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterViewHolder holder, int position) {
        Shelter shelter = shelterList.get(position);
        holder.name.setText(shelter.getName());
        holder.address.setText(shelter.getAddress());
        holder.phone.setText(shelter.getPhoneList() != null && !shelter.getPhoneList().isEmpty() ? shelter.getPhoneList().get(0) : "No phone available");


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(shelter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shelterList.size();
    }

    public static class ShelterViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone;

        public ShelterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvEntityName);
            address = itemView.findViewById(R.id.tvEntityAddress);
            phone = itemView.findViewById(R.id.tvEntityPhone);
        }
    }
}
