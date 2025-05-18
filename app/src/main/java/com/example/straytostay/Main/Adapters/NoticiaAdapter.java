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
import com.example.straytostay.Classes.Noticia;
import com.example.straytostay.Classes.Recurso;
import com.example.straytostay.Main.Adoptante.AnimalDetail;
import com.example.straytostay.R;

import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {

    private final List<Noticia> noticiasList;
    private final NoticiaAdapter.OnItemClickListener listener;

    public NoticiaAdapter(List<Noticia> noticiaList, NoticiaAdapter.OnItemClickListener listener) {
        this.noticiasList = noticiaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comp_card_news, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        Noticia noticia = noticiasList.get(position);
        holder.titulo.setText(noticia.getTitulo());
        holder.image.setImageBitmap(loadImage(noticia.getImageUrl()));
        holder.descripcion.setText(noticia.getDescripcion());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(noticia);
            }
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
        void onItemClick(Noticia news);
    }

    @Override
    public int getItemCount() {
        return noticiasList.size();
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView titulo, descripcion;

        public NoticiaViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cardImage);
            titulo = itemView.findViewById(R.id.cardTitle);
            descripcion = itemView.findViewById(R.id.cardDescription);
        }
    }
}
