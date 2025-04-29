package com.example.straytostay.Main.Admin;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.straytostay.StartUp.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ImageView profileImage;
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
        profileImage = view.findViewById(R.id.profile_picture_admin);
        btnChangeImage = view.findViewById(R.id.btn_change_image_admin);

        tvName = view.findViewById(R.id.profile_name_admin);
        tvEmail = view.findViewById(R.id.profile_email_admin);
        tvPhone = view.findViewById(R.id.profile_phone_admin);
        tvAddress = view.findViewById(R.id.profile_address_admin);

        btnLogout = view.findViewById(R.id.profile_logout_button_admin);
        btnDelete = view.findViewById(R.id.btn_delete_account_admin);


        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
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
                            String name = snapshot.getString("name");
                            String lastName = snapshot.getString("lastName");
                            String email = snapshot.getString("email");
                            String phone = snapshot.getString("phone");
                            String address = snapshot.getString("address");

                            // Set data with fallback if any field is null
                            tvName.setText((name != null ? name : "No name provided") + " " + (lastName != null ? lastName : ""));
                            tvEmail.setText(email != null ? email : "Email not provided");
                            tvPhone.setText(phone != null ? phone : "Phone not registered");
                            tvAddress.setText(address != null ? address : "Address not registered");

                        } else {
                            Toast.makeText(getContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error loading profile", e));
        }
    }

}
