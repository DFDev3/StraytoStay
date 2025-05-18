package com.example.straytostay.Main.Admin;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.straytostay.Classes.Recurso;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PostTips extends Fragment {
    private static final int IMAGE_PICK_CODE = 1000;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri imageUri;
    private String encodedImageBase64;
    private Spinner spinnerRecurso;
    private EditText tvTitulo, tvLink;
    private ImageView imageView;
    private Button seleccionarImagen, upload;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_tips_fragment, container, false);

        spinnerRecurso = view.findViewById(R.id.spinnerTipoRecurso);
        tvTitulo = view.findViewById(R.id.editTitulo);
        tvLink = view.findViewById(R.id.editLink);
        imageView = view.findViewById(R.id.previewImage);
        seleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen);
        upload = view.findViewById(R.id.btnSubirRecurso);

        String[] tipos = {"Video", "Revista", "Libro"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tipos);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecurso.setAdapter(tipoAdapter);

        seleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        upload.setOnClickListener(v -> subirRecurso());


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void subirRecurso() {
        String titulo = tvTitulo.getText().toString().trim();
        String link = tvLink.getText().toString().trim();
        String tipo = spinnerRecurso.getSelectedItem().toString();

        Recurso dummy = new Recurso();

        db.collection("recursos").add(dummy)
                .addOnSuccessListener(documentReference -> {
                    String rid = documentReference.getId();

                    Recurso recurso = new Recurso(rid, tipo, titulo, link, encodedImageBase64);

                    db.collection("recursos")
                            .document(rid)
                            .set(recurso)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Recurso guardado correctamente", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Error al guardar recurso", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show());

    }
}
