package com.example.straytostay.Main.Adoptante;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.straytostay.R;
import com.example.straytostay.StartUp.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView nameText, emailText, phoneText, addressText;
    private EditText nameEdit, phoneEdit, addressEdit, emailEdit;
    private Button logoutButton, editButton;
    private ImageView profilePicture;
    private Uri selectedImageUri;

    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_profile_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameText = view.findViewById(R.id.profile_name);
        emailText = view.findViewById(R.id.profile_email);
        phoneText = view.findViewById(R.id.profile_phone);
        addressText = view.findViewById(R.id.profile_address);

        nameEdit = view.findViewById(R.id.edit_profile_name);
        emailEdit = view.findViewById(R.id.edit_profile_email);
        phoneEdit = view.findViewById(R.id.edit_profile_phone);
        addressEdit = view.findViewById(R.id.edit_profile_address);

        profilePicture = view.findViewById(R.id.profile_picture);
        logoutButton = view.findViewById(R.id.profile_logout_button);
        editButton = view.findViewById(R.id.btn_edit_confirm);

        fetchUserDetails();

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        editButton.setOnClickListener(v -> {
            if (!isEditing) {
                enterEditMode();
            } else {
                saveProfileChanges();
            }
        });

        profilePicture.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void enterEditMode() {
        isEditing = true;
        editButton.setText("Confirmar");

        nameText.setVisibility(View.GONE);
        phoneText.setVisibility(View.GONE);
        emailText.setVisibility(View.GONE);
        addressText.setVisibility(View.GONE);

        nameEdit.setVisibility(View.VISIBLE);
        phoneEdit.setVisibility(View.VISIBLE);
        emailEdit.setVisibility(View.VISIBLE);
        addressEdit.setVisibility(View.VISIBLE);

        nameEdit.setText(nameText.getText().toString());
        emailEdit.setText(nameText.getText().toString());
        phoneEdit.setText(phoneText.getText().toString());
        addressEdit.setText(addressText.getText().toString());
    }

    private void saveProfileChanges() {
        isEditing = false;
        editButton.setText("Editar información");

        nameText.setVisibility(View.VISIBLE);
        emailEdit.setVisibility(View.VISIBLE);
        phoneText.setVisibility(View.VISIBLE);
        addressText.setVisibility(View.VISIBLE);

        nameEdit.setVisibility(View.GONE);
        emailEdit.setVisibility(View.GONE);
        phoneEdit.setVisibility(View.GONE);
        addressEdit.setVisibility(View.GONE);

        String newName = nameEdit.getText().toString().trim();
        String newMail = emailEdit.getText().toString().trim();
        String newPhone = phoneEdit.getText().toString().trim();
        String newAddress = addressEdit.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid)
                    .update("name", newName,
                            "phone", newPhone,
                            "address", newAddress)
                    .addOnSuccessListener(aVoid -> {
                        nameText.setText(newName);
                        emailText.setText(newMail);
                        phoneText.setText(newPhone);
                        addressText.setText(newAddress);
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error al actualizar perfil", e));
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profilePicture.setImageURI(selectedImageUri);

            // Aquí puedes subir a Firebase Storage si lo deseas
        }
    }

    private void fetchUserDetails() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            emailText.setText(firebaseUser.getEmail());

            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String phone = documentSnapshot.getString("phone");
                            String address = documentSnapshot.getString("address");

                            nameText.setText(name != null ? name : "Sin nombre");
                            phoneText.setText(phone != null ? phone : "Sin teléfono");
                            addressText.setText(address != null ? address : "Sin dirección");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error obteniendo datos del usuario", e));
        }
    }
}
