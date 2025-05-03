package com.anish.collegeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private TextInputEditText etName, etStudentId, etEmail, etDepartment, etPhone;
    private DatabaseHelper dbHelper;
    private String currentUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        Button btnSaveProfile = view.findViewById(R.id.btn_save_profile);

        // Initialize input fields
        etName = view.findViewById(R.id.et_name);
        etStudentId = view.findViewById(R.id.et_student_id);
        etEmail = view.findViewById(R.id.et_email);
        etDepartment = view.findViewById(R.id.et_department);
        etPhone = view.findViewById(R.id.et_phone);


        dbHelper = DatabaseHelper.getInstance(requireContext());

        // Get current user email from SharedPreferences
        getContext();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserEmail = sharedPreferences.getString("user_email", null);

        if (currentUserEmail == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return view;
        }

        btnSaveProfile.setOnClickListener(v -> updateProfile());
        loadUserData();

        return view;
    }

    private void loadUserData() {
        User currentUser = dbHelper.getUser(currentUserEmail);

        if (currentUser != null) {
            // Set header info
            tvUserName.setText(currentUser.getName());
            tvUserEmail.setText(currentUser.getEmail());

            // Set personal info
            etName.setText(currentUser.getName());
            etStudentId.setText(currentUser.getStudentId());
            etEmail.setText(currentUser.getEmail());
            etDepartment.setText(currentUser.getDepartment());
            etPhone.setText(currentUser.getPhone());

        } else {
            Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        String newName = Objects.requireNonNull(etName.getText()).toString().trim();
        String newStudentId = Objects.requireNonNull(etStudentId.getText()).toString().trim();
        String newEmail = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String newDepartment = Objects.requireNonNull(etDepartment.getText()).toString().trim();
        String newPhone = Objects.requireNonNull(etPhone.getText()).toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(getContext(), "Name and Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = dbHelper.getUser(currentUserEmail);
        if (currentUser != null) {
            currentUser.setName(newName);
            currentUser.setStudentId(newStudentId);
            currentUser.setEmail(newEmail);
            currentUser.setDepartment(newDepartment);
            currentUser.setPhone(newPhone);

            if (dbHelper.updateUser(currentUser) > 0) {
                tvUserName.setText(newName);
                tvUserEmail.setText(newEmail);

                if (!newEmail.equals(currentUserEmail)) {
                    getContext();
                    SharedPreferences.Editor editor = requireActivity()
                            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            .edit();
                    editor.putString("user_email", newEmail);
                    editor.apply();
                    currentUserEmail = newEmail;
                }
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}