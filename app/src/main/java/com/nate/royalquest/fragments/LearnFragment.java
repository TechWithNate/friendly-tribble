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
import com.nate.royalquest.adapters.LearnAdapter;
import com.nate.royalquest.models.LearnModel;

import java.util.ArrayList;

public class LearnFragment extends Fragment {

    private View view;
    private RecyclerView learnRecycler;
    private LearnAdapter learnAdapter;
    private ArrayList<LearnModel> learnItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.learn_fragment, container, false);
        learnRecycler = view.findViewById(R.id.learn_recycler);
        learnItemList = new ArrayList<>();

        learnItemList.add(new LearnModel("Understanding Photosynthesis", "2024-08-29", "Photosynthesis is the process by which plants make their own food using sunlight, water, and carbon dioxide. The green pigment in plants, called chlorophyll, captures sunlight and converts it into energy. This energy is used to turn water and carbon dioxide into glucose (a type of sugar) and oxygen. This process not only provides food for the plant but also releases oxygen, which is essential for humans and animals to breathe."));
        learnItemList.add(new LearnModel("The Water Cycle Explained", "2024-08-29", "The water cycle describes how water moves on, above, and below the surface of the Earth. It involves processes like evaporation, condensation, and precipitation. Water from oceans, lakes, and rivers evaporates into the air, forming clouds. When these clouds cool down, water condenses and falls back to Earth as rain, snow, or hail, replenishing water bodies and continuing the cycle."));


        learnRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        learnAdapter = new LearnAdapter(getContext(), learnItemList);
        learnRecycler.setAdapter(learnAdapter);


        return view;
    }
}
