package com.example.app;

/**
 * Model class for a Doctor profile.
 * Updated to support premium UI features.
 */
public class Doctor {
    private String name;
    private String specialty;
    private String rating;
    private String distance;
    private int imageRes;

    // Primary constructor for FindDoctorActivity
    public Doctor(String name, String specialty, String rating, String distance, int imageRes) {
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
        this.distance = distance;
        this.imageRes = imageRes;
    }

    // Overloaded constructor for backward compatibility (e.g. MainActivity)
    public Doctor(String name, String specialty, String rating) {
        this(name, specialty, rating, "0.5 km", android.R.drawable.ic_menu_myplaces);
    }

    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getRating() { return rating; }
    public String getDistance() { return distance; }
    public int getImageRes() { return imageRes; }
}