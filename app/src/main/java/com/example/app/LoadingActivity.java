package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;

/**
 * Loading Activity to simulate AI processing.
 * Displays a progress indicator for 2 seconds before showing the result.
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // 1. Get symptoms from Intent safely
        String symptoms = getIntent().getStringExtra("symptoms");
        if (symptoms == null) {
            symptoms = "None provided"; 
        }

        initUI();

        // final variable for use in the lambda
        final String symptomsToPass = symptoms;

        // 2. Wait for 2 seconds (2000ms)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // 3. Open ResultActivity
            Intent intent = new Intent(LoadingActivity.this, ResultActivity.class);
            
            // 4. Pass symptoms again
            intent.putExtra("symptoms", symptomsToPass);
            
            startActivity(intent);
            finish(); // Remove from back stack
        }, 2000);
    }

    private void initUI() {
        TextView tvLoading = findViewById(R.id.tvLoadingText);
        CircularProgressIndicator progress = findViewById(R.id.progressIndicator);

        // Ensure the text is exactly what was requested
        if (tvLoading != null) {
            tvLoading.setText("Analyzing symptoms...");
        }

        // Apply smooth fade-in for a premium feel
        if (tvLoading != null && progress != null) {
            tvLoading.setAlpha(0f);
            progress.setAlpha(0f);
            
            tvLoading.animate().alpha(1f).setDuration(300).start();
            progress.animate().alpha(1f).setDuration(300).start();
        }
    }
}