package com.example.app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Dashboard for MedAI.
 * Features a 2-column grid of service cards and a personalized greeting.
 * Now supports profile image selection (Camera/Gallery) and persistence.
 */
public class DashboardActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String PREFS_NAME = "MedAIPrefs";
    private static final String KEY_PROFILE_IMAGE = "profile_image_uri";
    private static final String KEY_PROFILE_BITMAP = "profile_image_bitmap";
    private static final String KEY_NAME = "profile_name";

    private RecyclerView rvDashboard;
    private DashboardAdapter adapter;
    private MaterialCardView cardProfileIcon;
    private ImageView ivProfileImage;
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initUI();
        loadUserData();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void initUI() {
        cardProfileIcon = findViewById(R.id.cardProfileIcon);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);

        cardProfileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileEditActivity.class);
            startActivity(intent);
        });

        cardProfileIcon.setOnLongClickListener(v -> {
            showImagePickDialog();
            return true;
        });
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Select Profile Image")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermission();
                    } else {
                        checkGalleryPermission();
                    }
                })
                .show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void checkGalleryPermission() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            if (requestCode == PICK_IMAGE_REQUEST && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception ignored) {}
                editor.putString(KEY_PROFILE_IMAGE, imageUri.toString());
                editor.remove(KEY_PROFILE_BITMAP);
                editor.apply();
                updateProfileImageView(imageUri, null);
            } else if (requestCode == CAMERA_REQUEST) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String b64 = ImageUtils.bitmapToBase64(bitmap);
                editor.putString(KEY_PROFILE_BITMAP, b64);
                editor.remove(KEY_PROFILE_IMAGE);
                editor.apply();
                updateProfileImageView(null, bitmap);
            }
        }
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tvUserName.setText(prefs.getString(KEY_NAME, "Alex Johnson"));

        String uriString = prefs.getString(KEY_PROFILE_IMAGE, null);
        String bitmapString = prefs.getString(KEY_PROFILE_BITMAP, null);

        if (bitmapString != null) {
            updateProfileImageView(null, ImageUtils.base64ToBitmap(bitmapString));
        } else if (uriString != null) {
            updateProfileImageView(Uri.parse(uriString), null);
        } else {
            ivProfileImage.setImageResource(android.R.drawable.ic_menu_myplaces);
            ivProfileImage.setPadding(8, 8, 8, 8);
            ivProfileImage.setColorFilter(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void updateProfileImageView(Uri uri, Bitmap bitmap) {
        ivProfileImage.setPadding(0, 0, 0, 0);
        ivProfileImage.setColorFilter(null);
        if (bitmap != null) {
            ivProfileImage.setImageBitmap(bitmap);
        } else if (uri != null) {
            ivProfileImage.setImageURI(uri);
        }
    }

    private void setupRecyclerView() {
        rvDashboard = findViewById(R.id.rvDashboard);
        rvDashboard.setLayoutManager(new GridLayoutManager(this, 2));
        List<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardItem("Symptom Checker", android.R.drawable.ic_menu_search, SymptomCheckerActivity.class));
        items.add(new DashboardItem("Find Doctor", android.R.drawable.ic_menu_myplaces, FindDoctorActivity.class));
        items.add(new DashboardItem("Medical Support", android.R.drawable.ic_menu_help, MedicalSupportActivity.class));
        items.add(new DashboardItem("Health History", android.R.drawable.ic_menu_recent_history, HealthHistoryActivity.class));
        adapter = new DashboardAdapter(items, this);
        rvDashboard.setAdapter(adapter);
    }
}