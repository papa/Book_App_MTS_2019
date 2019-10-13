package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KnjigaDodavanjeActivity extends AppCompatActivity {
static String idNoveKnjige = "nema";
static ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
static ArrayList<String> knjigeString = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //boki pravio
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_dodavanje);
        Spinner spinner = findViewById(R.id.youSpinMeRightRound);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("knjige");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Knjiga added = dataSnapshot1.getValue(Knjiga.class);
                    //knjige.add(added);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String help;
        for (Knjiga x : knjige) {
            help = x.getNaziv() + ", " + x.getIzdavac() + ", " + x.getPredmet() + ", " + x.getGodinaIzdanja();
            for (String l : x.getAutori()) {
                help += l;
            }
            knjigeString.add(help);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, knjigeString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idNoveKnjige = knjige.get(position).getId();
            }
        });
        //ok imam sad koju knjigu je birao
        Button novaKnjiga = findViewById(R.id.novaKnjigaActivityButton);
        novaKnjiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(KnjigaDodavanjeActivity.this,NovaVrstaKnjigeActivity.class);
                KnjigaDodavanjeActivity.this.startActivity(myIntent);
            }
        });

    }
}
