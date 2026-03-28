package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * Symptom Checker Activity for MedAI.
 * Allows users to select or type symptoms and receive an AI-powered prediction via LoadingActivity.
 */
public class SymptomCheckerActivity extends AppCompatActivity {

    private EditText etSymptoms;
    private MaterialButton btnAnalyze;
    private ImageButton btnBack;
    private ChipGroup chipGroupSymptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker);

        initUI();
    }

    private void initUI() {
        etSymptoms = findViewById(R.id.etSymptoms);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        btnBack = findViewById(R.id.btnBack);
        chipGroupSymptoms = findViewById(R.id.chipGroupSymptoms);

        // Standard back navigation
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Handle Chip Selection to populate EditText
        if (chipGroupSymptoms != null) {
            for (int i = 0; i < chipGroupSymptoms.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupSymptoms.getChildAt(i);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked && etSymptoms != null) {
                        String currentText = etSymptoms.getText().toString();
                        String chipText = buttonView.getText().toString();
                        if (currentText.isEmpty()) {
                            etSymptoms.setText(chipText);
                        } else if (!currentText.contains(chipText)) {
                            etSymptoms.setText(currentText + ", " + chipText);
                        }
                    }
                });
            }
        }

        // Handle Analyze Button Click
        if (btnAnalyze != null) {
            btnAnalyze.setOnClickListener(v -> {
                if (etSymptoms == null) return;

                String symptoms = etSymptoms.getText().toString().trim();

                // Validation: Check if input is empty
                if (symptoms.isEmpty()) {
                    Toast.makeText(SymptomCheckerActivity.this, "Enter symptoms", Toast.LENGTH_SHORT).show();
                } else {
                    // Open LoadingActivity and pass the symptom data
                    Intent intent = new Intent(SymptomCheckerActivity.this, LoadingActivity.class);
                    intent.putExtra("symptoms", symptoms);
                    startActivity(intent);
                }
            });
        }
    }
}