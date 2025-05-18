package com.example.straytostay.Main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.example.straytostay.Classes.Recurso;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.R;

import java.util.List;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder> {

    private final List<Recurso> recursosList;
    private final RecursoAdapter.OnItemClickListener listener;

    public RecursoAdapter(List<Recurso> recursosList, RecursoAdapter.OnItemClickListener listener) {
        this.recursosList = recursosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comp_card_tips, parent, false);
        return new RecursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecursoViewHolder holder, int position) {
        Recurso recurso = recursosList.get(position);
        holder.titulo.setText(recurso.getTitulo());
        holder.image.setImageBitmap(loadImage(recurso.getImagenUrl()));

        if (recurso.getImagenUrl() != null && !recurso.getImagenUrl().isEmpty()) {
            byte[] imageBytes = Base64.decode(recurso.getImagenUrl(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.image.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(recurso);
        });
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
        void onItemClick(Recurso res);
    }

    @Override
    public int getItemCount() {
        return recursosList.size();
    }

    public static class RecursoViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView titulo;

        public RecursoViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgResourceImage);
            titulo = itemView.findViewById(R.id.tvResourceTitle);

        }
    }
}
