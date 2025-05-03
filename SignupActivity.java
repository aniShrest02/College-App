package com.anish.collegeapp;

import static com.anish.collegeapp.R.layout.activity_signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etPhone, etDepartment, etStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_signup);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone); // New phone field
        etDepartment = findViewById(R.id.etDepartment);
        etStudentId = findViewById(R.id.etStudentId);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnSignUp.setOnClickListener(v -> handleSignUp());
        tvLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void handleSignUp() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                phone.isEmpty() || department.isEmpty() || studentId.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user object with phone number
        User newUser = new User(name, email, password, department, studentId, phone);

        // Add user to database
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        long userId = dbHelper.addUser(newUser);

        if (userId != -1) {
            // Signup successful
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Save user email to SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
            editor.putString("user_email", email);
            editor.apply();

            navigateToLogin();
        } else {
            Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLogin() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }
}