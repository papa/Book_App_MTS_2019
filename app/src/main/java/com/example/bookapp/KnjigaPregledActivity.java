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

import java.util.ArrayList;

public class KnjigaPregledActivity extends AppCompatActivity {

    //papa
    //TODO
    //dodaj deo za dodatni opis i ucitaj ga u taj textView
    //idPrenos treba da se uzme iz intenta koji mu dodje
    //Andrijo ti to stavi u Intent pa cu ja posle da ga skupim
    //Intent koji dodje iz Adaptera za knjige
    //i dodatniOpisOglas i idPrenosOglas da se uzme iz intenta
    DatabaseReference knjigaData;
    String idPrenosKnjiga;
    String dodatniOpisOglas;
    String idPrenosOglas;
    Knjiga knjiga;
    TextView nazivKnjige;
    TextView izdavac;
    TextView brojStranica;
    TextView autori;
    TextView godinaIzdanja;
    TextView predmet;
    TextView dodatniOpis;
    TextView brojZainteresovanih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_pregled);

        init();
        postaviListener();
    }

    void postaviListener()
    {
        knjigaData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                knjiga = dataSnapshot.getValue(Knjiga.class);
                ucitajPodatke(knjiga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void ucitajPodatke(Knjiga k)
    {
        nazivKnjige.setText(k.getNaziv());
        //brojStranica.setText(k.getBrojstranica());
        izdavac.setText(k.getIzdavac());
        predmet.setText(k.getPredmet());
        String gi = k.getGodinaIzdanja() + ".";
        godinaIzdanja.setText(gi);
       // brojZainteresovanih.setText(k.getBrojZainteresovanih());
        String autoriS = "";
        ArrayList<String> au = k.getAutori();
        for(int i=0;i<au.size()-1;i++)
        {
            autoriS=autoriS + au.get(i) + ",";
        }
        autoriS = autoriS + au.get(au.size()-1);
        autori.setText(autoriS);
    }

    void init()
    {

        knjigaData = FirebaseDatabase.getInstance().getReference("Knjige").child(idPrenosKnjiga);

        nazivKnjige=(TextView)findViewById(R.id.nazivKnjigePrikaz);
        izdavac = (TextView)findViewById(R.id.izdavacKnjigePrikaz);
        brojStranica=(TextView)findViewById(R.id.brojStranicaPrikaz);
        autori = (TextView)findViewById(R.id.autoriKnjigePrikaz);
        predmet = (TextView)findViewById(R.id.predmetPrikaz);
        godinaIzdanja = (TextView)findViewById(R.id.godinaIzdanjaPrikaz);
        brojZainteresovanih = (TextView)findViewById(R.id.brojZainteresovanihPrikaz);
    }
}
