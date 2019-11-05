package com.example.bookapp.Fragmenti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentKnjige extends Fragment implements View.OnClickListener{
    private ArrayList<Bitmap> slike = new ArrayList<Bitmap>();
    private ArrayList<String> nazivi = new ArrayList<String>();
    private ArrayList<ArrayList<String>> autori = new ArrayList<ArrayList<String>>();
    private ArrayList<String> cene = new ArrayList<String>();
    private ArrayList<String> izdavaci = new ArrayList<String>();
    private ArrayList<String> predmeti = new ArrayList<String>();
    private ArrayList<String> godineIzdanja = new ArrayList<String>();
    private ArrayList<String> dodatniOpisi = new ArrayList<String>();
    private ArrayList<String>  brojeviZainteresovanih = new ArrayList<String>();

    private ArrayList<Bitmap> slikeF;
    private ArrayList<String> naziviF;
    private ArrayList<ArrayList<String>> autoriF;
    private ArrayList<String> ceneF;
    private ArrayList<String> izdavaciF;
    private ArrayList<String> predmetiF;
    private ArrayList<String> godineIzdanjaF;
    private ArrayList<String> dodatniOpisiF;
    private ArrayList<String>  brojeviZainteresovanihF;

    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private ArrayList<Oglas> oglasi = new ArrayList<Oglas>();
    private int[] connection;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private CheckBox cenaRB;
    private CheckBox predmetRB;
    private CheckBox izdavacRB;
    private EditText cenaET;
    private EditText predmetET;
    private EditText izdavacET;
    private Button filter;

    private String cenaFilterString = "";
    private String predmetFilterString = "";
    private String izdavacFilterString = "";

    private boolean started = false;
    private AdapterKnjige adapterKnjige;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knjige, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerKnjige);
        cenaRB=(CheckBox)view.findViewById(R.id.cenaCheckBox);
        predmetRB=(CheckBox)view.findViewById(R.id.predmetCheckBox);
        izdavacRB=(CheckBox)view.findViewById(R.id.izdavacCheckBox);
        cenaET=(EditText)view.findViewById(R.id.cenaFilterText);
        predmetET=(EditText)view.findViewById(R.id.predmetFilterText);
        izdavacET=(EditText)view.findViewById(R.id.izdavacFilterText);
        filter=(Button)view.findViewById(R.id.filterButton);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cenaRB.isChecked()){
                    if(!cenaET.getText().toString().isEmpty()){
                        cenaFilterString=cenaET.getText().toString();
                    }
                }
                if(predmetRB.isChecked()){
                    if(!predmetET.getText().toString().isEmpty()){
                        predmetFilterString=predmetET.getText().toString().toLowerCase();
                    }
                }
                if(izdavacRB.isChecked()){
                    if(!izdavacET.getText().toString().isEmpty()){
                        izdavacFilterString=izdavacET.getText().toString().toLowerCase();
                    }
                }
                if(cenaFilterString.isEmpty()&&predmetFilterString.isEmpty()&&izdavacFilterString.isEmpty()){
                    start();
                }
                else {
                    clear();
                    started = false;
                    for (int i = 0; i < oglasi.size(); i++) {
                        if (cenaFilterString.isEmpty() || Integer.parseInt(cene.get(i)) <= Integer.parseInt(cenaFilterString)) {
                            if (predmetFilterString.isEmpty() || predmeti.get(i).contains(predmetFilterString)) {
                                if (izdavacFilterString.isEmpty() || izdavaci.get(i).contains(izdavacFilterString)) {
                                    slikeF.add(slike.get(i));
                                    naziviF.add(nazivi.get(i));
                                    autoriF.add(autori.get(i));
                                    predmetiF.add(predmeti.get(i));
                                    izdavaciF.add(izdavaci.get(i));
                                    godineIzdanjaF.add(godineIzdanja.get(i));
                                    ceneF.add(cene.get(i));
                                    dodatniOpisiF.add(dodatniOpisi.get(i));
                                    brojeviZainteresovanihF.add(brojeviZainteresovanih.get(i));
                                }
                            }
                        }
                    }
                    adapterKnjige.notifyDataSetChanged();
                }
            }
        });


        //-BOGDAN-
        //pretpostavljam da se to ovde radi
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ucitajIzBaze();
        napuniListe();
        start();
        napraviListu();
        setRecycler();

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    private void napraviListu(){

    }
    private void setRecycler()
    {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapterKnjige= new AdapterKnjige(getContext(), slikeF,naziviF,autoriF,predmetiF,izdavaciF,godineIzdanjaF,ceneF,dodatniOpisiF,brojeviZainteresovanihF);
        recyclerView.setAdapter(adapterKnjige);
    }
    private void ucitajIzBaze(){
        databaseReference.child("Knjige").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Knjiga added = dataSnapshot1.getValue(Knjiga.class);

                    knjige.add(added);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Oglasi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Oglas added = dataSnapshot1.getValue(Oglas.class);
                    oglasi.add(added);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void napuniListe(){
        connection = new int[oglasi.size()];
        Oglas tempOglas;
        Knjiga tempKnjiga = null;
        for(int i = 0; i < oglasi.size(); i++){
            tempOglas = oglasi.get(i);
            for(int j = 0; j < knjige.size(); j++){
                if(tempOglas.getIdKnjige() == knjige.get(j).getId()){
                    connection[i] = j;
                    tempKnjiga = knjige.get(j);
                    break;
                }
            }
            slike.add(Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888));
            nazivi.add(tempKnjiga.getNaziv());
            autori.add(tempKnjiga.getAutori());
            predmeti.add(tempKnjiga.getPredmet());
            izdavaci.add(tempKnjiga.getIzdavac());
            godineIzdanja.add(Integer.toString(tempKnjiga.getGodinaIzdanja()));
            cene.add(Integer.toString(tempOglas.getCena()));
            dodatniOpisi.add(tempOglas.getDodatniOpis());
            brojeviZainteresovanih.add(Integer.toString(tempOglas.getBrojZainteresovanih()));
        }
    }
    private void start(){
        started = true;
        slikeF = new ArrayList<Bitmap>(slike);
        naziviF = new ArrayList<String>(nazivi);
        autoriF = new ArrayList<ArrayList<String>>(autori);
        predmetiF = new ArrayList<String>(predmeti);
        izdavaciF = new ArrayList<String>(izdavaci);
        godineIzdanjaF = new ArrayList<String>(godineIzdanja);
        ceneF = new ArrayList<String>(cene);
        dodatniOpisiF = new ArrayList<String>(dodatniOpisi);
        brojeviZainteresovanihF = new ArrayList<String>(brojeviZainteresovanih);
    }
    private void clear(){
        slikeF = new ArrayList<Bitmap>();
        naziviF = new ArrayList<String>();
        autoriF = new ArrayList<ArrayList<String>>();
        predmetiF = new ArrayList<String>();
        izdavaciF = new ArrayList<String>();
        godineIzdanjaF = new ArrayList<String>();
        ceneF = new ArrayList<String>();
        dodatniOpisiF = new ArrayList<String>();
        brojeviZainteresovanihF = new ArrayList<String>();
    }
}
