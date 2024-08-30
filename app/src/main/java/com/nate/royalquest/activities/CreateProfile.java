package com.nate.royalquest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nate.royalquest.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    private ImageView profileImage;
    private EditText etFirstname;
    private EditText etLastname;
    private MaterialButton createProfileBtn;
    private ProgressBar progressBar;
    private AutoCompleteTextView genderAutoComplete;
    private AutoCompleteTextView levelAutoComplete;
    private String gender;
    private String level;
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE = 100;
    private String imageUriAccessToken;


    private static final String PREFS_NAME = "UserPrefs"; // SharedPreferences file name
    private static final String KEY_LEVEL = "userLevel";  // Key to store user level


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderOptions);
        genderAutoComplete.setAdapter(adapter);
        genderAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            gender = (String) parent.getItemAtPosition(position);
        });

        String[] levelOptions = {"Primary", "JHS"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, levelOptions);
        levelAutoComplete.setAdapter(adapter1);
        levelAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            level = (String) parent.getItemAtPosition(position);
        });

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });


        createProfileBtn.setOnClickListener(v -> {
            checkFields();
        });

    }

    private void initViews(){
        profileImage = findViewById(R.id.user_img);
        etFirstname = findViewById(R.id.firstname);
        etLastname = findViewById(R.id.lastname);
        genderAutoComplete = findViewById(R.id.gender);
        levelAutoComplete = findViewById(R.id.level);
        createProfileBtn = findViewById(R.id.create_profile_btn);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("royals");
        storageReference = FirebaseStorage.getInstance().getReference("Images");
    }

    private void checkFields(){
        if (imageUri == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        }else if (etFirstname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter firstname", Toast.LENGTH_SHORT).show();
        } else if (etLastname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter lastname", Toast.LENGTH_SHORT).show();
        }else if (genderAutoComplete.getText().toString().isEmpty()){
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
        } else if (levelAutoComplete.getText().toString().isEmpty()) {
            Toast.makeText(this, "Select Class", Toast.LENGTH_SHORT).show();
        }else {
            createProfile();
        }
    }

    private void saveUserLevel() {
        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the selected level in SharedPreferences
        editor.putString(KEY_LEVEL, level);
        editor.apply(); // Save changes asynchronously
    }


    private void createProfile(){
        progressBar.setVisibility(View.VISIBLE);
        saveUserLevel();
        sendDataForNewUser();
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(CreateProfile.this, Home.class));
        finish();
    }

    private void sendDataForNewUser() {
        if (firebaseUser != null) {
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("userUID", firebaseUser.getUid());
            userProfile.put("firstname", etFirstname.getText().toString());
            userProfile.put("lastname", etLastname.getText().toString());
            userProfile.put("gender", gender);
            userProfile.put("level", level);
            userProfile.put("score", 10);
            userProfile.put("playerLevel", 1);

            // Upload the image first
            sendImageToStorage(userProfile);
        }
    }

    private void sendImageToStorage(Map<String, Object> userProfile) {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child(firebaseUser.getUid()).child("Profile Pic");

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                Toast.makeText(CreateProfile.this, "Image load failed", Toast.LENGTH_SHORT).show();
                return;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot ->
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUriAccessToken = uri.toString();
                        userProfile.put("profileImg", imageUriAccessToken);
                        // Now update the database with user profile data including image URI
                        updateUserProfile(userProfile);
                    }).addOnFailureListener(e ->
                            Toast.makeText(CreateProfile.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()));
        } else {
            // If there's no image, just update the profile
            updateUserProfile(userProfile);
        }
    }


    private void updateUserProfile(Map<String, Object> userProfile) {
        databaseReference.child(firebaseUser.getUid())
                .updateChildren(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateProfile.this, "Profile Created Successfully", Toast.LENGTH_SHORT).show();
                        openHomeActivity();
                    } else {
                        Toast.makeText(CreateProfile.this, "Profile creation failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void openHomeActivity() {
        Intent intent = new Intent(CreateProfile.this, Home.class);
        startActivity(intent);
        finish();
    }

}