package com.example.app;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

/**
 * FindDoctorActivity allows users to search for doctors and view their profiles.
 * Updated with premium card design support.
 */
public class FindDoctorActivity extends AppCompatActivity {

    private RecyclerView rvDoctors;
    private DoctorAdapter adapter;
    private List<Doctor> doctorList;
    private TextInputEditText etSearch;
    private MaterialButton btnSearch;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        initUI();
        setupRecyclerView();
    }

    private void initUI() {
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        rvDoctors = findViewById(R.id.rvDoctors);

        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        
        doctorList = getDoctors();
        adapter = new DoctorAdapter(doctorList);
        rvDoctors.setAdapter(adapter);
    }

    /**
     * Placeholder for Doctors API.
     * Updated with distance and image placeholder support.
     */
    private List<Doctor> getDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Dr. Sarah Wilson", "Cardiologist", "⭐ 4.9", "1.2 km", android.R.drawable.ic_menu_myplaces));
        doctors.add(new Doctor("Dr. James Miller", "Neurologist", "⭐ 4.8", "2.5 km", android.R.drawable.ic_menu_myplaces));
        doctors.add(new Doctor("Dr. Elena Rodriguez", "Pediatrician", "⭐ 4.7", "0.8 km", android.R.drawable.ic_menu_myplaces));
        doctors.add(new Doctor("Dr. Michael Chen", "Dermatologist", "⭐ 4.9", "3.1 km", android.R.drawable.ic_menu_myplaces));
        doctors.add(new Doctor("Dr. Sophia Garcia", "Oncologist", "⭐ 4.8", "4.0 km", android.R.drawable.ic_menu_myplaces));
        doctors.add(new Doctor("Dr. David Lee", "Orthopedic", "⭐ 4.6", "1.5 km", android.R.drawable.ic_menu_myplaces));
        return doctors;
    }
}