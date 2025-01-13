package com.cuneyt.notlarim.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cuneyt.notlarim.R;
import com.cuneyt.notlarim.assistantclass.DateTime;
import com.cuneyt.notlarim.assistantclass.RandomId;
import com.cuneyt.notlarim.entities.NoteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NoteWriteFragment extends Fragment {

    private TextInputEditText inputTitle, inputNote;
    private FloatingActionButton fabNoteSave;
    private NoteModel noteModel;
    private DateTime dateTime = new DateTime();
    private RandomId randomId = new RandomId();
    private DatabaseReference referenceNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_write, container, false);

        inputNote = view.findViewById(R.id.inputNote);
        inputTitle = view.findViewById(R.id.inputTitle);
        fabNoteSave = view.findViewById(R.id.fabNoteSave);

        referenceNote = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.db_note));

        selectNoteShow();

        NoteWriteFragmentArgs bundle = NoteWriteFragmentArgs.fromBundle(getArguments());
        String addUpd = bundle.getAddOrUpdate();

        fabNoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (addUpd){
                    case "add":
                        add();
                        break;
                    case "update":
                        update();
                        break;
                }

                Navigation.findNavController(view).navigate(R.id.action_write_to_list);
            }
        });

        return view;
    }

    public void add(){

        String id = randomId.randomUUID();
        String title = inputTitle.getText().toString();
        String note = inputNote.getText().toString();
        String date = dateTime.currentlyDateTime("dd.MM.yyyy");
        String notification = "";

        referenceNote.child(id).setValue(noteModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    noteModel = new NoteModel(id, title, note, date, notification);
                    referenceNote.child(id).setValue(noteModel);

                } else {
                    Toast.makeText(requireContext(), "Not eklenemedi.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void selectNoteShow(){
        NoteWriteFragmentArgs bundle = NoteWriteFragmentArgs.fromBundle(getArguments());
        String id = bundle.getId();
        String title = bundle.getTitle();
        String note = bundle.getNote();
        String date = bundle.getDate();
        String addUpd = bundle.getAddOrUpdate();

        inputTitle.setText(title);
        inputNote.setText(note);
    }

    public void update(){

        NoteWriteFragmentArgs bundle = NoteWriteFragmentArgs.fromBundle(getArguments());
        String currentlyId = bundle.getId();

        referenceNote.child(currentlyId).setValue(noteModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                HashMap<String, Object> updateNote = new HashMap<>();
                updateNote.put("id", currentlyId);
                updateNote.put("noteTitle", inputTitle.getText().toString());
                updateNote.put("note", inputNote.getText().toString());
                updateNote.put("date", dateTime.currentlyDateTime("dd.MM.yyyy"));
                updateNote.put("notification", "");

                referenceNote.child(currentlyId).updateChildren(updateNote);
            }
        });
    }

}