package com.example.bookapp.Fragmenti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentProfil extends Fragment implements View.OnClickListener {

    private TextView tvIme,tvPrezime,tvEmail,tvBrojOcena,tvProsecnaOcena;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String ime,prezime,email;
    private int brojOcena;
    private double prosecnaOcena;
    private ImageView slika;

    private RecyclerView recyclerView;

    private ArrayList<String> idOglasa=new ArrayList<>();

    private Button nalogP;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        initialize(view);

        citajBazu();

        //ucitajSliku();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

        slika=(ImageView)view.findViewById(R.id.slikaProfil);

        user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Korisnici").child(user.getUid());

        progressDialog=new ProgressDialog(getContext());

        nalogP=view.findViewById(R.id.nalog);
        nalogP.setOnClickListener(this);
    }

    private void citajBazu()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                citajInfoKorisnik(dataSnapshot);

                Log.d("USO","USO");

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
            Log.d("USO2","USO2");

            citanje();
        }
    }

    private void citanje() {

        CitanjeOglasa citanjeOglasa = new CitanjeOglasa();

        Log.d("USO3","USO3");

        citanjeOglasa.procitaj(idOglasa, recyclerView, getContext());
    }

    private void upisi()
    {
        tvIme.setText(ime + " ");
        tvPrezime.setText(prezime);
        tvProsecnaOcena.setText(String.valueOf(prosecnaOcena));
        tvBrojOcena.setText(String.valueOf(brojOcena));
        tvEmail.setText(email);
    }

    private void ucitajSliku() {

        progressDialog.setMessage("Ucitavanje...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid() + "/Korisnik/" + "image.jpg");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri).into(slika);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Nemate profilnu sliku!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
