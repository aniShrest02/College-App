package com.anish.collegeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private final Calendar currentCalendar = Calendar.getInstance();
    private final HashSet<String> holidays = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize holiday dates (format: "MM-dd")
        initializeHolidays();

        // Initialize views
        monthYearText = view.findViewById(R.id.tv_month_year);
        calendarRecyclerView = view.findViewById(R.id.calendar_grid);
        ImageButton previousButton = view.findViewById(R.id.btn_previous);
        ImageButton nextButton = view.findViewById(R.id.btn_next);

        // Set up calendar
        setupCalendar();

        // Set up button listeners
        previousButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            setupCalendar();
        });

        nextButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            setupCalendar();
        });

        return view;
    }

    private void initializeHolidays() {
        // Add your holidays here (format: "MM-dd")
        holidays.addAll(Arrays.asList(
                "01-01", // New Year's Day
                "01-26", // Republic Day (India)
                "03-25", // Holi (example)
                "08-15", // Independence Day (India)
                "10-02", // Gandhi Jayanti
                "12-25"  // Christmas
        ));
    }

    private void setupCalendar() {
        // Update month/year display
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(sdf.format(currentCalendar.getTime()));

        // Get days in month
        List<Date> daysInMonth = getDaysInMonth(currentCalendar);

        // Set up RecyclerView
        CalendarAdapter adapter = new CalendarAdapter(daysInMonth);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        calendarRecyclerView.setAdapter(adapter);
    }

    private List<Date> getDaysInMonth(Calendar calendar) {
        List<Date> days = new ArrayList<>();

        // Clone calendar to avoid modifying the original
        Calendar cal = (Calendar) calendar.clone();

        // Set to first day of month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of week for the first day (Sunday = 1, Saturday = 7)
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // Get days in month
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty cells for days before the first day of month
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add(null);
        }

        // Add all days of the month
        for (int i = 1; i <= daysInMonth; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i);
            days.add(cal.getTime());
        }

        return days;
    }

    private boolean isHoliday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return holidays.contains(sdf.format(date));
    }

    private class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

        private final List<Date> days;

        CalendarAdapter(List<Date> days) {
            this.days = days;
        }

        @NonNull
        @Override
        public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_calendar_day, parent, false);
            return new CalendarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
            Date date = days.get(position);

            if (date == null) {
                holder.dayText.setText("");
                holder.dayText.setBackground(null);
                holder.holidayIndicator.setVisibility(View.GONE);
                holder.itemView.setClickable(false);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                holder.dayText.setText(String.valueOf(day));

                // Check if today
                Calendar today = Calendar.getInstance();
                boolean isToday = cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                        cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);

                // Check if holiday
                boolean isHoliday = isHoliday(date);

                // Highlight today
                if (isToday) {
                    holder.dayText.setBackgroundResource(R.drawable.circle_background);
                    holder.dayText.setTextColor(Color.WHITE);
                } else {
                    holder.dayText.setBackground(null);
                    // Sunday or Saturday (weekend)
                    if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                            cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        holder.dayText.setTextColor(Color.RED);
                    } else {
                        holder.dayText.setTextColor(Color.BLACK);
                    }
                }

                // Show holiday indicator
                if (isHoliday) {
                    holder.holidayIndicator.setVisibility(View.VISIBLE);
                    holder.dayText.setTextColor(Color.RED);
                } else {
                    holder.holidayIndicator.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(v -> {
                    // Handle day click - you could show events or details here
                });
            }
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        class CalendarViewHolder extends RecyclerView.ViewHolder {
            TextView dayText;
            View holidayIndicator;

            CalendarViewHolder(@NonNull View itemView) {
                super(itemView);
                dayText = itemView.findViewById(R.id.day_text);
                holidayIndicator = itemView.findViewById(R.id.holiday_indicator);
            }
        }
    }
}