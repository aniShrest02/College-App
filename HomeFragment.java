package com.anish.collegeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private TextView tvUserName, tvUserDepartment;
    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserDepartment = view.findViewById(R.id.tv_user_department);

        // Get database helper instance
        dbHelper = DatabaseHelper.getInstance(requireContext());

        // Get user email from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userEmail = prefs.getString("user_email", null);

        // Load and display user data
        loadUserData();

        return view;
    }

    private void loadUserData() {
        if (userEmail != null) {
            User user = dbHelper.getUser(userEmail);
            if (user != null) {
                tvUserName.setText(user.getName());
                tvUserDepartment.setText(user.getDepartment());
            }
        }
    }
}