package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity for MedAI.
 * Features a premium dashboard with a symptom checker entry and a list of top doctors.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDoctors;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setupRecyclerView();
    }

    private void initUI() {
        MaterialButton btnCheckSymptoms = findViewById(R.id.btnCheckSymptoms);
        
        // Navigation to Symptom Checker
        btnCheckSymptoms.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SymptomCheckerActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        rvDoctors = findViewById(R.id.rvDoctors);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));

        // Fetching dummy data from placeholder function
        doctorList = getDoctors();
        doctorAdapter = new DoctorAdapter(doctorList);
        rvDoctors.setAdapter(doctorAdapter);
    }

    // --- PLACEHOLDER API FUNCTIONS ---

    /**
     * Placeholder for ML Prediction API.
     * In a real app, this would call a remote service or local TFLite model.
     */
    public String getPrediction(String symptoms) {
        return "Based on: " + symptoms + ". Simulated result: Possible seasonal allergies.";
    }

    /**
     * Placeholder for Doctors API.
     * Returns a static list of premium doctor profiles.
     */
    private List<Doctor> getDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Dr. Sarah Wilson", "Cardiologist", "⭐ 4.9 (120 reviews)"));
        doctors.add(new Doctor("Dr. James Miller", "Neurologist", "⭐ 4.8 (85 reviews)"));
        doctors.add(new Doctor("Dr. Elena Rodriguez", "Pediatrician", "⭐ 4.7 (210 reviews)"));
        doctors.add(new Doctor("Dr. Michael Chen", "Dermatologist", "⭐ 4.9 (150 reviews)"));
        return doctors;
    }

    /**
     * Placeholder for Medical Advice API.
     */
    public String getMedicalAdvice(String condition) {
        return "Advice for " + condition + ": Ensure adequate hydration and rest.";
    }
}