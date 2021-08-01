package com.example.explorer.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorer.R;
import com.example.explorer.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_page extends AppCompatActivity {

    EditText mMail,mPass;
    CardView mLoginBtn;
    TextView mForgotPassBtn;
    TextView mRegisterBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    TextView regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mMail = findViewById(R.id.Username_login);
        mPass = findViewById(R.id.Password_login);
        mForgotPassBtn =findViewById(R.id.ForgotPass_login);
        mLoginBtn = findViewById(R.id.Button_login);
        mRegisterBtn = findViewById(R.id.Signup);
        fAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.ProgressBar_login);


       mRegisterBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(login_page.this, "SignUp", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(view.getContext(),registration_page.class));
           }
       });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mMail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                //conditions for inserting  correct details

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


                //User authentication

                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(login_page.this, "Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(login_page.this, "Sorry, your E-mail/password was incorrect. Please double-check your E-mail/password.\n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });


        mForgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("ResetPassword ?");
                passwordResetDialog.setMessage("Enter Email to received reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //email extraction and reset link sending

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login_page.this, "Reset link sent to your e-mail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login_page.this, "Error! Reset Link not sent."+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //just close it
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }
}