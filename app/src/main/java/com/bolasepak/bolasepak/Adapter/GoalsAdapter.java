package com.bolasepak.bolasepak.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolasepak.bolasepak.R;

import java.util.ArrayList;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.MyHolder>
{

    Context context ;
    ArrayList<String> arrName ;

    public GoalsAdapter(Context context, ArrayList<String> arrName) {
        this.context = context;
        this.arrName = arrName;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goals,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String name = arrName.get(i) ;
        myHolder.tvGoals.setText(name) ;
    }

    @Override
    public int getItemCount() {
        return arrName.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public TextView tvGoals ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvGoals = itemView.findViewById(R.id.tvGoals) ;
        }
    }
}
