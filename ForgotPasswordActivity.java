package com.anish.collegeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail, etVerificationCode;
    private Button btnSendCode, btnVerify, btnBackToLogin;
    private LinearLayout layoutVerificationCode, layoutPassword;
    private TextView tvPassword;

    private String generatedCode;
    private String userEmail;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etVerificationCode = findViewById(R.id.etVerificationCode);
        btnSendCode = findViewById(R.id.btnSendCode);
        btnVerify = findViewById(R.id.btnVerify);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        layoutVerificationCode = findViewById(R.id.layoutVerificationCode);
        layoutPassword = findViewById(R.id.layoutPassword);
        tvPassword = findViewById(R.id.tvPassword);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etEmail.getText().toString().trim();

                if (userEmail.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if email exists in database
                    User user = databaseHelper.getUser(userEmail);

                    if (user != null) {
                        // Email exists, generate and send verification code
                        generatedCode = generateVerificationCode();
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Verification code sent to " + userEmail + ": " + generatedCode,
                                Toast.LENGTH_LONG).show();

                        // Show verification UI
                        layoutVerificationCode.setVisibility(View.VISIBLE);
                        btnVerify.setVisibility(View.VISIBLE);
                        btnSendCode.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = etVerificationCode.getText().toString().trim();

                if (enteredCode.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
                } else if (!enteredCode.equals(generatedCode)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                } else {
                    // Code verified, get the actual password from database
                    User user = databaseHelper.getUser(userEmail);
                    if (user != null) {
                        // Show the actual password
                        layoutPassword.setVisibility(View.VISIBLE);
                        tvPassword.setText(user.getPassword());

                        // Update UI
                        btnVerify.setVisibility(View.GONE);
                        btnBackToLogin.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private String generateVerificationCode() {
        // Generate a random 6-digit number
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}