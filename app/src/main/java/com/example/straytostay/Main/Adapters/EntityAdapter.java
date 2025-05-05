package com.example.straytostay.Main.Adapters;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Entity;
import com.example.straytostay.R;

import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.ShelterViewHolder> {

    private final List<Entity> entityList;
    private final OnItemClickListener listener;

    public EntityAdapter(List<Entity> entityList, OnItemClickListener listener) {
        this.entityList = entityList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShelterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_card_shelter, parent, false);
        return new ShelterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterViewHolder holder, int position) {
        Entity entity = entityList.get(position);
        holder.name.setText(entity.getName());
        holder.address.setText(entity.getAddress());
        holder.phone.setText(entity.getPhoneList() != null && !entity.getPhoneList().isEmpty() ? entity.getPhoneList().get(0) : "No phone available");
        holder.image.setImageBitmap(loadImage(entity.getImageUrl()));

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

    private Bitmap loadImage(String base64Image) {
        // Decode the Base64 string into a Bitmap
        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedBitmap;
        } catch (Exception e) {
            Log.e("UserProfile", "Error loading image", e);
            return null;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Entity shelter);
    }

    public static class ShelterViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone;
        ImageView image;

        public ShelterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvEntityName);
            address = itemView.findViewById(R.id.tvEntityAddress);
            phone = itemView.findViewById(R.id.tvEntityPhone);
            image = itemView.findViewById(R.id.imgEntityLogo);
        }
    }
}

