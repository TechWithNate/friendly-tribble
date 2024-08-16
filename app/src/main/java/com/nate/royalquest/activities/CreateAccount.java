package com.nate.royalquest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.nate.royalquest.R;

public class CreateAccount extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private MaterialButton signUpBtn;
    private ProgressBar progressBar;
    private TextView login;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        signUpBtn.setOnClickListener(v -> checkFields());
        login.setOnClickListener(v -> openLogin());

    }


    private void initViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signUpBtn = findViewById(R.id.sign_up_btn);
        progressBar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void checkFields(){
        if (email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else if (password.getText().length() <10){
            Toast.makeText(this, "Password must be at least 10 characters long", Toast.LENGTH_SHORT).show();
        }else if (!confirmPassword.getText().toString().equals(password.getText().toString())){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }else{
            createAccount(email.getText().toString(), password.getText().toString());
        }
    }

    private void createAccount(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()){
                openCreateProfile();
                Toast.makeText(CreateAccount.this, "Account created successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CreateAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openCreateProfile() {
        startActivity(new Intent(CreateAccount.this, CreateProfile.class));
        finish();
    }

    private void openLogin(){
        startActivity(new Intent(CreateAccount.this, Login.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            openHomeActivity();
        }
    }

    private void openHomeActivity() {
        Intent intent = new Intent(CreateAccount.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}