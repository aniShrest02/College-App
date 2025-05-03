package com.anish.collegeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        LinearLayout introductionItem = view.findViewById(R.id.introduction_item);
        LinearLayout rulesItem = view.findViewById(R.id.rules_item);
        LinearLayout facilitiesItem = view.findViewById(R.id.facilities_item);
        LinearLayout achievementsItem = view.findViewById(R.id.achievements_item);
        LinearLayout settingsItem = view.findViewById(R.id.settings_item);
        LinearLayout loginItem = view.findViewById(R.id.login_item);

        // Set click listeners
        introductionItem.setOnClickListener(v -> navigateToActivity(IntroductionActivity.class));

        rulesItem.setOnClickListener(v -> navigateToActivity(RulesActivity.class));

        facilitiesItem.setOnClickListener(v -> navigateToActivity(FacilitiesActivity.class));

        achievementsItem.setOnClickListener(v -> navigateToActivity(AchievementsActivity.class));

        settingsItem.setOnClickListener(v -> navigateToActivity(SettingsActivity.class));

        loginItem.setOnClickListener(v -> showLogoutConfirmation());

        return view;
    }

    private void navigateToActivity(Class<?> activityClass) {
        startActivity(new Intent(getActivity(), activityClass));
    }

    private void showLogoutConfirmation() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    // Clear user session
                    SharedPreferences sharedPreferences = requireActivity()
                            .getSharedPreferences("user_prefs", requireContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("user_email");
                    editor.apply();

                    // Navigate to login screen
                    requireActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}