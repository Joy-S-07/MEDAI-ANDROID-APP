package com.example.app;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * HealthHistoryActivity displays the user's past medical data.
 * Uses a ViewPager2 + TabLayout to organize Consultations, Prescriptions, and Reports.
 */
public class HealthHistoryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_history);

        initUI();
    }

    private void initUI() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Setup Adapter
        viewPager.setAdapter(new HistoryPagerAdapter(this));

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Consultations"); break;
                case 1: tab.setText("Prescriptions"); break;
                case 2: tab.setText("Reports"); break;
            }
        }).attach();
    }

    /**
     * Adapter for handling fragments in the ViewPager2.
     */
    private static class HistoryPagerAdapter extends FragmentStateAdapter {
        public HistoryPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return HistoryFragment.newInstance("Consultations");
                case 1: return HistoryFragment.newInstance("Prescriptions");
                case 2: return HistoryFragment.newInstance("Reports");
                default: return HistoryFragment.newInstance("Consultations");
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}