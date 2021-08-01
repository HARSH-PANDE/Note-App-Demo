package com.example.explorer.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.explorer.R;

public class Maps extends AppCompatActivity {

    private EditText button4;
    private Button button1;
    private Button button2;
    private Button button9;
    private Button button5;
    private Button button6;
    private Button button7;

    private SearchView s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        button4=findViewById(R.id.button4);
        button9=findViewById(R.id.button9);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);

//         final String str = button4.getText().toString();
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = button4.getText().toString();
                Intent b = new Intent(Intent.ACTION_VIEW);
                b.setData(Uri.parse("geo:0,0?q="+str));
                Intent chooser = Intent.createChooser(b,"Launch Maps");
                startActivity(chooser);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = button4.getText().toString();
                Intent c = new Intent(Intent.ACTION_VIEW);
                c.setData(Uri.parse("geo:0,0?q=hotels Nearby "+str));
                Intent chooser = Intent.createChooser(c,"Launch Maps");
                startActivity(chooser);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = button4.getText().toString();
                Intent d = new Intent(Intent.ACTION_VIEW);
                d.setData(Uri.parse("geo:0,0?q=airport Nearby "+str));
                Intent chooser = Intent.createChooser(d,"Launch Maps");
                startActivity(chooser);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = button4.getText().toString();
                Intent e = new Intent(Intent.ACTION_VIEW);
                e.setData(Uri.parse("geo:0,0?q=tourist places Nearby "+str));
                Intent chooser = Intent.createChooser(e,"Launch Maps");
                startActivity(chooser);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = button4.getText().toString();
                Intent e = new Intent(Intent.ACTION_VIEW);
                e.setData(Uri.parse("geo:0,0?q=Restraunts Nearby "+str));
                Intent chooser = Intent.createChooser(e,"Launch Maps");
                startActivity(chooser);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = button4.getText().toString();
                Intent e = new Intent(Intent.ACTION_VIEW);
                e.setData(Uri.parse("geo:0,0?q=Hospitals Nearby "+str));
                Intent chooser = Intent.createChooser(e,"Launch Maps");
                startActivity(chooser);
            }
        });

    }
}