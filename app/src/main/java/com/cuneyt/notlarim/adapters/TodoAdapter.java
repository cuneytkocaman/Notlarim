package com.cuneyt.notlarim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cuneyt.notlarim.R;
import com.cuneyt.notlarim.entities.TodoModel;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    private ArrayList<TodoModel> todoModelArrayList;
    private Context mContext;

    public TodoAdapter(ArrayList<TodoModel> todoModelArrayList) {
        this.todoModelArrayList = todoModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtRowTodo;
        ConstraintLayout constRowTodo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRowTodo = itemView.findViewById(R.id.txtRowTodo);
            constRowTodo = itemView.findViewById(R.id.constRowTodo);
        }
    }

    @NonNull
    @Override
    public TodoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row_note, parent, false);
        return new MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull TodoAdapter.MyViewHolder holder, int position) {

        String id = todoModelArrayList.get(position).getId();
        String todo = todoModelArrayList.get(position).getTodo();
        String date = todoModelArrayList.get(position).getDate();

        holder.txtRowTodo.setText(todo);
    }

    @Override
    public int getItemCount() {
        return todoModelArrayList.size();
    }
}

