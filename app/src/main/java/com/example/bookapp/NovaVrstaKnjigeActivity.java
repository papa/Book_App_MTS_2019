package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookapp.Klase.Knjiga;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class NovaVrstaKnjigeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_vrsta_knjige);
        //Boki
        //odje pravi novu knjigu
        //dakle
        final String predmet;
        String naziv;
        final ArrayList<String> autori;
        String izdavac;
        final int godinaIzdanja;
        final EditText predmetText = findViewById(R.id.EditTextPredmet);
        final EditText nazivText = findViewById(R.id.EditTextNazivKnjige);
        final EditText autoriText = findViewById(R.id.EditTextAutori);
        final EditText izdavacText = findViewById(R.id.EditTextIzdavac);
        final EditText godinaIzdanjaText = findViewById(R.id.EditTextGodina);
        Button dodaj = findViewById(R.id.GotovaKnjiga);
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
                else try{
                    int helpBrojGodina = Integer.parseInt(godinaIzdanjaText.getText().toString());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("Knjige");
                    String idKnjiga = dbRef.push().getKey();
                    Knjiga novaKnjigaZaUpload = new Knjiga(idKnjiga,predmetText.getText().toString(),nazivText.getText().toString(),izdavacText.getText().toString(),Integer.parseInt(godinaIzdanjaText.getText().toString()),new ArrayList<String>( Arrays.asList(autoriText.getText().toString().split(","))));
                    dbRef.child(idKnjiga).setValue(novaKnjigaZaUpload);
                    KnjigaDodavanjeActivity.idNoveKnjige = idKnjiga;
                    finish();

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(NovaVrstaKnjigeActivity.this, "Nepravilan format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}