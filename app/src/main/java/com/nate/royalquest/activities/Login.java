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

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private MaterialButton login;
    private TextView signUp;
    private ProgressBar progressBar;
//    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//    private String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
//    private String confirmPasswordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
//    private String namePattern = "^[a-zA-Z ]+$";
//    private String phoneNumberPattern = "^[0-9]{10}$";
//    private String addressPattern = "^[a-zA-Z0-9 ]+$";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        login.setOnClickListener(v -> checkFields());

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateAccount.class));
            finish();
        });

    }

    private void initViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void checkFields(){
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }else{
            loginUser(email.getText().toString(), password.getText().toString());
        }
    }

    private void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                openHomePage();
            }else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openHomePage() {
        startActivity(new Intent(this, Home.class));
        finish();

    }

}