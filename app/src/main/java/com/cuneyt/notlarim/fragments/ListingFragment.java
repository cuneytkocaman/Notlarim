package com.cuneyt.notlarim.fragments;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cuneyt.notlarim.R;
import com.cuneyt.notlarim.adapters.NoteListAdapter;
import com.cuneyt.notlarim.entities.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListingFragment extends Fragment {

    private DatabaseReference referenceNote;
    private NoteListAdapter noteListAdapter;
    private ArrayList<NoteModel> noteModelArrayList = new ArrayList<>();
    private FloatingActionButton fabNoteAdd, fabTodoAdd;
    private RecyclerView rvNoteList;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        fabNoteAdd = view.findViewById(R.id.fabNoteAdd);
        fabTodoAdd = view.findViewById(R.id.fabTodoAdd);
        rvNoteList = view.findViewById(R.id.rvNoteList);

        referenceNote = FirebaseDatabase.getInstance().getReference(requireContext().getResources().getString(R.string.db_note));

        fabNoteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListingFragmentDirections.ActionListingToWrite pass = ListingFragmentDirections.actionListingToWrite("", "", "", "", "add");
                pass.getId();
                pass.getTitle();
                pass.getNote();
                pass.getDate();
                pass.getAddOrUpdate();
                Navigation.findNavController(view).navigate(pass);



            }
        });

        /*fabTodoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(requireContext(), R.style.CustomAlertDialog); // Alert Dialog oluşturuldu. R.style ile oval bölgenin dışında kalan alan opak yapıldı.
                dialog.setContentView(R.layout.alert_add_todo_title);
                dialog.setCancelable(false);
                dialog.show();

                EditText editAlertTodoTitle =dialog.findViewById(R.id.editAlertTodoTitle);
                TextView textAlertYes = dialog.findViewById(R.id.textAlertYes);
                TextView textAlertNo = dialog.findViewById(R.id.textAlertNo);

                textAlertYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ListingFragmentDirections.ActionListingToTodo pass = ListingFragmentDirections.actionListingToTodo(editAlertTodoTitle.toString());
                        pass.getTodoTitle();
                        Navigation.findNavController(view).navigate(pass);

                        dialog.dismiss();
                    //    Navigation.findNavController(view).navigate(R.id.action_listing_to_check);
                    }
                });

                textAlertNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });*/

        show();

        delete();

        sort();

        fabButtonHiddenRecyclerView();

        return view;
    }

    public void show() {

        rvNoteList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true); // VERTICAL, true: RecyclerView'e eklenen veri en alttan üste doğru eklenir.
        linearLayoutManager.setStackFromEnd(true); // RecyclerView'e eklenen veri sayfayı otomatik kaydırır.
        rvNoteList.setItemAnimator(new DefaultItemAnimator());
        rvNoteList.setLayoutManager(linearLayoutManager);

        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInflater = inflater.inflate(R.layout.design_row_note, null);

        noteListAdapter = new NoteListAdapter(noteModelArrayList);
        rvNoteList.setAdapter(noteListAdapter);

        referenceNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteModelArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    NoteModel nModel = ds.getValue(NoteModel.class);

                    noteModelArrayList.add(nModel);
                }

                sort();

                noteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void delete() {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition(); // Kaydırılan öğenin pozisyonu alındı.
                NoteModel noteItem = noteModelArrayList.get(position); // Silinecek öğe alındı

                if (direction == ItemTouchHelper.RIGHT) {

                    Dialog dialog = new Dialog(requireContext(), R.style.CustomAlertDialog); // Alert Dialog oluşturuldu. R.style ile oval bölgenin dışında kalan alan opak yapıldı.
                    dialog.setContentView(R.layout.alert_delete);
                    dialog.setCancelable(false);
                    dialog.show();

                    TextView textAlertYes = dialog.findViewById(R.id.textAlertYes);
                    TextView textAlertNo = dialog.findViewById(R.id.textAlertNo);

                    textAlertYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            referenceNote.child(noteItem.getId()).removeValue();
                            noteModelArrayList.remove(position); // Veri silindi
                            noteListAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    textAlertNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noteListAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                }

                noteListAdapter.notifyItemRemoved(position);
            }

            // RecyclerView'deki satırları sağa veya sola kaydırınca arkaplanda yapılacak işlemle alakalı (sil,güncelle vb) yazılar çıkması sağlandı.
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                Paint paint = new Paint(); // Arka plan ve yazıyı çizmek için Paint nesnesi
                View itemView = viewHolder.itemView;
                float height = itemView.getBottom() - itemView.getTop();

                if (dX > 0) { // Sağa kaydırma
                    paint.setColor(requireContext().getColor(R.color.app));
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), paint);

                    paint.setColor(requireContext().getColor(R.color.delete));
                    paint.setTextSize(40);
                    paint.setTextAlign(Paint.Align.LEFT);
                    c.drawText("Sil", itemView.getLeft() + 50, itemView.getTop() + height / 2 + 15, paint);

                } else if (dX < 0) { // Sola kaydırma
                    paint.setColor(requireContext().getColor(R.color.app));
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);

                    paint.setColor(requireContext().getColor(R.color.delete));
                    paint.setTextSize(40);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    c.drawText("Sil", itemView.getRight() - 50, itemView.getTop() + height / 2 + 15, paint);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvNoteList);
    }

    public void sort() {

        Collections.sort(noteModelArrayList, new Comparator<NoteModel>() { //RecyclerView A->Z sıralama
            @Override
            public int compare(NoteModel noteModel, NoteModel t1) {
                return t1.getNoteTitle().compareToIgnoreCase(noteModel.getNoteTitle());
            }
        });
    }

    public void fabButtonHiddenRecyclerView() {

        rvNoteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    fabNoteAdd.show();
                    fabTodoAdd.show();
                } else if (dy > 0) {
                    fabNoteAdd.hide();
                    fabTodoAdd.hide();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

}