package com.solanki.sahil.fruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.solanki.sahil.fruit.R;
import com.solanki.sahil.fruit.model.Model;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .ViewHolder> {

    private Context context;
    private List<Model> mList;

    public MyRecyclerViewAdapter(Context context, List<Model> list) {
        this.context = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(mList.get(position).getName());
        holder.email.setText(mList.get(position).getEmail());
        holder.date.setText(mList.get(position).getDate());
        holder.message.setText(mList.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    public void getAllGames(List<Model> list) {
//        this.mList = list;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, email, date, message ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView);
            email = itemView.findViewById(R.id.textView2);
            date = itemView.findViewById(R.id.textView3);
            message = itemView.findViewById(R.id.textView4);
        }
    }
}
