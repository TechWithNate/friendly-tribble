package com.nate.royalquest.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.nate.royalquest.R;
import com.nate.royalquest.activities.Quiz;

public class HomeFragment extends Fragment {

    private View view;
    private MaterialCardView synonyms, antonyms, bTeaser, wai, gtc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        // Retrieve the user's selection from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String userLevel = sharedPreferences.getString("UserLevel", "Primary"); // Default to "Primary" if not set

        // Based on userLevel, load the appropriate views
        if (userLevel.equals("Primary")) {
            loadPrimaryViews(view);
        } else if (userLevel.equals("JHS")) {
            loadJHSViews(view);
        }

        return view;
    }

    // Method to load Primary views
    private void loadPrimaryViews(View view) {
        // For example, make some views visible and others gone
        View primaryView = view.findViewById(R.id.primary_layout);
        View jhsView = view.findViewById(R.id.jhs_layout);

        primaryView.setVisibility(View.VISIBLE);
        jhsView.setVisibility(View.GONE);

        synonyms = primaryView.findViewById(R.id.syn_card);


        synonyms.setOnClickListener(view1 -> {
            loadCourse("course1", "synonyms");
        });


        // Load other UI components specific to Primary

    }

    // Method to load JHS views
    private void loadJHSViews(View view) {
        // For example, make some views visible and others gone
        View primaryView = view.findViewById(R.id.primary_layout);
        View jhsView = view.findViewById(R.id.jhs_layout);

        primaryView.setVisibility(View.GONE);
        jhsView.setVisibility(View.VISIBLE);

        // Load other UI components specific to JHS
    }

    private void loadCourse(String courseId, String courseName){
        Intent intent = new Intent(getContext(), Quiz.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
    }

}
