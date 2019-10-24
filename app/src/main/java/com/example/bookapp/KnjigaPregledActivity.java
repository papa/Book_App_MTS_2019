package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bookapp.Klase.Knjiga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KnjigaPregledActivity extends AppCompatActivity {

    //papa
    DatabaseReference knjigaData;
    String idPrenosKnjiga;
    TextView nazivKnjige;
    TextView izdavac;
    TextView brojStranica;
    TextView autori;
    TextView godinaIzdanja;
    TextView predmet;
    TextView dodatniOpis;
    TextView brojZainteresovanih;
    TextView cenaPrikaz;

    //Andrija-prijem intenta
    private String naziv;
    private String autor;
    private int cena;
    String pr,izd,dodatno;
    int gizdanja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_pregled);

        init();
        prijem();
        ucitajPodatke();

        //ovde su ti primljene informacije o knjizi u ova tri stringa
        //fali slika al to cemo kasnije
        //prijem();
    }

    private void prijem()
    {
        if(getIntent().hasExtra("autor"))
        {
            autor=getIntent().getStringExtra("autor");
            naziv=getIntent().getStringExtra("naziv");
            cena=getIntent().getIntExtra("cena",0);
            gizdanja=getIntent().getIntExtra("godinaIzdanja",0);
            pr = getIntent().getStringExtra("predmet");
            izd = getIntent().getStringExtra("izdavac");
            dodatno = getIntent().getStringExtra("opis");
        }
    }

//    void postaviListener()
//    {
//        knjigaData.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                knjiga = dataSnapshot.getValue(Knjiga.class);
//                ucitajPodatke(knjiga);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    void ucitajPodatke()
    {
        nazivKnjige.setText(naziv);
        izdavac.setText(izd);
        predmet.setText(pr);
        String gi = gizdanja + ".";
        godinaIzdanja.setText(gi);
        String autoriS = "";
//        ArrayList<String> au = k.getAutori();
//        for(int i=0;i<au.size()-1;i++)
//        {
//            autoriS=autoriS + au.get(i) + ",";
//        }
//        autoriS = autoriS + au.get(au.size()-1);
//        autori.setText(autoriS);
        dodatniOpis.setText(dodatno);
        cenaPrikaz.setText(cena);
    }

    void init()
    {

        knjigaData = FirebaseDatabase.getInstance().getReference("Knjige").child(idPrenosKnjiga);

        nazivKnjige=(TextView)findViewById(R.id.nazivKnjigePrikaz);
        izdavac = (TextView)findViewById(R.id.izdavacKnjigePrikaz);
        autori = (TextView)findViewById(R.id.autoriKnjigePrikaz);
        predmet = (TextView)findViewById(R.id.predmetPrikaz);
        godinaIzdanja = (TextView)findViewById(R.id.godinaIzdanjaPrikaz);
        dodatniOpis = (TextView)findViewById(R.id.dodatniOpisPrikaz);
        cenaPrikaz = (TextView)findViewById(R.id.cenaKnjigaPrikaz);
    }
}
