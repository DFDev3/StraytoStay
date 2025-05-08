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
import com.example.straytostay.Classes.Usuario;
import com.example.straytostay.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<Usuario> userList;
    private final OnItemClickListener listener;

    public UserAdapter(List<Usuario> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_card_shelter, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Usuario user = userList.get(position);
        holder.name.setText(user.getName());
        holder.address.setText(user.getAddress());
        holder.phone.setText(user.getPhone());
        holder.image.setImageBitmap(loadImage(user.getImageUrl()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
        void onItemClick(Usuario user);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone;
        ImageView image;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvEntityName);
            address = itemView.findViewById(R.id.tvEntityAddress);
            phone = itemView.findViewById(R.id.tvEntityPhone);
            image = itemView.findViewById(R.id.imgEntityLogo);
        }
    }

}

