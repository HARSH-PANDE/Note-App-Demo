package com.example.explorer.activities.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

import java.util.HashMap;
import java.util.Map;

public class Edit_Notes extends AppCompatActivity {

    Intent dat ;
    private EditText editNoteTit, editNoteSub, editNoteTxt;
    private TextView edittextDateTime;

    FirebaseFirestore fStore;
    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__notes);

        ImageView imgback =findViewById(R.id.editimageBack);
        imgback.setOnClickListener(view ->{
            onBackPressed();
        });

        dat=getIntent();

        editNoteTit=findViewById(R.id.editNoteTitle);
        editNoteSub=findViewById(R.id.editNoteSubtitle);
        editNoteTxt=findViewById(R.id.editinputNote);
        editNoteTxt.setMovementMethod(new ScrollingMovementMethod());
        edittextDateTime=findViewById(R.id.edittextDateTime);

        String noteTitle= dat.getStringExtra("CityNoteTitle");
        String noteSub= dat.getStringExtra("CityNoteSubtitle");
        String noteTText= dat.getStringExtra("CityNoteText");
        String noteDateTime= dat.getStringExtra("DateTime");

        editNoteTit.setText(noteTitle);
        editNoteSub.setText(noteSub);
        editNoteTxt.setText(noteTText);
        edittextDateTime.setText(noteDateTime);

        ImageView savenote=findViewById(R.id.savenote);

        fStore = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        savenote.setOnClickListener(view -> {

            String NTitle = editNoteTit.getText().toString();
            String NSubtitle = editNoteSub.getText().toString();
            String NText = editNoteTxt.getText().toString();
            String NDateTime =edittextDateTime.getText().toString();



            if (NTitle.isEmpty() || NSubtitle.isEmpty() || NText.isEmpty()) {


                Toast.makeText(Edit_Notes.this, "Can not Save note with Empty Field.", Toast.LENGTH_SHORT).show();
                return;
            }


            DocumentReference documentReference = fStore.collection("CityNotes").document(user.getUid()).collection("myNotes").document(dat.getStringExtra("noteId"));
            Map<String, Object> note = new HashMap<>();
            note.put("CityNoteTitle", NTitle);
            note.put("CityNoteSubtitle", NSubtitle);
            note.put("CityNoteText", NText);
            note.put("DateTime", NDateTime);

            documentReference.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Edit_Notes.this, "Note Added.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Edit_Notes.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                    // progressBarSave.setVisibility(View.VISIBLE);
                }
            });

            startActivity(new Intent(view.getContext(), MainActivity.class));
            finish();
        });

    }
}