package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText etEmail,etPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private String email,password;
    private String userId;
    private Typeface typeface;
    private TextView naslov;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Sve isto kao za prosli activity, samo design objects i obratite paznju na imenovanje
        da ne kucate sto puta
         */

        initialize();

        postaviListenere();

    }


    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                userId = firebaseUser.getUid();
            }
        }
    };

    private void postaviListenere()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                if(!etEmail.getText().toString().trim().equals("") && !etPassword.getText().toString().trim().equals(""))
                {
                    email=etEmail.getText().toString().trim();
                    password=etPassword.getText().toString().trim();

                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful())
                                    {
                                        Toast.makeText(LoginActivity.this,
                                                "Neuspesna prijava", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        authListener.onAuthStateChanged(auth);
                                        Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                                        intent.putExtra("userId",userId);
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this,
                                                "Prijava uspesna!", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(LoginActivity.this,"Popunite sva polja",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initialize()
    {
        auth=FirebaseAuth.getInstance();

        etEmail=(EditText)findViewById(R.id.eLogEmail);
        etPassword=(EditText)findViewById(R.id.eLogPassword);

        btnLogin=(Button)findViewById(R.id.btLogin);

        /*naslov=(TextView)findViewById(R.id.textView5);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");

        btnLogin.setTypeface(typeface);
        etEmail.setTypeface(typeface);
        etPassword.setTypeface(typeface);
        naslov.setTypeface(typeface);*/

        progressDialog=new ProgressDialog(this);
    }


}
