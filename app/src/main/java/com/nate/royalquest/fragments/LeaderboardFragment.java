package com.nate.royalquest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nate.royalquest.R;
import com.nate.royalquest.adapters.LeaderboardAdapter;
import com.nate.royalquest.models.Leaderboard;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    private View view;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private ArrayList<Leaderboard> leaderboardList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        initViews();
        leaderboardList.add(new Leaderboard("1", "https://cdn.pixabay.com/photo/2018/03/04/23/37/child-3199624_960_720.jpg",  560, "Divine", "Jesse"));
        leaderboardList.add(new Leaderboard("1", "https://cdn.pixabay.com/photo/2015/08/13/19/40/girl-887396_960_720.jpg", 342, "Jane", "Laura"));
        leaderboardList.add(new Leaderboard("1", "https://cdn.pixabay.com/photo/2022/02/06/14/06/dog-6997211_1280.jpg", 234, "Mathias", "Lovelace"));
        leaderboardList.add(new Leaderboard("1", "https://cdn.pixabay.com/photo/2015/06/12/21/54/child-807533_960_720.jpg", 190, "Jacob", "Smith"));

        leaderboardAdapter = new LeaderboardAdapter(getContext(), leaderboardList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        return view;
    }

    private void initViews(){
        leaderboardRecyclerView = view.findViewById(R.id.leaderboard_recycler);
        leaderboardList = new ArrayList<>();
        leaderboardRecyclerView.setHasFixedSize(true);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
