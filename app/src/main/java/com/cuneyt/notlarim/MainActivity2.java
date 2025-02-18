package com.cuneyt.notlarim;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cuneyt.notlarim.adapters.NoteListAdapter;
import com.cuneyt.notlarim.entities.NoteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    private EditText editTextText;

    private DatabaseReference referenceNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            editTextText = findViewById(R.id.editTextText);

            String gelenId = getIntent().getStringExtra("id");

            referenceNote = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.db_note));

            Query query = referenceNote.orderByChild("id").equalTo(gelenId);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // NoteModel nesnesine dönüştür
                            NoteModel note = dataSnapshot.getValue(NoteModel.class);
                            if (note != null) {
                                // Veriyi EditText'lere yerleştir
                                editTextText.setText(note.getNote());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            editTextText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) { // Klavye kapandığında

                        String yeniDeger = editTextText.getText().toString();

                        referenceNote.setValue(gelenId).addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity2.this, "Veri güncellendi", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity2.this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            /*referenceNote.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot ds: snapshot.getChildren()){
                        NoteModel nModel = ds.getValue(NoteModel.class);
                        editTextText.setText(nModel.getNote());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

            return insets;
        });
    }
}