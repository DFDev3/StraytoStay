package com.example.straytostay.Main.Admin;

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

public class AdminProfile extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ImageView profilePicture;
    private ImageButton btnChangeImage;
    private TextView tvName, tvEmail, tvPhone, tvAddress;
    private EditText etName, etEmail, etPhone, etAddress;
    private Button btnEditConfirm, btnForm, btnLogout, btnDelete;

    private TextView adoptionStatus;
    private boolean isEditing = false;
    private String viewedUserId; // uid of the profile to display

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_profile_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        profilePicture = view.findViewById(R.id.profile_picture_admin);
        btnChangeImage = view.findViewById(R.id.btn_change_image_admin);
        btnEditConfirm = view.findViewById(R.id.btn_edit_confirm_admin);

        tvName = view.findViewById(R.id.profile_name_admin);
        tvEmail = view.findViewById(R.id.profile_email_admin);
        tvPhone = view.findViewById(R.id.profile_phone_admin);
        tvAddress = view.findViewById(R.id.profile_address_admin);

        btnLogout = view.findViewById(R.id.profile_logout_button_admin);

        btnEditConfirm.setOnClickListener(v -> {
            if (!isEditing) {
                enableEditFeatures();
            } else {
                saveProfileChanges();
            }
        });


        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), Login.class));
            requireActivity().finish();
        });

        loadProfile();

        return view;
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
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            populateProfile(snapshot);

                        } else {
                            Toast.makeText(getContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error loading profile", e));
        }
    }

    private void populateProfile(DocumentSnapshot snapshot) {
        String name = snapshot.getString("name");
        String lastName = snapshot.getString("lastName");
        String email = snapshot.getString("email");
        String phone = snapshot.getString("phone");
        String address = snapshot.getString("address");
        String image = snapshot.getString("imageUrl");


        if (image != null && !image.isEmpty()) {
            loadImage(image);
        }

        // Set data with fallback if any field is null
        tvName.setText((name != null ? name : "No name provided") + " " + (lastName != null ? lastName : ""));
        tvEmail.setText(email != null ? email : "Email not provided");
        tvPhone.setText(phone != null ? phone : "Phone not registered");
        tvAddress.setText(address != null ? address : "Address not registered");
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
                    .addOnFailureListener(e -> Log.e("AdminProfileFragment", "Error al actualizar perfil", e));
        }

    }
}
