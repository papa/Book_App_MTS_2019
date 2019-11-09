package com.example.bookapp.Fragmenti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.bookapp.Klase.Oglasi.CitanjeOglasa;
import com.example.bookapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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


    //Moje promenljive
    private ArrayList<String> idOglasa=new ArrayList<>();


    private ImageView slikeKnjige;

    private int[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_knjige, container, false);

        initialize(view);

        //nextImage();

        cenaRB.setOnClickListener(this);

        //postaviListenere();

        //-BOGDAN-
        //pretpostavljam da se to ovde radi

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Oglasi");

        //optimizovano
        ucitajIzBaze();

        napuniListe();
        start();
        napraviListu();
        //setRecycler();

        return view;
    }


    //<editor-fold desc="Ovo mi je animacija da se menjaju slike, koristicu je na drugom mesto al neka je, samo smanjite ovaj deo da ne smeta">
    private void nextImage(){
        slikeKnjige.setImageResource(imageArray[currentIndex]);
        //Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        //slikeKnjige.startAnimation(rotateimage);
        currentIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex>endIndex){
                    currentIndex--;
                    previousImage();
                }else{
                    nextImage();
                }

            }
        },1000); // here 1000(1 second) interval to change from current  to next image

    }
    private void previousImage(){
        slikeKnjige.setImageResource(imageArray[currentIndex]);
        //Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        //slikeKnjige.startAnimation(rotateimage);
        currentIndex--;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex<startIndex){
                    currentIndex++;
                    nextImage();
                }else{
                    previousImage(); // here 1000(1 second) interval to change from current  to previous image
                }
            }
        },1000);

    }
    //</editor-fold>


    //<editor-fold desc="postavilistener">
    /*private void postaviListenere()
    {
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

    }*/
    //</editor-fold>


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.slikaKnjige: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
            }break;*/
        }
    }

    private void napraviListu(){

    }
    private void ucitajIzBaze()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    idOglasa.add(snapshot.getKey());
                citanje();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void citanje() {

        CitanjeOglasa citanjeOglasa = new CitanjeOglasa();

        citanjeOglasa.procitaj(idOglasa, recyclerView, getActivity().getApplication().getApplicationContext());
    }

    private void napuniListe()
    {
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

    private void start()
    {
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

    private void clear()
    {
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
    private void initialize(View view)
    {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerKnjige);
        cenaRB=(CheckBox)view.findViewById(R.id.cenaCheckBox);
        predmetRB=(CheckBox)view.findViewById(R.id.predmetCheckBox);
        izdavacRB=(CheckBox)view.findViewById(R.id.izdavacCheckBox);
        cenaET=(EditText)view.findViewById(R.id.cenaFilterText);
        predmetET=(EditText)view.findViewById(R.id.predmetFilterText);
        izdavacET=(EditText)view.findViewById(R.id.izdavacFilterText);
        filter=(Button)view.findViewById(R.id.filterButton);

        imageArray = new int[8];
        imageArray[0] = R.drawable.ic_launcher_background;
        imageArray[1] = R.drawable.ic_launcher_foreground;
        imageArray[2] = R.drawable.ic_launcher_background;
        imageArray[3] = R.drawable.ic_launcher_background;
        imageArray[4] = R.drawable.ic_launcher_background;
        imageArray[5] = R.drawable.ic_launcher_background;
        imageArray[6] = R.drawable.ic_launcher_background;
        imageArray[7] = R.drawable.ic_launcher_background;

        startIndex = 0;
        endIndex = 7;
    }
}
