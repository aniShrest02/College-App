package com.anish.collegeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class NoticeFragment extends Fragment {

    private LinearLayout eventsContainer;
    private TextView tvEmptyEvents;
    private List<Event> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        eventsContainer = view.findViewById(R.id.eventsContainer);
        tvEmptyEvents = view.findViewById(R.id.tvEmptyEvents);

        // Initialize event list with sample data
        eventList = new ArrayList<>();
        eventList.add(new Event(
                "Annual Sports Day",
                "Fri, May 30 • 9:00 AM",
                "School Ground",
                R.drawable.sports));

        eventList.add(new Event(
                "HACKATHON",
                "Mon, May 23 • 10:00 AM",
                "Room 102",
                R.drawable.hackathon));

        // Add more events as needed

        // Populate events
        populateEvents();

        return view;
    }

    private void populateEvents() {
        eventsContainer.removeAllViews();

        if (eventList.isEmpty()) {
            tvEmptyEvents.setVisibility(View.VISIBLE);
            return;
        }

        tvEmptyEvents.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Event event : eventList) {
            View eventView = inflater.inflate(R.layout.item_event, eventsContainer, false);

            ImageView eventImage = eventView.findViewById(R.id.eventImage);
            TextView eventTitle = eventView.findViewById(R.id.eventTitle);
            TextView eventDate = eventView.findViewById(R.id.eventDate);
            TextView eventLocation = eventView.findViewById(R.id.eventLocation);

            eventImage.setImageResource(event.getImageRes());
            eventTitle.setText(event.getTitle());
            eventDate.setText(event.getDate());
            eventLocation.setText(event.getLocation());

            eventView.setOnClickListener(v -> {
                showEventDetails(event);
            });

            eventsContainer.addView(eventView);
        }
    }

    private void showEventDetails(Event event) {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle(event.getTitle())
                .setMessage(event.getDate() + "\nLocation: " + event.getLocation())
                .setPositiveButton("OK", null)
                .show();
    }

    // Event model class
    private static class Event {
        private String title;
        private String date;
        private String location;
        private int imageRes;

        public Event(String title, String date, String location, int imageRes) {
            this.title = title;
            this.date = date;
            this.location = location;
            this.imageRes = imageRes;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getLocation() {
            return location;
        }

        public int getImageRes() {
            return imageRes;
        }
    }
}