package com.example.straytostay.Main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.straytostay.Classes.Mascota;
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

//        // Load image if available
//        if (mascota.getImagenUrl() != null && !mascota.getImagenUrl().isEmpty()) {
//            Glide.with(context)
//                    .load(mascota.getImagenUrl())
//                    .placeholder(R.drawable.placeholder_image)
//                    .into(holder.imagen);
//        } else {
//            holder.imagen.setImageResource(R.drawable.placeholder_image);
//        }
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre, tipo, edad, descripcion;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.animalImage);
            nombre = itemView.findViewById(R.id.animalName);
            tipo = itemView.findViewById(R.id.animalBreed);
            edad = itemView.findViewById(R.id.animalAge);
        }
    }
}
