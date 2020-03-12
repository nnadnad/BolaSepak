package com.example.bolasepak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolasepak.R;

import java.util.ArrayList;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.MyHolder> {

    private Context context ;
    private ArrayList<String> arrName ;

    public GoalsAdapter(Context context, ArrayList<String> arrName) {
        this.context = context;
        this.arrName = arrName;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.goal_item,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String name = arrName.get(i) ;
        myHolder.show_goal.setText(name) ;
    }

    @Override
    public int getItemCount() {
        return arrName.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView show_goal ;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            show_goal = itemView.findViewById(R.id.show_goal) ;
        }
    }

}
