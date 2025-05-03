package com.anish.collegeapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        setTitle("Rules");

        // Enable back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Handle back arrow click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
