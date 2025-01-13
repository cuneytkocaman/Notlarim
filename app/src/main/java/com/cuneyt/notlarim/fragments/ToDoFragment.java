package com.cuneyt.notlarim.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cuneyt.notlarim.R;
import com.cuneyt.notlarim.adapters.NoteListAdapter;
import com.cuneyt.notlarim.adapters.TodoAdapter;
import com.cuneyt.notlarim.assistantclass.DateTime;
import com.cuneyt.notlarim.assistantclass.RandomId;
import com.cuneyt.notlarim.entities.TodoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {
    private FloatingActionButton fabTodoAdd;
    private EditText editTodoTitle, editTodoWrite;
    private RecyclerView rvTodoList;
    private DateTime dateTime = new DateTime();
    private RandomId randomId = new RandomId();
    private TodoModel todoModel;
    private DatabaseReference referenceTodo;
    private TodoAdapter todoAdapter;
    private ArrayList<TodoModel> todoModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);

        fabTodoAdd = view.findViewById(R.id.fabTodoAdd);
        editTodoTitle = view.findViewById(R.id.editTodoTitle);
        editTodoWrite = view.findViewById(R.id.editTodoWrite);
        rvTodoList = view.findViewById(R.id.rvTodoList);

        referenceTodo = FirebaseDatabase.getInstance().getReference(requireContext().getResources().getString(R.string.db_todo));

        fabTodoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        ToDoFragmentArgs bundle = ToDoFragmentArgs.fromBundle(getArguments());
        String todoTitle = bundle.getTodoTitle();

        editTodoTitle.setText(todoTitle);

        return view;
    }

    public void add() {

        String id = randomId.randomUUID();
        String title = editTodoTitle.getText().toString();
        String todo = editTodoWrite.toString();
        String date = dateTime.currentlyDateTime("dd.MM.yyyy");
        String status = "active";

        referenceTodo.child(title).child(id).setValue(todoModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    todoModel = new TodoModel(id, todo, date, status);
                    referenceTodo.child(id).setValue(todoModel);
                }
            }
        });
    }

    public void show(){
        rvTodoList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true); // VERTICAL, true: RecyclerView'e eklenen veri en alttan üste doğru eklenir.
        linearLayoutManager.setStackFromEnd(true); // RecyclerView'e eklenen veri sayfayı otomatik kaydırır.
        rvTodoList.setItemAnimator(new DefaultItemAnimator());
        rvTodoList.setLayoutManager(linearLayoutManager);

        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInflater = inflater.inflate(R.layout.design_row_note, null);

        todoAdapter = new TodoAdapter(todoModelArrayList);
        rvTodoList.setAdapter(todoAdapter);

        referenceTodo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoModelArrayList.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    TodoModel todo = ds.getValue(TodoModel.class);
                    todoModelArrayList.add(todo);
                }

                todoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}