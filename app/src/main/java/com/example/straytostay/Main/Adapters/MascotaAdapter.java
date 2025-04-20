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

import com.example.straytostay.Classes.Mascota;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.R;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private final List<Mascota> mascotaList;
    private final Context context;

    public MascotaAdapter(Context context, List<Mascota> mascotaList) {
        this.context = context;
        this.mascotaList = mascotaList;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comp_card_pet, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotaList.get(position);
        holder.nombre.setText(mascota.getNombre());
        holder.tipo.setText(mascota.getTipo());
        holder.edad.setText("Edad: " + mascota.getEdad());

        if (mascota.getImagenUrl() != null && !mascota.getImagenUrl().isEmpty()) {
            byte[] imageBytes = Base64.decode(mascota.getImagenUrl(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imagen.setImageBitmap(bitmap);
        }

        holder.detail.setOnClickListener(v -> {
            Mascota mascotaChosen = mascotaList.get(position);

            Bundle bundle = new Bundle();
            bundle.putString("animalId", mascotaChosen.getId()); // assuming getId() returns the Firebase ID


            AnimalDetail fragment = new AnimalDetail();
            fragment.setArguments(bundle);

            FragmentTransaction transaction = ((AppCompatActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.fragment_container, fragment); // your container ID
            transaction.addToBackStack(null);
            transaction.commit();
        });


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
