package com.example.straytostay.Main.Shelter;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.straytostay.R;
import com.example.straytostay.StartUp.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EntityProfile extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ImageView profilePicture;
    private ImageButton btnChangeImage;
    private TextView tvName, tvEmail, tvPhone, tvAddress, tvNit, tvServices, tvProducts;
    private EditText etName, etEmail, etPhone, etAddress, etNit, etServices, etProducts;;
    private Button btnEditConfirm, btnLogout, btnDelete;

    private boolean isEditing = false;
    private String viewedUserId; // uid of the profile to display

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelter_profile_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        profilePicture = view.findViewById(R.id.profile_picture);
        btnChangeImage = view.findViewById(R.id.btn_change_image);

        tvName = view.findViewById(R.id.profile_name);
        tvEmail = view.findViewById(R.id.profile_email);
        tvPhone = view.findViewById(R.id.profile_phone);
        tvAddress = view.findViewById(R.id.profile_address);
        tvNit = view.findViewById(R.id.profile_NIT);

        etName = view.findViewById(R.id.edit_profile_name);
        etEmail = view.findViewById(R.id.edit_profile_email);
        etPhone = view.findViewById(R.id.edit_profile_phone);
        etAddress = view.findViewById(R.id.edit_profile_address);
        etNit = view.findViewById(R.id.edit_profile_NIT);

        btnEditConfirm = view.findViewById(R.id.btn_edit_confirm);
        btnLogout = view.findViewById(R.id.profile_logout_button);
        btnDelete = view.findViewById(R.id.btn_delete_account);

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), Login.class));
            requireActivity().finish();
        });

        btnEditConfirm.setOnClickListener(v -> {
            if (!isEditing) {
                enterEditMode();
            } else {
                saveProfileChanges();
            }
        });

        loadProfile();

        return view;
    }

    private void enterEditMode() {
        isEditing = true;
        btnEditConfirm.setText("Confirmar");

        tvName.setVisibility(View.GONE);
        tvPhone.setVisibility(View.GONE);
        tvEmail.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        tvNit.setVisibility(View.GONE);

        etName.setVisibility(View.VISIBLE);
        etPhone.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        etAddress.setVisibility(View.VISIBLE);
        etNit.setVisibility(View.VISIBLE);

        etName.setText(tvName.getText().toString());
        etEmail.setText(tvName.getText().toString());
        etPhone.setText(tvPhone.getText().toString());
        etAddress.setText(tvAddress.getText().toString());
        etNit.setText(tvNit.getText().toString());
    }

    private void saveProfileChanges() {
        isEditing = false;
        btnEditConfirm.setText("Editar información");

        tvName.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        tvPhone.setVisibility(View.VISIBLE);
        tvAddress.setVisibility(View.VISIBLE);

        etName.setVisibility(View.GONE);
        etEmail.setVisibility(View.GONE);
        etPhone.setVisibility(View.GONE);
        etAddress.setVisibility(View.GONE);

        String newName = etName.getText().toString().trim();
        String newMail = etEmail.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();
        String newAddress = etAddress.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid)
                    .update("name", newName,
                            "phone", newPhone,
                            "address", newAddress)
                    .addOnSuccessListener(aVoid -> {
                        tvName.setText(newName);
                        tvEmail.setText(newMail);
                        tvPhone.setText(newPhone);
                        tvAddress.setText(newAddress);
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error al actualizar perfil", e));
        }
    }

    //    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, 1001);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1001 && resultCode == getActivity().RESULT_OK && data != null) {
//            selectedImageUri = data.getData();
//            profilePicture.setImageURI(selectedImageUri);
//
//            // Aquí puedes subir a Firebase Storage si lo deseas
//        }
//    }
    private void loadProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // First, try in "shelters"
            db.collection("entities").document(uid).get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            populateProfile(snapshot);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error checking entities collection", e));
        }
    }

    private void populateProfile(DocumentSnapshot snapshot) {
        String name = snapshot.getString("name");
        String email = snapshot.getString("email");
        String phone = snapshot.getString("phone");
        String address = snapshot.getString("address");
        String nit = snapshot.getString("nit");
        String image = snapshot.getString("imageUrl");

        if (image != null && !image.isEmpty()) {
            loadImage(image);
        }

        tvName.setText((name != null ? name : "No name provided"));
        tvEmail.setText(email != null ? email : "Email not provided");
        tvPhone.setText(phone != null ? phone : "Phone not registered");
        tvAddress.setText(address != null ? address : "Address not registered");
        tvNit.setText(nit != null ? nit : "Not a vet");

        etName.setText(name != null ? name : "");
        etEmail.setText(email != null ? email : "");
        etPhone.setText(phone != null ? phone : "");
        etAddress.setText(address != null ? address : "");
        etNit.setText(nit != null ? nit : "");
    }

    private void loadImage(String base64Image) {
        // Decode the Base64 string into a Bitmap
        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            Log.e("UserProfile", "Error loading image", e);
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableEditFeatures() {
        btnChangeImage.setVisibility(View.VISIBLE);
        btnEditConfirm.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
    }
}
