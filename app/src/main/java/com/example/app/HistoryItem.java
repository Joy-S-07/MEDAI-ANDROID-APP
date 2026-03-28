package com.example.app;

/**
 * Model class for Health History items.
 */
public class HistoryItem {
    private String title;
    private String date;
    private String subtitle;
    private int iconRes;

    public HistoryItem(String title, String date, String subtitle, int iconRes) {
        this.title = title;
        this.date = date;
        this.subtitle = subtitle;
        this.iconRes = iconRes;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getSubtitle() { return subtitle; }
    public int getIconRes() { return iconRes; }
}