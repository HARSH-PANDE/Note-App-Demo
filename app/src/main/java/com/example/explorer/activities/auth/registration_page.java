package com.example.explorer.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registration_page extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mName,mMail,mPass,mPhone;
    CardView mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        mName = findViewById(R.id.name_regis);
        mMail = findViewById(R.id.Username_login);
        mPass = findViewById(R.id.Password_login);
        mPhone = findViewById(R.id.phone_regis);
        mRegisterBtn = findViewById(R.id.Button_login);
        mLoginBtn = findViewById(R.id.login_regis);
        progressBar = findViewById(R.id.bar_regis);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


/*
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), login_page.class));
            finish();
        }

 */

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login_page.class));
                finish();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mMail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                String name = mName.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();

                //conditions for inserting  correct details

                if(TextUtils.isEmpty(name)){
                    mName.setError("Empty Field!!");
                    return;
                }

                if(TextUtils.isEmpty(phone)) {
                    mPhone.setError("Empty Field!!");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mMail.setError("Empty Field!!");
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Empty Field!!");
                    return;
                }

                if(pass.length() <= 6 ){
                    mPass.setError("Atleast 6 characters required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Registration of users

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(registration_page.this, "User Registered", Toast.LENGTH_SHORT).show();

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            //hash map method
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",name);
                            user.put("Email",email);
                            user.put("Phone number",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile created ("+userID+")");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure:"+e.toString());
                                }
                            });


                            startActivity(new Intent(getApplicationContext(),login_page.class));
                            return;
                        }

                        else{
                            Toast.makeText(registration_page.this, "Failed!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

    }
}