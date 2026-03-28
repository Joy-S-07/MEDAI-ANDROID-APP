package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * MedicalSupportActivity provides AI-powered medical guidance and support.
 * Features a disclaimer card and a result area for simulated medical advice.
 */
public class MedicalSupportActivity extends AppCompatActivity {

    private TextInputEditText etCondition;
    private MaterialButton btnGetSupport;
    private MaterialCardView cardAdviceResult;
    private TextView tvAdvice;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_support);

        initUI();
    }

    private void initUI() {
        etCondition = findViewById(R.id.etCondition);
        btnGetSupport = findViewById(R.id.btnGetSupport);
        cardAdviceResult = findViewById(R.id.cardAdviceResult);
        tvAdvice = findViewById(R.id.tvAdvice);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnGetSupport.setOnClickListener(v -> {
            String condition = etCondition.getText().toString().trim();
            if (condition.isEmpty()) {
                Toast.makeText(this, "Please enter your medical concern", Toast.LENGTH_SHORT).show();
            } else {
                displayAdvice(condition);
            }
        });
    }

    /**
     * Updates the UI with the retrieved advice.
     */
    private void displayAdvice(String condition) {
        String advice = getMedicalAdvice(condition);
        tvAdvice.setText(advice);
        cardAdviceResult.setVisibility(View.VISIBLE);
        
        // Hide keyboard or focus could be handled here for better UX
        etCondition.clearFocus();
    }

    // --- PLACEHOLDER API FUNCTION ---

    /**
     * Placeholder for Medical Advice API.
     * In a production app, this would call a backend service or a local knowledge base.
     */
    private String getMedicalAdvice(String condition) {
        // Structured dummy data for better UX
        return "Standard guidance for '" + condition + "':\n\n" +
               "• Monitor your symptoms closely for any changes.\n" +
               "• Ensure you are getting adequate rest and avoiding strenuous activity.\n" +
               "• Stay hydrated by drinking at least 8 glasses of water a day.\n" +
               "• Keep a record of your temperature and any new symptoms.\n\n" +
               "If symptoms persist or worsen, please consult a healthcare professional.";
    }
}