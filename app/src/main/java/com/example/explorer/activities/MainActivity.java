package com.example.explorer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorer.R;
import com.example.explorer.activities.Notes.CreateNotes;
import com.example.explorer.activities.Notes.Edit_Notes;
import com.example.explorer.activities.auth.login_page;
import com.example.explorer.activities.models.Notesave;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;
    RecyclerView noteLists;
    //Adapt adapter;

    FirestoreRecyclerAdapter<Notesave, Noteviewholder> noteAdapter;

    FirebaseFirestore fStore;
    FirebaseUser user;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteLists = findViewById(R.id.notesRecyclerView);
        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        Query query = fStore.collection("CityNotes").document(user.getUid()).collection("myNotes").orderBy("CityNoteTitle",Query.Direction.DESCENDING);
        //User notes> uuid > myNotes
        FirestoreRecyclerOptions<Notesave> allNotes = new FirestoreRecyclerOptions.Builder<Notesave>()
                .setQuery(query, Notesave.class)
                .build();
        noteAdapter =new FirestoreRecyclerAdapter<Notesave, Noteviewholder>(allNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull Noteviewholder holder, int i, @NonNull final Notesave notes) {
                holder.noteTitle.setText(notes.getCityNoteTitle());
                holder.noteSubtitle.setText(notes.getCityNoteSubtitle());
                holder.textDateTime.setText(notes.getDateTime());
                holder.linearLayout.setBackgroundColor(holder.view.getResources().getColor(getRandomColor(),null));
                ImageView editIcon=holder.view.findViewById(R.id.editIcon);
                editIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "Dialog Clicked..", Toast.LENGTH_SHORT).show();
                        final String docId= noteAdapter.getSnapshots().getSnapshot(i).getId();
                        PopupMenu menu= new PopupMenu(v.getContext(),v);
                        menu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Toast.makeText(MainActivity.this, " Edit Btn Clicked..", Toast.LENGTH_SHORT).show();
                                Intent a= new Intent(v.getContext(), Edit_Notes.class);
                                a.putExtra("CityNoteTitle",notes.getCityNoteTitle());
                                a.putExtra("CityNoteSubtitle",notes.getCityNoteSubtitle());
                                a.putExtra("CityNoteText",notes.getCityNoteText());
                                a.putExtra("DateTime",notes.getDateTime());
                                a.putExtra("noteId",docId);
                                startActivity(a);
                                return false;
                            }
                        });
                        menu.getMenu().add("DeleteNote").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                DocumentReference docRef = fStore.collection("CityNotes").document(user.getUid()).collection("myNotes").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //note deleted
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Error Deleting", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        menu.show();
                    }
                });

                String docId= noteAdapter.getSnapshots().getSnapshot(i).getId();

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), CreateNotes.class);
                        i.putExtra("CityNoteTitle",notes.getCityNoteTitle());
                        i.putExtra("CityNoteSubtitle",notes.getCityNoteSubtitle());
                        i.putExtra("CityNoteText",notes.getCityNoteText());
                        i.putExtra("DateTime",notes.getDateTime());
                        i.putExtra("noteId",docId);
                        v.getContext().startActivity(i);
                        Toast.makeText(v.getContext(), "Note Clicked..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public Noteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note,parent,false);
                return new Noteviewholder(view);
            }
        };


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        noteLists.setLayoutManager(new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL ));
        noteLists.setAdapter(noteAdapter);

        ImageView imageAddNoteButton = findViewById(R.id.imageAddNoteMain);
        imageAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CreateNotes.class));
                finish();
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNote:
                startActivity(new Intent(this, CreateNotes.class));
                break;
            case R.id.map:
                startActivity(new Intent(this,Maps.class));
                break;
            case R.id.spent:
                startActivity(new Intent(this,ExpensCalcu.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login_page.class));
                Toast.makeText(this, "Logged Out Success", Toast.LENGTH_SHORT).show();
                break;


            default:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

    }
    private int getRandomColor() {


        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.teal_700);
       // colorCode.add(R.color.colorNorColor3);
        colorCode.add(R.color.colorNotColor4);
        colorCode.add(R.color.colorNoteColor2);
        //colorCode.add(R.color.purple_200);
        //colorCode.add(R.color.teal_200);
        //colorCode.add(R.color.LightGreen);
        colorCode.add(R.color.colorRed);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);

    }

    public class Noteviewholder extends RecyclerView.ViewHolder {

        TextView noteTitle;
        TextView noteSubtitle;
        TextView textDateTime;
        LinearLayout linearLayout;
        ImageView edit;
        View view;

        public Noteviewholder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.textTitle);
            noteSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime= itemView.findViewById(R.id.textDateTime);
            linearLayout= itemView.findViewById(R.id.layoutNote);
            edit=itemView.findViewById(R.id.editIcon);
            view=itemView;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }

}