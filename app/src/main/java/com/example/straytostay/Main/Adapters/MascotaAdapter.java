package com.example.straytostay.Main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Entity;
import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.R;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private final List<Mascota> mascotaList;
    private final MascotaAdapter.OnItemClickListener listener;

    public MascotaAdapter(List<Mascota> mascotaList, MascotaAdapter.OnItemClickListener listener) {
        this.mascotaList = mascotaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comp_card_pet, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotaList.get(position);
        holder.nombre.setText(mascota.getNombre());
        holder.tipo.setText(mascota.getTipo());
        holder.edad.setText("Edad: " + mascota.getEdad() + " año(s)");

        if (mascota.getImagenUrl() != null && !mascota.getImagenUrl().isEmpty()) {
            byte[] imageBytes = Base64.decode(mascota.getImagenUrl(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imagen.setImageBitmap(bitmap);
        }

        holder.detail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(mascota);
            }
        });


    }
    public interface OnItemClickListener {
        void onItemClick(Mascota pet);
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre, tipo, edad;
        Button detail;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.animalImage);
            nombre = itemView.findViewById(R.id.animalName);
            tipo = itemView.findViewById(R.id.animalBreed);
            edad = itemView.findViewById(R.id.animalAge);
            detail = itemView.findViewById(R.id.btnVerMas);
        }
    }
}
