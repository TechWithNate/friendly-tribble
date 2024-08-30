package com.nate.royalquest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nate.royalquest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz extends AppCompatActivity {

    private LinearProgressIndicator progress;
    private CountDownTimer countDownTimer;
    private MaterialToolbar topAppBar;
    private ImageView playerImage, opponentImage;
    private TextView playerName, opponentName,
            playerLevel, opponentLevel;

    private TextView question;
    RadioButton radioButtonA;
    RadioButton radioButtonB;
    RadioButton radioButtonC;
    RadioButton radioButtonD;
    private RadioGroup radioGroup;
    private MaterialButton submitBtn;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;

    private DatabaseReference databaseReference;
    private int totalQuestions= 13;
    private int questionsToAnswer = 5;
    String oppLevel;
    String oppName;
    String courseTitle;
    String courseID;
    String oppProfileImage;
    String playerImageToken;
    TextView playerScore;
    private String opponentId;

    int score = 0; // Add a variable to keep track of the user's score
    boolean answered = false; // Add a variable to check if the user has already answered
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    String correctAnswer = "";
    String quizKey;
    DatabaseReference quizzesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        Intent intent = getIntent();
        if (null != intent){
            courseID = intent.getStringExtra("courseId");
            courseTitle = intent.getStringExtra("courseName");
        }else{
            Toast.makeText(this, "Course Id and title null", Toast.LENGTH_SHORT).show();
        }

//        setupToolbar();
//        setupFirebaseReferences();
//
//        // Fetch initial set of questions
//        fetchQuestions();
//
//        // Set listener for the submit button
//        submitBtn.setOnClickListener(v -> checkRadioButton());

        topAppBar.setTitle(courseTitle);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Quiz.this, Home.class));
                finish();
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Subjects");


//        quizzesRef = FirebaseDatabase.getInstance().getReference("Users")
//                .child(firebaseAuth.getUid()).child("quizzes");
//
//        quizKey = quizzesRef.push().getKey(); // Generate a unique key for the quiz
//
//        // Prepare the quiz data
//        Map<String, Object> quizData = new HashMap<>();
//        quizData.put("courseName", "Course 1"); // Set the course name as needed
//        quizData.put("opponentId", opponentId); // Set the opponent's ID as needed
//        // Add questions and answers to the quizData map as needed
//
//        // Set the quiz data under the generated key
//        quizzesRef.child(quizKey).setValue(quizData);

        // Fetch the initial set of questions
        fetchQuestions();
        submitBtn.setOnClickListener(v -> checkRadioButton());
        radioGroup.clearCheck();


    }


//    // Setup the toolbar with the course title
//    private void setupToolbar() {
//        topAppBar.setTitle(courseTitle);
//        topAppBar.setNavigationOnClickListener(v -> {
//            startActivity(new Intent(Quiz.this, Home.class));
//            finish();
//        });
//    }
//
//    // Initialize Firebase references
//    private void setupFirebaseReferences() {
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseStorage = FirebaseStorage.getInstance();
//
//        databaseReference = firebaseDatabase.getReference("Subjects");
//        quizzesRef = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("quizzes");
//        quizKey = quizzesRef.push().getKey(); // Generate a unique key for the quiz
//    }
//
//    // Handle question data from Firebase
//    private void handleQuestionData(DataSnapshot dataSnapshot, int index) {
//        String questionText = dataSnapshot.child("text").getValue(String.class);
//        correctAnswer = dataSnapshot.child("correctChoice").getValue(String.class);
//        Map<String, String> choices = new HashMap<>();
//        for (DataSnapshot choiceSnapshot : dataSnapshot.child("choices").getChildren()) {
//            choices.put(choiceSnapshot.getKey(), choiceSnapshot.getValue(String.class));
//        }
//
//        // Save the question to Firebase (optional)
//        Map<String, Object> quiz = new HashMap<>();
//        quiz.put("text", questionText);
//        quiz.put("choices", choices);
//        quiz.put("correctChoice", correctAnswer);
//        quizzesRef.child(quizKey).child("question" + (index + 1)).setValue(quiz);
//
//        // Display question and choices
//        displayQuestion(questionText, choices);
//    }

    // Fetch questions from Firebase
