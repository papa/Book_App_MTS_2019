package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookapp.Klase.Knjiga;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;

public class NovaVrstaKnjigeActivity extends AppCompatActivity {

    String predmet;
    String naziv;
    ArrayList<String> autori;
    String izdavac;
    int godinaIzdanja;
    EditText predmetText;
    EditText nazivText;
    EditText autoriText;
    EditText izdavacText;
    EditText godinaIzdanjaText;
    Button dodaj;

    String idKnjiga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_vrsta_knjige);
        //Boki
        //odje pravi novu knjigu
        //dakle

        init();

        postaviListenere();

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(predmetText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi predmet", Toast.LENGTH_SHORT).show();
                }
                else if(nazivText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi naziv", Toast.LENGTH_SHORT).show();
                }
                else if(izdavacText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi izdavaca", Toast.LENGTH_SHORT).show();
                }
                else if(autoriText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi autore", Toast.LENGTH_SHORT).show();
                }
                else if(godinaIzdanjaText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi godinu", Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        int helpBrojGodina = Integer.parseInt(godinaIzdanjaText.getText().toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = database.getReference("Knjige");
                        idKnjiga = dbRef.push().getKey();
                        Knjiga novaKnjigaZaUpload = new Knjiga(idKnjiga,predmetText.getText().toString(),nazivText.getText().toString(),izdavacText.getText().toString(),Integer.parseInt(godinaIzdanjaText.getText().toString()),new ArrayList<String>( Arrays.asList(autoriText.getText().toString().split(","))));
                        dbRef.child(idKnjiga).setValue(novaKnjigaZaUpload)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("idknjige", idKnjiga);
                                        setResult(KnjigaDodavanjeActivity.RESULT_OK,returnIntent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("idknjige", "");
                                        setResult(KnjigaDodavanjeActivity.RESULT_OK,returnIntent);
                                        finish();
                                    }
                                });
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(NovaVrstaKnjigeActivity.this, "Nepravilan format", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void postaviListenere()
    {
        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(predmetText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi predmet", Toast.LENGTH_SHORT).show();
                }
                else if(nazivText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi naziv", Toast.LENGTH_SHORT).show();
                }
                else if(izdavacText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi izdavaca", Toast.LENGTH_SHORT).show();
                }
                else if(autoriText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi autore", Toast.LENGTH_SHORT).show();
                }
                else if(godinaIzdanjaText.getText().toString().isEmpty()) {
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Unesi godinu", Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        int helpBrojGodina = Integer.parseInt(godinaIzdanjaText.getText().toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = database.getReference("Knjige");
                        idKnjiga = dbRef.push().getKey();
                        Knjiga novaKnjigaZaUpload = new Knjiga(idKnjiga,predmetText.getText().toString(),nazivText.getText().toString(),izdavacText.getText().toString(),Integer.parseInt(godinaIzdanjaText.getText().toString()),new ArrayList<String>( Arrays.asList(autoriText.getText().toString().split(","))));
                        dbRef.child(idKnjiga).setValue(novaKnjigaZaUpload)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("idknjige", idKnjiga);
                                        setResult(KnjigaDodavanjeActivity.RESULT_OK,returnIntent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("idknjige", "");
                                        setResult(KnjigaDodavanjeActivity.RESULT_OK,returnIntent);
                                        finish();
                                    }
                                });
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(NovaVrstaKnjigeActivity.this, "Nepravilan format", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void init()
    {
        predmetText = findViewById(R.id.EditTextPredmet);
        nazivText = findViewById(R.id.EditTextNazivKnjige);
        autoriText = findViewById(R.id.EditTextAutori);
        izdavacText = findViewById(R.id.EditTextIzdavac);
        godinaIzdanjaText = findViewById(R.id.EditTextGodina);
        dodaj = findViewById(R.id.GotovaKnjiga);
    }

}