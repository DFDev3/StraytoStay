package com.example.straytostay.Main.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.R;

import java.util.List;

public class ShelterAdapter extends RecyclerView.Adapter<ShelterAdapter.ShelterViewHolder> {

    private List<Usuario> shelterList;

    public ShelterAdapter(List<Usuario> shelterList) {
        this.shelterList = shelterList;
    }

    @NonNull
    @Override
    public ShelterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_card_shelter, parent, false);
        return new ShelterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterViewHolder holder, int position) {
        Usuario shelter = shelterList.get(position);
        holder.nameTextView.setText(shelter.getName());
        holder.addressTextView.setText(shelter.getAddress());
        holder.phoneTextView.setText(shelter.getPhone());
    }

    @Override
    public int getItemCount() {
        return shelterList.size();
    }

    public static class ShelterViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView, phoneTextView;

        public ShelterViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvEntityName);
            addressTextView = itemView.findViewById(R.id.tvEntityAddress);
            phoneTextView = itemView.findViewById(R.id.tvEntityPhone);
        }
    }
}
