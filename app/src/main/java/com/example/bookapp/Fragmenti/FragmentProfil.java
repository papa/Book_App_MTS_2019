package com.example.bookapp.Fragmenti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.Klase.Oglasi.CitanjeOglasa;
import com.example.bookapp.KnjigaDodavanjeActivity;
import com.example.bookapp.PodesavanjaNalogaActivity;
import com.example.bookapp.ProfileActivity;
import com.example.bookapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentProfil extends Fragment implements View.OnClickListener {

    private TextView tvIme,tvPrezime,tvEmail,tvBrojOcena,tvProsecnaOcena;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String ime,prezime,email;
    private int brojOcena;
    private double prosecnaOcena;
    //private ArrayList<Bitmap> slike=new ArrayList<>();
    private ArrayList<String> nazivi=new ArrayList<>();
    private ArrayList<String> autori=new ArrayList<>();
    private ArrayList<String> predmeti=new ArrayList<>();
    private ArrayList<String> izdavaci=new ArrayList<>();
    private ArrayList<String> godineIzdanja=new ArrayList<>();
    private ArrayList<String> cene=new ArrayList<>();

    private RecyclerView recyclerView;


    private ArrayList<String> idOglasa=new ArrayList<>();

    private Button priv,nalogP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        initialize(view);

        citajBazu();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.privremeno: {
                Intent intent = new Intent(getActivity(), KnjigaDodavanjeActivity.class);
                startActivity(intent);
            }break;*/
            case R.id.nalog:{
                Intent intent = new Intent(getActivity(), PodesavanjaNalogaActivity.class);
                startActivity(intent);
            }break;
        }
    }

    private void initialize(View view)
    {
        tvIme=(TextView)view.findViewById(R.id.tpIme);
        tvPrezime=(TextView)view.findViewById(R.id.tpPrezime);
        tvEmail=(TextView)view.findViewById(R.id.tpEmail);
        tvBrojOcena=(TextView)view.findViewById(R.id.tpBroj);
        tvProsecnaOcena=(TextView)view.findViewById(R.id.tpProsek);

        recyclerView=(RecyclerView)view.findViewById(R.id.recViewProfile);

        user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Korisnici").child(user.getUid());

        //priv = view.findViewById(R.id.privremeno);
        nalogP=view.findViewById(R.id.nalog);
        //priv.setOnClickListener(this);
        nalogP.setOnClickListener(this);
    }

    private void citajBazu()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                citajInfoKorisnik(dataSnapshot);

                upisi();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void citajInfoKorisnik(DataSnapshot dataSnapshot) {
        Korisnik korisnik = dataSnapshot.getValue(Korisnik.class);

        ime = korisnik.getIme();
        prezime = korisnik.getPrezime();
        email = korisnik.getMail();
        brojOcena = korisnik.getBrojOcena();
        prosecnaOcena = korisnik.getProsecnaOcena();

        if (dataSnapshot.hasChild("oglasi")) {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("oglasi").getChildren()) {
                idOglasa.add(dataSnapshot1.getKey());
            }

            citanje();
        }
    }

    private void citanje() {

        CitanjeOglasa citanjeOglasa = new CitanjeOglasa();

        citanjeOglasa.procitaj(idOglasa, recyclerView, getActivity().getApplication().getApplicationContext());
    }

    private void upisi()
    {
        tvIme.setText(ime + " ");
        tvPrezime.setText(prezime);
        tvProsecnaOcena.setText(String.valueOf(prosecnaOcena));
        tvBrojOcena.setText(String.valueOf(brojOcena));
        tvEmail.setText(email);
    }

    //<editor-fold desc="Fus citanje za svaki slucaj">
        /*if(dataSnapshot.hasChild("oglasi")) {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("oglasi").getChildren()) {
                citajOglase(dataSnapshot1.getKey());
            }
        }
        else
            Log.d("Nema","Korisnik "+ime+" nema oglase svoje");*/


    /*private void citajOglase(String id)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Oglasi").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Oglas oglas=dataSnapshot1.getValue(Oglas.class);

                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Knjige").child(oglas.getIdKnjige());

                    citajKnjigaInfo(databaseReference1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void citajKnjigaInfo(final DatabaseReference databaseReference)
    {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Knjiga knjiga=dataSnapshot.getValue(Knjiga.class);

                nazivi.add(knjiga.getNaziv());
                godineIzdanja.add(String.valueOf(knjiga.getGodinaIzdanja()));
                predmeti.add(knjiga.getPredmet());
                izdavaci.add(knjiga.getIzdavac());

                Log.d("Info","Naziv "+knjiga.getNaziv()+" izdavac "+knjiga.getIzdavac());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
    //</editor-fold>


}
