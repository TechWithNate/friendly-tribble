package com.nate.royalquest.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.royalquest.R;

public class Results extends AppCompatActivity {

    private ImageView playerImage;
    private RelativeLayout relativeLayout;
    private TextView winStatus;
    private TextView scores;
    private TextView playerLevel;
    private TextView playerName;
    private TextView winningXP;
    private MaterialButton replayBtn;
    int score;
    private String courseID;
    private String courseTitle;
    private int points;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int level = snapshot.child("playerLevel").getValue(Integer.class);
                String name = snapshot.child("name").getValue(String.class);
                String image = snapshot.child("profileImg").getValue(String.class);
                playerLevel.setText("Level "+level);
                playerName.setText(name);
                Glide.with(Results.this).load(image).into(playerImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        score = getIntent().getIntExtra("score", 0);
        courseID = getIntent().getStringExtra("courseId");
        courseTitle = getIntent().getStringExtra("courseName");
        scores.setText(score+" out of 5");
        setScore();

        replayBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Results.this, Quiz.class);
            intent.putExtra("courseId", courseID);
            intent.putExtra("courseName", courseTitle);
            startActivity(intent);
            finish();
        });


    }

    private void initViews(){
        playerImage = findViewById(R.id.player_image);

        relativeLayout = findViewById(R.id.relative_layout);
        winStatus = findViewById(R.id.win_status);
        playerImage = findViewById(R.id.player_image);
        playerLevel = findViewById(R.id.player_level);
        playerName = findViewById(R.id.player_name);
        scores = findViewById(R.id.scores);
        replayBtn = findViewById(R.id.rematch_btn);
        winningXP = findViewById(R.id.winningXP);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("royals").child(firebaseAuth.getUid());
    }


    @SuppressLint("ResourceAsColor")
    private void setScore() {
        if (score > 3) {
            points = score * 5;
            winStatus.setText("You Won");
            winningXP.setText(points +" Royal Points");
            updatePoints(points);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        } else if (score == 3) {
            points = score * 3;
            winStatus.setText("You Won");
            winningXP.setText(points +" Royal Points");
            updatePoints(points);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        } else if (score == 2){
            points = score * 2;
            winStatus.setText("You Lose");
            winningXP.setText(points +" Royal Points");
            updatePoints(points);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }else if (score == 1){
            points = score;
            winStatus.setText("You Lose");
            winningXP.setText(points +" Royal Points");
            updatePoints(points);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }else {
            points = 0;
            winStatus.setText("You Lose");
            winningXP.setText(points +" Royal Points");
            updatePoints(points);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }
    }

    private void updatePoints(int points){

        databaseReference.child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int currentPoints = snapshot.getValue(Integer.class);
                int newPoints = currentPoints + points;
                databaseReference.child("score").setValue(newPoints);
                updatePlayerLevel(newPoints);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Results.this, "There is a db error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updatePlayerLevel(int points) {
        databaseReference.child("playerLevel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int currentLevel = snapshot.getValue(Integer.class);

                int nextLevel = currentLevel + 1; // The next level to achieve
                int pointsRequiredForNextLevel = calculatePointsForLevel(nextLevel);

                // Check if the player has enough points to level up
                if (points >= pointsRequiredForNextLevel) {
                    // Update the player's level in the database
                    databaseReference.child("playerLevel").setValue(nextLevel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Results.this, "There is a db error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method calculates the points required to reach a given level.
     * The increment starts at 100 points for level 2, then increases by 50 points for each subsequent level.
     *
     * @param level The level for which to calculate the points requirement.
     * @return The points required to reach the specified level.
     */
    private int calculatePointsForLevel(int level) {
        int pointsRequired = 0;
        int increment = 100;

        for (int i = 2; i <= level; i++) {
            pointsRequired += increment;
            increment += 50; // Increase the increment by 50 for each new level
        }

        return pointsRequired;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Results.this, Home.class));
        finish();
    }
}