package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display a list of health history items within a specific category.
 */
public class HistoryFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private String category;

    public static HistoryFragment newInstance(String category) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        HistoryAdapter adapter = new HistoryAdapter(getDummyData(category));
        recyclerView.setAdapter(adapter);
        
        return view;
    }

    private List<HistoryItem> getDummyData(String category) {
        List<HistoryItem> list = new ArrayList<>();
        if ("Consultations".equals(category)) {
            list.add(new HistoryItem("General Checkup", "12 Oct", "Dr. Sarah Wilson", android.R.drawable.ic_menu_today));
            list.add(new HistoryItem("Heart Screening", "05 Sep", "Dr. James Miller", android.R.drawable.ic_menu_today));
            list.add(new HistoryItem("Dental Care", "20 Aug", "Dr. Michael Chen", android.R.drawable.ic_menu_today));
        } else if ("Prescriptions".equals(category)) {
            list.add(new HistoryItem("Amoxicillin", "12 Oct", "500mg - 2 times a day", android.R.drawable.ic_menu_edit));
            list.add(new HistoryItem("Vitamin D3", "01 Oct", "1000 IU - Daily", android.R.drawable.ic_menu_edit));
        } else {
            list.add(new HistoryItem("Blood Test", "10 Oct", "Central Lab Services", android.R.drawable.ic_menu_view));
            list.add(new HistoryItem("X-Ray Chest", "15 Sep", "City Hospital", android.R.drawable.ic_menu_view));
        }
        return list;
    }
}