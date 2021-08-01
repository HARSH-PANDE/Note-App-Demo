package com.example.explorer.activities.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorer.R;
import com.example.explorer.activities.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNotes extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseUser user;

    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;

    Intent data ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);

        textDateTime = findViewById(R.id.textDateTime);
        inputNoteText.setMovementMethod(new ScrollingMovementMethod());

        data=getIntent();

        inputNoteTitle.setText(data.getStringExtra("CityNoteTitle"));
        inputNoteSubtitle.setText(data.getStringExtra("CityNoteSubtitle"));
        inputNoteText.setText(data.getStringExtra("CityNoteText"));
        textDateTime.setText(data.getStringExtra("DateTime"));

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> {
           startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        ImageView editNote= findViewById(R.id.editnote);
        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a= new Intent(view.getContext(), Edit_Notes.class);
                a.putExtra("CityNoteTitle",data.getStringExtra("CityNoteTitle"));
                a.putExtra("CityNoteSubtitle",data.getStringExtra("CityNoteSubtitle"));
                a.putExtra("CityNoteText",data.getStringExtra("CityNoteText"));
                a.putExtra("DateTime",data.getStringExtra("DateTime"));
                a.putExtra("noteId",data.getStringExtra("noteId"));

                Toast.makeText(CreateNotes.this, "Edit Note", Toast.LENGTH_SHORT).show();
                startActivity(a);
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date()) );
        //firebase
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v -> {
            String NoteTitle = inputNoteTitle.getText().toString();
            String NoteSubtitle = inputNoteSubtitle.getText().toString();
            String NoteText = inputNoteText.getText().toString();
            String DateTime =textDateTime.getText().toString();



            if (NoteTitle.isEmpty() || NoteSubtitle.isEmpty() || NoteText.isEmpty()) {
                Toast.makeText(CreateNotes.this, "Can not Save note with Empty Field.", Toast.LENGTH_SHORT).show();
                return;
            }


            DocumentReference documentReference = fStore.collection("CityNotes").document(user.getUid()).collection("myNotes").document();
            Map<String, Object> note = new HashMap<>();
            note.put("CityNoteTitle", NoteTitle);
            note.put("CityNoteSubtitle", NoteSubtitle);
            note.put("CityNoteText", NoteText);
            note.put("DateTime", DateTime);

            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CreateNotes.this, "Note Added.", Toast.LENGTH_SHORT).show();
                  //  onBackPressed();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNotes.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                    // progressBarSave.setVisibility(View.VISIBLE);
                }
            });

            startActivity(new Intent(v.getContext(), MainActivity.class));
            finish();
        });

    }
}
