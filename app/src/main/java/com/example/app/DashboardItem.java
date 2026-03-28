package com.example.app;

public class DashboardItem {
    private String title;
    private int iconRes;
    private Class<?> targetActivity;

    public DashboardItem(String title, int iconRes, Class<?> targetActivity) {
        this.title = title;
        this.iconRes = iconRes;
        this.targetActivity = targetActivity;
    }

    public String getTitle() {
        return title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }
}