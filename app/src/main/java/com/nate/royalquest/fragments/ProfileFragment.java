package com.nate.royalquest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nate.royalquest.R;
import com.nate.royalquest.activities.CreateProfile;
import com.nate.royalquest.activities.Login;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;
    private ImageView profileImage;
    private TextView tvUsername;
    private TextView tvScore;
    private TextView tvLvl;
    private EditText etFirstname;
    private EditText etLastname;
    private AutoCompleteTextView genderAutoComplete;
    private TextView levelAutoComplete;
    private MaterialButton editProfileBtn;
    private MaterialButton logoutBtn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private String gender;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        initViews();

        fetchUserProfile();
        editProfileBtn.setOnClickListener(v -> {
            ///startActivity(new Intent(getContext(), EditProfile.class));
        });

        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, genderOptions);
        genderAutoComplete.setAdapter(genderAdapter);
        genderAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            gender = (String) parent.getItemAtPosition(position);
        });

        logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(getContext(), Login.class));
            getActivity().finish();
        });

        return view;
    }

    private void initViews(){
        profileImage = view.findViewById(R.id.profile_img);
        tvUsername = view.findViewById(R.id.tv_username);
        tvScore = view.findViewById(R.id.tv_scores);
        tvLvl = view.findViewById(R.id.tv_lvl);
        etFirstname = view.findViewById(R.id.firstname);
        etLastname = view.findViewById(R.id.lastname);
        levelAutoComplete = view.findViewById(R.id.level);
        genderAutoComplete = view.findViewById(R.id.gender);
        editProfileBtn = view.findViewById(R.id.edit_profile_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);

        // In your onCreate or a similar method
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("royals");
        storageReference = FirebaseStorage.getInstance().getReference("Images").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Profile Pic");

    }


    private void fetchUserProfile() {
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Extract user profile data
                        //String username = snapshot.child("username").getValue(String.class);
                        int score = snapshot.child("score").getValue(Integer.class);
                        String firstname = snapshot.child("firstname").getValue(String.class);
                        String lastname = snapshot.child("lastname").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String level = snapshot.child("level").getValue(String.class);
                        String imageUri = snapshot.child("profileImg").getValue(String.class);
                        int playerLevel = snapshot.child("playerLevel").getValue(Integer.class);

                        // Populate UI components
                        tvUsername.setText(firstname + " " + lastname);
                        tvScore.setText(score +" Royal Points");
                        tvLvl.setText("Lvl "+ playerLevel);
                        genderAutoComplete.setText(gender);
                        etFirstname.setText(firstname);
                        etLastname.setText(lastname);
                        levelAutoComplete.setText(level);
                        if (imageUri != null && !imageUri.isEmpty()) {
                            loadImageFromUri(imageUri);
                        }
                    } else {
                        Toast.makeText(getContext(), "Profile does not exist", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), CreateProfile.class));
                        getActivity().finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to fetch profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadImageFromUri(String imageUri) {
        Glide.with(this)
                .load(imageUri)
                .into(profileImage);

    }



}
