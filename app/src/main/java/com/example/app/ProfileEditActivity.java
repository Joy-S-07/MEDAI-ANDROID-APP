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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * ProfileEditActivity allows users to update their personal and health information.
 * Supports profile image selection (Camera/Gallery) and persistence.
 */
public class ProfileEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String PREFS_NAME = "MedAIPrefs";

    // Pref Keys
    private static final String KEY_NAME = "profile_name";
    private static final String KEY_EMAIL = "profile_email";
    private static final String KEY_PHONE = "profile_phone";
    private static final String KEY_AGE = "profile_age";
    private static final String KEY_BLOOD = "profile_blood";
    private static final String KEY_PROFILE_IMAGE = "profile_image_uri";
    private static final String KEY_PROFILE_BITMAP = "profile_image_bitmap";

    private TextInputEditText etName, etEmail, etPhone, etAge, etBloodGroup;
    private ImageView ivProfileEdit;
    private MaterialCardView cardEditImage;
    private MaterialButton btnSave;
    private ImageButton btnBack;
    
    private String selectedImageUri = null;
    private String selectedBitmapB64 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        initUI();
        loadExistingData();
    }

    private void initUI() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        ivProfileEdit = findViewById(R.id.ivProfileEdit);
        cardEditImage = findViewById(R.id.cardEditImage);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        cardEditImage.setOnClickListener(v -> showImagePickDialog());
        btnSave.setOnClickListener(v -> saveProfileData());
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Select Profile Image")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) checkCameraPermission();
                    else checkGalleryPermission();
                }).show();
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
            ? Manifest.permission.READ_MEDIA_IMAGES 
            : Manifest.permission.READ_EXTERNAL_STORAGE;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Logic to reopen dialog or directly open camera/gallery could be here
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception ignored) {}
                selectedImageUri = uri.toString();
                selectedBitmapB64 = null;
                ivProfileEdit.setImageURI(uri);
                ivProfileEdit.setColorFilter(null);
            } else if (requestCode == CAMERA_REQUEST) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                selectedBitmapB64 = ImageUtils.bitmapToBase64(bitmap);
                selectedImageUri = null;
                ivProfileEdit.setImageBitmap(bitmap);
                ivProfileEdit.setColorFilter(null);
            }
        }
    }

    private void loadExistingData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        etName.setText(prefs.getString(KEY_NAME, "Alex Johnson"));
        etEmail.setText(prefs.getString(KEY_EMAIL, "alex.johnson@example.com"));
        etPhone.setText(prefs.getString(KEY_PHONE, "+1 234 567 890"));
        etAge.setText(prefs.getString(KEY_AGE, "28"));
        etBloodGroup.setText(prefs.getString(KEY_BLOOD, "O+"));
        
        selectedImageUri = prefs.getString(KEY_PROFILE_IMAGE, null);
        selectedBitmapB64 = prefs.getString(KEY_PROFILE_BITMAP, null);

        if (selectedBitmapB64 != null) {
            ivProfileEdit.setImageBitmap(ImageUtils.base64ToBitmap(selectedBitmapB64));
            ivProfileEdit.setColorFilter(null);
        } else if (selectedImageUri != null) {
            ivProfileEdit.setImageURI(Uri.parse(selectedImageUri));
            ivProfileEdit.setColorFilter(null);
        }
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, etPhone.getText().toString().trim());
        editor.putString(KEY_AGE, etAge.getText().toString().trim());
        editor.putString(KEY_BLOOD, etBloodGroup.getText().toString().trim());
        
        if (selectedBitmapB64 != null) {
            editor.putString(KEY_PROFILE_BITMAP, selectedBitmapB64);
            editor.remove(KEY_PROFILE_IMAGE);
        } else if (selectedImageUri != null) {
            editor.putString(KEY_PROFILE_IMAGE, selectedImageUri);
            editor.remove(KEY_PROFILE_BITMAP);
        }
        
        editor.apply();
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}