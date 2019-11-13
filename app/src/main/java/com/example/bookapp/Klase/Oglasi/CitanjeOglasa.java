package com.example.bookapp.Klase.Oglasi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CitanjeOglasa {

    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Bitmap> slike = new ArrayList<>();

    DatabaseReference databaseReference1;
    ArrayList<Knjiga> knjige = new ArrayList<>();
    String ID;
    int brojOglasa = 0;

    ArrayList<Oglas> oglasi = new ArrayList<>();


    public CitanjeOglasa() {
    }

    public interface MyCallback {
        void onCallback(Knjiga value);
    }

    public interface CallbackA {
        void onCallback(ArrayList<Knjiga> value);
    }

    private void citajKnjigaInfo(final MyCallback myCallback)
    {
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Knjiga knjiga = dataSnapshot.getValue(Knjiga.class);

                myCallback.onCallback(knjiga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOglas(final CallbackA myCallback) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Oglasi").child(ID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Oglas oglas = dataSnapshot.getValue(Oglas.class);

                oglasi.add(oglas);

                Log.d("CENA",String.valueOf(oglas.getCena()));

                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Knjige").child(oglas.getIdKnjige());

                citajKnjigaInfo(new MyCallback() {
                    @Override
                    public void onCallback(Knjiga value) {
                        knjige.add(value);

                        myCallback.onCallback(knjige);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void procitaj(ArrayList<String> idOglasa, final RecyclerView recyclerView2, final Context c)
    {
        brojOglasa = idOglasa.size();

        for (int i = 0; i < brojOglasa; i++)
        {
            ID = idOglasa.get(i);

            getOglas(new CallbackA() {
                @Override
                public void onCallback(ArrayList<Knjiga> value) {
                    prikaziOglase(value, recyclerView2,c );
                }
            });
        }

    }

    private void prikaziOglase(ArrayList<Knjiga> knjige, RecyclerView recyclerView, Context context)
    {
        if (knjige.size() == brojOglasa)
        {
            layoutManager = new GridLayoutManager(context, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            AdapterKnjige adapterKnjige = new AdapterKnjige(context, slike,oglasi,knjige);
            recyclerView.setAdapter(adapterKnjige);
        }
    }

    public ArrayList<Oglas> uzmiOglase()
    {
        return oglasi;
    }

    public ArrayList<Knjiga> uzmiKnjige()
    {
        return knjige;
    }
}
