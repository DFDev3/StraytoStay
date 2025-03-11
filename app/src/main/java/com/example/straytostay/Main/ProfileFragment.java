package com.example.straytostay.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.straytostay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView nameText, emailText, phoneText, addressText, adminIdText, userTypeText;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        nameText = view.findViewById(R.id.profile_name);
        emailText = view.findViewById(R.id.profile_email);
        phoneText = view.findViewById(R.id.profile_phone);
        addressText = view.findViewById(R.id.profile_address);
        adminIdText = view.findViewById(R.id.profile_admin_id);
        logoutButton = view.findViewById(R.id.profile_logout_button);

        fetchUserDetails();

        // Logout button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            getActivity().finish(); // Closes the app or navigate to login
        });

        return view;
    }

    private void fetchUserDetails() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            String userEmail = firebaseUser.getEmail();
            emailText.setText("Email: " + (userEmail != null ? userEmail : "N/A"));

            // Try fetching user info from Firestore
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Log.d("ProfileFragment", "User data found in Firestore (users)");
                            displayUserInfo(documentSnapshot, "User");
                        } else {
                            // If user not found in "users", try "shelters"
                            db.collection("shelters").document(uid)
                                    .get()
                                    .addOnSuccessListener(shelterSnapshot -> {
                                        if (shelterSnapshot.exists()) {
                                            Log.d("ProfileFragment", "Shelter data found in Firestore (shelters)");
                                            displayUserInfo(shelterSnapshot, "Shelter");
                                        } else {
                                            Log.e("ProfileFragment", "User/Shelter not found in Firestore");
                                            userTypeText.setText("User Type: Not found");
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error fetching shelter data", e));
                        }
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error fetching user data", e));
        } else {
            Log.e("ProfileFragment", "No authenticated user found");
        }
    }

    private void displayUserInfo(DocumentSnapshot documentSnapshot, String userType) {
        String name = documentSnapshot.getString("name");
        String phone = documentSnapshot.getString("phone");
        String address = documentSnapshot.contains("address") ? documentSnapshot.getString("address") : "N/A";
        Long adminId = documentSnapshot.contains("adminID") ? documentSnapshot.getLong("adminID") : -1;

        nameText.setText("Name: " + (name != null ? name : "N/A"));
        phoneText.setText("Phone: " + (phone != null ? phone : "N/A"));
        addressText.setText("Address: " + address);
        adminIdText.setText("AdminID: " + adminId);
        userTypeText.setText("User Type: " + userType);

        Log.d("ProfileFragment", "Fetched user data: Name=" + name + ", Phone=" + phone + ", Address=" + address + ", AdminID=" + adminId);
    }
}
