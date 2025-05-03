package com.example.straytostay.Main.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.straytostay.Classes.Entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.R;

import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.ShelterViewHolder> {

    private List<Entity> entityList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Entity shelter);
    }

    public EntityAdapter(List<Entity> entityList, OnItemClickListener listener) {
        this.entityList = entityList;
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
        Entity entity = entityList.get(position);
        holder.name.setText(entity.getName());
        holder.address.setText(entity.getAddress());
        holder.phone.setText(entity.getPhoneList() != null && !entity.getPhoneList().isEmpty() ? entity.getPhoneList().get(0) : "No phone available");


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(entity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
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
