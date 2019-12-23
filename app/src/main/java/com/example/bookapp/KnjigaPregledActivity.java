package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    String drugiId;
    String idOglasa;
    String idKorisnika;
    Oglas oglas;
    String chatid;

    //TODO
    //andrijio i ovde treba slike
    //posto ih ima 3 moglo bi ono kao
    //da se menjaju na 2-3 sekunde
    //video si to nekad sigurno

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_pregled);

        //todo
        //da se ucita slika
        //i ono da moze vise njih da bude

        prijem();
        init();
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
            drugiId = getIntent().getStringExtra("idKorisnika");
            idOglasa = getIntent().getStringExtra("idOglasa");
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
        Toast.makeText(KnjigaPregledActivity.this, String.valueOf(cena),Toast.LENGTH_LONG).show();
        cenaPrikaz.setText(String.valueOf(cena));
    }

    void init()
    {
        //knjigaData = FirebaseDatabase.getInstance().getReference("Knjige").child(idPrenosKnjiga);

        idKorisnika = ProfileActivity.userr.getUid();
        nazivKnjige=(TextView)findViewById(R.id.nazivKnjigePrikaz);
        izdavac = (TextView)findViewById(R.id.izdavacKnjigePrikaz);
        autori = (TextView)findViewById(R.id.autoriKnjigePrikaz);
        predmet = (TextView)findViewById(R.id.predmetPrikaz);
        godinaIzdanja = (TextView)findViewById(R.id.godinaIzdanjaPrikaz);
        dodatniOpis = (TextView)findViewById(R.id.dodatniOpisPrikazi);
        cenaPrikaz = (TextView)findViewById(R.id.cenaKnjigaPrikaz);
    }

    private int dan;
    private int mesec;
    private int godina;
    private int sati;
    private int minuti;

    private void datumVreme()
    {
        Calendar cal=Calendar.getInstance();

        mesec=cal.get(Calendar.MONTH);
        dan=cal.get(Calendar.DAY_OF_MONTH);
        godina=cal.get(Calendar.YEAR);

        sati=cal.get(Calendar.HOUR_OF_DAY);
        minuti=cal.get(Calendar.MINUTE);
    }

    void klikPoruka(String poruka)
    {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Korisnici");
        //todo
        //ovde da se odnekud uzme ovaj oglas
        db.child(idKorisnika).child("porukeTrazi").child(drugiId).child(idOglasa).setValue(oglas);
        db.child(drugiId).child("porukeNudi").child(idOglasa).setValue(oglas);

        chatid = idKorisnika + drugiId + idOglasa;

        posaljiPoruku(idKorisnika,drugiId,dodatno);
        posaljiPoruku(drugiId,idKorisnika,"neki tekst");

    }

    void posaljiPoruku(String salje,String prima,String poruka)
    {

        datumVreme();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("prima",prima);
        hashMap.put("salje",salje);
        hashMap.put("poruka",poruka);
        hashMap.put("dan",dan);
        hashMap.put("mesec",mesec);
        hashMap.put("godina",godina);
        hashMap.put("sati",sati);
        hashMap.put("minuti",minuti);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Chats").child(chatid).push().setValue(hashMap);

        final DatabaseReference chatref=FirebaseDatabase.getInstance().getReference("Chatlist").child(chatid).child(idKorisnika).child(prima);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())
                {
                    chatref.child("id").setValue(prima);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
