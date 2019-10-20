package com.example.bookapp.Klase.Oglasi;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitanjeOglasa {


    public CitanjeOglasa(){}

    void  citajKnjigaInfo(final DatabaseReference databaseReference, final ArrayList<Knjiga> knjige)
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Knjiga knjiga=dataSnapshot.getValue(Knjiga.class);

                knjige.add(knjiga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void getOglas(String id, final ArrayList<Knjiga> knjige)
    {
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Oglasi").child(id);

        Knjiga knjiga = new Knjiga();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Oglas oglas=dataSnapshot.getValue(Oglas.class);

                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Knjige").child(oglas.getIdKnjige());

                    citajKnjigaInfo(databaseReference1,knjige);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
