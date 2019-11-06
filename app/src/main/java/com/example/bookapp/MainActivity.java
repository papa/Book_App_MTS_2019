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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText etName,etPassword,etEmail,etSurname;
    private Button btnRegister;
    private TextView tvLogin,naslov;
    private String name,password,email,surname;
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

        //TODO
        //papa
        //ako je zaboravio sifru preko mejla da moze da je promeni
        //to cu ja da uradim jer imali smo to u dodji na teren

        initialize();

        postaviListenere();

    }

    private void postaviListenere()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setMessage("Ucitavanje...");
                if(!etName.getText().toString().trim().equals("") && !etPassword.getText().toString().trim().equals("") && !etEmail.getText().toString().trim().equals("") && !etSurname.getText().toString().trim().equals(""))
                {
                    name=etName.getText().toString().trim();
                    surname=etSurname.getText().toString().trim();
                    email=etEmail.getText().toString().trim();
                    password=etPassword.getText().toString().trim();
                    if(password.length()<6)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Lozinka mora biti duza od 6 karaktera",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Greska prilikom registrovanja.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Uspesna registracija.",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                            intent.putExtra("name",name);
                                            intent.putExtra("surname",surname);
                                            intent.putExtra("email",email);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }}
                else
                {
                    Toast.makeText(MainActivity.this,"Popunite sva polja!",Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null)
            updateUI();
    }

    private void updateUI()
    {
        Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);
    }



    private void initialize()
    {
        etName=(EditText)findViewById(R.id.eName);
        etPassword=(EditText)findViewById(R.id.ePass);
        etEmail=(EditText)findViewById(R.id.eEmail);
        etSurname=(EditText)findViewById(R.id.eSurname);

        tvLogin=(TextView)findViewById(R.id.tvLog);

        btnRegister=(Button)findViewById(R.id.btRegister);

        auth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        /*typeface = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");

        etName.setTypeface(typeface);
        etEmail.setTypeface(typeface);
        etPassword.setTypeface(typeface);
        naslov.setTypeface(typeface);
        btnRegister.setTypeface(typeface);
        tvLogin.setTypeface(typeface);

         */
    }
}
