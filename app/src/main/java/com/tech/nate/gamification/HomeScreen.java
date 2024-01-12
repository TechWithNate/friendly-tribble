package com.tech.nate.gamification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;

public class HomeScreen extends AppCompatActivity {

    private MaterialCardView primCard;
    private MaterialCardView jhsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initViews();

        primCard.setOnClickListener(v -> {
            //TODO: open primary section
        });

        jhsCard.setOnClickListener(v -> {
            //TODO: open JHS section
        });

    }

    private void initViews(){
        primCard = findViewById(R.id.prim_layout);
        jhsCard = findViewById(R.id.jhs_layout);
    }

}