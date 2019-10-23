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

    private ArrayList<Bitmap> slike=new ArrayList<>();
    private ArrayList<String> nazivi=new ArrayList<>();
    private ArrayList<ArrayList<String>> autori=new ArrayList<>();
    private ArrayList<String> predmeti=new ArrayList<>();
    private ArrayList<String> izdavaci=new ArrayList<>();
    private ArrayList<String> godineIzdanja=new ArrayList<>();
    private ArrayList<String> cene=new ArrayList<>();
    private ArrayList<String> dodatniOpis=new ArrayList<>();
    private ArrayList<String> brojZainteresovanih=new ArrayList<>();

    DatabaseReference databaseReference1;
    ArrayList<Knjiga> knjige=new ArrayList<>();
    String ID;
    int brojOglasa=0;

    public CitanjeOglasa() {
    }

    public interface MyCallback {
        void onCallback(Knjiga value);
    }

    public interface CallbackA{
        void onCallback(ArrayList<Knjiga> value);
    }

    private void citajKnjigaInfo(final MyCallback myCallback) {
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

                cene.add(String.valueOf(dataSnapshot.child("cena").getValue()));
                dodatniOpis.add(String.valueOf(dataSnapshot.child("dodatniOpis").getValue()));
                brojZainteresovanih.add(String.valueOf(dataSnapshot.child("brojZainteresovanih").getValue()));

                Oglas oglas = dataSnapshot.getValue(Oglas.class);

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
        brojOglasa=idOglasa.size();

        for(int i=0;i<brojOglasa;i++)
        {
            ID=idOglasa.get(i);

            getOglas(new CallbackA() {
                @Override
                public void onCallback(ArrayList<Knjiga> value) {
                    prikaziOglase(value, recyclerView2, c);
                }
            });
        }

    }

    private void prikaziOglase(List<Knjiga> oglasi,RecyclerView recyclerView,Context context) {
        if (oglasi.size() == brojOglasa) {
            for (int i = 0; i < oglasi.size(); i++) {
                nazivi.add(oglasi.get(i).getNaziv());
                predmeti.add(oglasi.get(i).getPredmet());
                izdavaci.add(oglasi.get(i).getIzdavac());
                autori.add(oglasi.get(i).getAutori());
                godineIzdanja.add(String.valueOf(oglasi.get(i).getGodinaIzdanja()));
            }

            layoutManager = new GridLayoutManager(context, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            AdapterKnjige adapterKnjige = new AdapterKnjige(context,slike, nazivi, autori, predmeti,izdavaci,godineIzdanja,cene,dodatniOpis,brojZainteresovanih);
            recyclerView.setAdapter(adapterKnjige);
        }
    }
}
