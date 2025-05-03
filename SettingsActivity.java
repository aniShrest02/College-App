package com.anish.collegeapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat notificationSwitch;
    private LinearLayout languageOptions;
    private ImageView englishCheck, nepaliCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        // Enable back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        notificationSwitch = findViewById(R.id.notificationSwitch);
        languageOptions = findViewById(R.id.languageOptions);
        englishCheck = findViewById(R.id.englishCheck);
        nepaliCheck = findViewById(R.id.nepaliCheck);

        // Load saved preferences
        loadPreferences();

        // Set up notification toggle
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationPreference(isChecked);
        });

        // Set up language dropdown
        ImageView languageDropdown = findViewById(R.id.languageDropdown);
        languageDropdown.setOnClickListener(v -> toggleLanguageOptions());

        // Set up language selection
        LinearLayout englishOption = findViewById(R.id.englishOption);
        LinearLayout nepaliOption = findViewById(R.id.nepaliOption);

        englishOption.setOnClickListener(v -> setLanguage("en"));
        nepaliOption.setOnClickListener(v -> setLanguage("ne"));
    }

    private void loadPreferences() {
        // Load notification preference
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean notificationsEnabled = preferences.getBoolean("notifications_enabled", true);
        notificationSwitch.setChecked(notificationsEnabled);

        // Load language preference
        String currentLanguage = preferences.getString("app_language", "en");
        updateLanguageCheckmarks(currentLanguage);
    }

    private void saveNotificationPreference(boolean isEnabled) {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        preferences.edit().putBoolean("notifications_enabled", isEnabled).apply();
    }

    private void toggleLanguageOptions() {
        if (languageOptions.getVisibility() == View.VISIBLE) {
            languageOptions.setVisibility(View.GONE);
        } else {
            languageOptions.setVisibility(View.VISIBLE);
        }
    }

    private void updateLanguageCheckmarks(String languageCode) {
        if (languageCode.equals("en")) {
            englishCheck.setVisibility(View.VISIBLE);
            nepaliCheck.setVisibility(View.GONE);
        } else {
            englishCheck.setVisibility(View.GONE);
            nepaliCheck.setVisibility(View.VISIBLE);
        }
    }

    private void setLanguage(String languageCode) {
        // Save language preference
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        preferences.edit().putString("app_language", languageCode).apply();

        // Update UI
        updateLanguageCheckmarks(languageCode);
        languageOptions.setVisibility(View.GONE);

        // Change app language
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Restart activity to apply changes
        recreate();
    }

    // Handle back arrow click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}