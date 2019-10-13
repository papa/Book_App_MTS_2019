package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class KnjigaPregledActivity extends AppCompatActivity {

    DatabaseReference knjigaData;
    String idPrenos;
    Knjiga knjiga;
    TextView imeKnjige;
    TextView izdavac;
    TextView brojStranica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_pregled);

        init();
        postaviListener();
        ucitajPodatke();
    }

    void postaviListener()
    {
        knjigaData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                knjiga = dataSnapshot.getValue(Knjiga.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void ucitajPodatke()
    {

    }

    void init()
    {
        //idPrenos treba da se uzme iz intenta koji mu dodje
        //Andrijo ti to stavi u Intent pa cu ja posle da ga skupim
        //Intent koji dodje iz Adaptera za knjige

        knjigaData = FirebaseDatabase.getInstance().getReference("knjige").child(idPrenos);
    }
}
