package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

/**
 * Result Activity for MedAI.
 * Displays the AI-generated diagnosis report and handles navigation to specialized doctors.
 */
public class ResultActivity extends AppCompatActivity {

    private TextView tvDisease, tvConfidence, tvSeverity, tvAdvice;
    private MaterialButton btnFindDoctor;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initUI();
        processDiagnosis();
    }

    private void initUI() {
        tvDisease = findViewById(R.id.tvDisease);
        tvConfidence = findViewById(R.id.tvConfidence);
        tvSeverity = findViewById(R.id.tvSeverity);
        tvAdvice = findViewById(R.id.tvAdvice);
        btnFindDoctor = findViewById(R.id.btnFindDoctor);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Navigate to FindDoctorActivity passing the predicted disease
        btnFindDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, FindDoctorActivity.class);
            String predictedDisease = tvDisease.getText().toString();
            intent.putExtra("disease", predictedDisease);
            startActivity(intent);
        });
    }

    private void processDiagnosis() {
        // 1. Get symptoms from Intent
        String symptoms = getIntent().getStringExtra("symptoms");
        if (symptoms == null) {
            symptoms = "";
        }

        // 2. Get structured prediction string
        String predictionData = getPrediction(symptoms);

        // 3. Split and assign to UI
        // Format: "Disease|Confidence|Severity|Recommendations"
        String[] parts = predictionData.split("\\|");
        
        if (parts.length >= 4) {
            tvDisease.setText(parts[0]);
            tvConfidence.setText(parts[1]);
            tvSeverity.setText(parts[2]);
            tvAdvice.setText(parts[3]);
            
            // Color coding severity text (Premium UI)
            if (parts[2].equalsIgnoreCase("High")) {
                tvSeverity.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else if (parts[2].equalsIgnoreCase("Moderate")) {
                tvSeverity.setTextColor(android.graphics.Color.parseColor("#F59E0B")); // Amber
            } else {
                tvSeverity.setTextColor(getResources().getColor(R.color.accent)); // Green
            }
        }
    }

    /**
     * Placeholder AI Logic.
     * Returns a structured string for the UI to parse.
     */
    private String getPrediction(String symptoms) {
        String lowerSymptoms = symptoms.toLowerCase();
        
        if (lowerSymptoms.contains("fever") || lowerSymptoms.contains("cough")) {
            return "Viral Infection|87%|Moderate|• Maintain hydration\n• Adequate bed rest\n• Monitor body temperature\n• Consult a doctor if symptoms worsen";
        } else if (lowerSymptoms.contains("headache") || lowerSymptoms.contains("migraine")) {
            return "Tension Headache|94%|Low|• Rest in a quiet, dark room\n• Stay hydrated\n• Apply cool compress\n• Manage stress levels";
        } else {
            return "Common Cold|75%|Low|• Drink plenty of fluids\n• Get extra sleep\n• Use saline nasal drops\n• Salt water gargles";
        }
    }
}