//    private void fetchQuestions() {
//        if (currentQuestionIndex >= questionsToAnswer) {
//            finishQuiz();
//            return;
//        }
//
//        List<Integer> randomIndices = generateRandomIndices(totalQuestions, 5);
//        for (int i = 0; i < randomIndices.size(); i++) {
//            int randomIndex = randomIndices.get(i);
//            int finalI = i;
//            databaseReference.child(courseID).child(courseTitle)
//                    .child("question" + (randomIndex + 1))
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            handleQuestionData(dataSnapshot, finalI);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Toast.makeText(Quiz.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }


    private void initViews(){
        topAppBar = findViewById(R.id.topAppBar);


        question = findViewById(R.id.question);
        radioButtonA = findViewById(R.id.answer1);
        radioButtonB = findViewById(R.id.answer2);
        radioButtonC = findViewById(R.id.answer3);
        radioButtonD = findViewById(R.id.answer4);
        submitBtn = findViewById(R.id.submit_btn);
        radioGroup = findViewById(R.id.radio_group);
        playerScore = findViewById(R.id.player_score);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }


    private void fetchQuestions() {


        radioGroup.clearCheck();
        // Assume you have a DatabaseReference reference to your Firebase database

        // Generate five random indices (questions) within the range
        List<Integer> randomIndices = generateRandomIndices(totalQuestions, 5);

        if(currentQuestionIndex == questionsToAnswer){
            finishQuiz();
            return;
        }



        for (int i = 0; i < 5; i++) {
            int randomIndex = randomIndices.get(i);

            // Query Firebase to get the question and its choices
            int finalI = i;
            databaseReference.child(courseID).child(courseTitle)
                    .child("question" + (randomIndex + 1)) // Firebase indices start at 1
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Parse the question and choices from dataSnapshot
                            String questionText = dataSnapshot.child("text").getValue(String.class);
                            correctAnswer = dataSnapshot.child("correctChoice").getValue(String.class);
                            Map<String, String> choices = new HashMap<>();
                            for (DataSnapshot choiceSnapshot : dataSnapshot.child("choices").getChildren()) {
                                String choiceKey = choiceSnapshot.getKey();
                                String choiceValue = choiceSnapshot.getValue(String.class);
                                choices.put(choiceKey, choiceValue);
                            }

                            // Now you have the question and its choices, you can display them
//                            // and set up radio buttons for the choices
//                            Map<String, Object> quiz = new HashMap<>();
//                            quiz.put("text", questionText); // Set the course name as needed
//                            quiz.put("choices", choices);
//                            quiz.put("correctChoice", correctAnswer);
//                            quizzesRef.child(quizKey).child("question"+ (finalI +1)).setValue(quiz);
//                            // Toast.makeText(Quiz.this, "Quiz "+ currentQuestionIndex, Toast.LENGTH_SHORT).show();
//

                            displayQuestion(questionText, choices);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(Quiz.this, "Database Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }



    }

    // Generate unique random indices within a specified range
    private List<Integer> generateRandomIndices(int range, int count) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            indices.add(i);
        }

        Collections.shuffle(indices); // Shuffle the list of indices
        return indices.subList(0, count); // Return the first 'count' indices
    }

    // Display the question text and answer choices in your TextView and RadioButtons
    private void displayQuestion(String questionText, Map<String, String> choices) {

        // Set the question text in the TextView
        question.setText(questionText);

        // Set the answer choices in the RadioButtons
        radioButtonA.setText(choices.get("A"));
        radioButtonB.setText(choices.get("B"));
        radioButtonC.setText(choices.get("C"));
        radioButtonD.setText(choices.get("D"));
    }


    private void finishQuiz(){
        Intent intent = new Intent(Quiz.this, Results.class);
        intent.putExtra("score", score);
        intent.putExtra("courseId", courseID);
        intent.putExtra("courseName", courseTitle);


        


        startActivity(intent);

    }


    private void checkRadioButton(){
        // Get the ID of the selected radio button
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (radioButtonA.isChecked()){
            selectedAnswer = "A";
        }else if (radioButtonB.isChecked()){
            selectedAnswer = "B";
        } else if (radioButtonC.isChecked()) {
            selectedAnswer = "C";
        }else if (radioButtonD.isChecked()){
            selectedAnswer = "D";
        }else {
            selectedAnswer = null;
        }

        if (null != selectedAnswer){
            if (selectedAnswer.equals(correctAnswer)){
                score++;
                playerScore.setText("Your score is: "+score+"");
            }
        }

        currentQuestionIndex++;
        fetchQuestions();

    }



}