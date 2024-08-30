package com.nate.royalquest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nate.royalquest.R;
import com.nate.royalquest.activities.LearnDetailActivity;
import com.nate.royalquest.models.LearnModel;

import java.util.ArrayList;

public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.ViewHolder> {

    private ArrayList<LearnModel> learnItemList;
    private Context context;

    public LearnAdapter(Context context, ArrayList<LearnModel> learnItemList) {
        this.context = context;
        this.learnItemList = learnItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.learn_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LearnModel item = learnItemList.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LearnDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("content", item.getContent());
            intent.putExtra("date", item.getDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return learnItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.learn_title);
            date = itemView.findViewById(R.id.learn_date);
        }
    }
}

