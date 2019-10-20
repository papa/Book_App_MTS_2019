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

    private HashMap<String,String>  citajKnjigaInfo(final DatabaseReference databaseReference)
    {
        final HashMap<String,String> knjigaMapa=new HashMap<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Knjiga knjiga=dataSnapshot.getValue(Knjiga.class);

                knjigaMapa.put("naziv",knjiga.getNaziv());
                knjigaMapa.put("godinaIzdanja",String.valueOf(knjiga.getGodinaIzdanja()));
                knjigaMapa.put("izdavac",knjiga.getIzdavac());
                knjigaMapa.put("predmet",knjiga.getPredmet());

                Log.d("Info","Naziv "+knjiga.getNaziv()+" izdavac "+knjiga.getIzdavac());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        knjigaMapa.notify();

        Log.d("KNJIGAMAPA",knjigaMapa.get("naziv")+"ojsaaa");

        return knjigaMapa;

    }


    private List<HashMap<String,String>> getOglas(String id)
    {
        final List<HashMap<String,String>> oglasi = new ArrayList<>();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Oglasi").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Oglas oglas=dataSnapshot.getValue(Oglas.class);

                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Knjige").child(oglas.getIdKnjige());

                    oglasi.add(citajKnjigaInfo(databaseReference1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return oglasi;
    }



    public List<HashMap<String,String>> konvertuj(String id)
    {
        return getOglas(id);
    }


}
