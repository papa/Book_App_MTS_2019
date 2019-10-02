package com.example.bookapp;

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
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etName,etPassword,etEmail;
    private Button btnRegister;
    private TextView tvLogin,naslov;
    private String name,password,email;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Dole u initialize su inicijalizovani design objects pa im dajte takva imena da ne biste prekucavali
        Ovo za typeface je font pa to ostavite pod komentarima u slucaju da nam zatreba
        design objects razumete na osnovu imena
        ako hocete da dodate nova polja samo edit text i proverite da l nije prazan ova prva grana u btnregister onclick
         */

        /*btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                if(!etName.getText().toString().trim().equals("") && !etPassword.getText().toString().trim().equals("") && !etEmail.getText().toString().trim().equals(""))
                {
                    name=etName.getText().toString().trim();
                    email=etEmail.getText().toString().trim();
                    password=etPassword.getText().toString().trim();
                    if(password.length()<6)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Password must be longer than 6 characters",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Successfully registered.",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent cintent = new Intent(MainActivity.this, ProfileActivity.class);
                                            cintent.putExtra("name",name);
                                            startActivity(cintent);
                                        }
                                    }
                                });
                    }}
                else
                {
                    Toast.makeText(MainActivity.this,"Please fill in all the fields.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
         */
    }
    /*@Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null)
            updateUI();
    }
    private void updateUI(){
        Intent lintent=new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(lintent);
        finish();
    }
    private void initialize()
    {
        etName=(EditText)findViewById(R.id.eName);
        etPassword=(EditText)findViewById(R.id.ePassword);
        etEmail=(EditText)findViewById(R.id.eEmail);

        tvLogin=(TextView)findViewById(R.id.tvLog);
        naslov=(TextView)findViewById(R.id.textView4);

        btnRegister=(Button)findViewById(R.id.btRegister);

        auth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");

        etName.setTypeface(typeface);
        etEmail.setTypeface(typeface);
        etPassword.setTypeface(typeface);
        naslov.setTypeface(typeface);
        btnRegister.setTypeface(typeface);
        tvLogin.setTypeface(typeface);
    }
     */
}
