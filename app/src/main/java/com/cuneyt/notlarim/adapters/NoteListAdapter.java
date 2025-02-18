package com.cuneyt.notlarim.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cuneyt.notlarim.MainActivity2;
import com.cuneyt.notlarim.R;
import com.cuneyt.notlarim.entities.NoteModel;
import com.cuneyt.notlarim.fragments.ListingFragment;
import com.cuneyt.notlarim.fragments.ListingFragmentDirections;
import com.cuneyt.notlarim.fragments.NoteWriteFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.ListResourceBundle;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<NoteModel> noteModelArrayList;


    /*public NoteListAdapter(ArrayList<NoteModel> noteModelArrayList) {
        this.noteModelArrayList = noteModelArrayList;
    }*/

    public NoteListAdapter(ArrayList<NoteModel> noteModelArrayList) {
        this.noteModelArrayList = noteModelArrayList;
    }

    public NoteListAdapter(Context mContext, ArrayList<NoteModel> noteModelArrayList) {
        this.mContext = mContext;
        this.noteModelArrayList = noteModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtRowTitle, txtRowNote, txtRowDate;
        ConstraintLayout constRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRowTitle = itemView.findViewById(R.id.txtRowTitle);
            txtRowNote = itemView.findViewById(R.id.txtRowNote);
            txtRowDate = itemView.findViewById(R.id.txtRowDate);
            constRow = itemView.findViewById(R.id.constRow);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String addUpd = "update";
        String id = noteModelArrayList.get(position).getId();
        String title = noteModelArrayList.get(position).getNoteTitle();
        String note = noteModelArrayList.get(position).getNote();
        String date = noteModelArrayList.get(position).getDate();

        holder.txtRowTitle.setText(title);
        holder.txtRowNote.setText(note);
        holder.txtRowDate.setText(date);

        holder.constRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListingFragmentDirections.ActionListingToWrite pass= ListingFragmentDirections.actionListingToWrite(id, title, note, date, addUpd);
                pass.getId();
                pass.getTitle();
                pass.getNote();
                pass.getDate();
                pass.getAddOrUpdate();
                Navigation.findNavController(v).navigate(pass);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteModelArrayList.size();
    }
}
